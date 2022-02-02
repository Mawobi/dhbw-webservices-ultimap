import com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.dsl.SpringBootExtension

plugins {
    kotlin("jvm") version "1.4.30" apply false
    kotlin("plugin.spring") version "1.4.30" apply false
    id("org.springframework.boot") version "2.4.2" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    id("com.netflix.dgs.codegen") version "4.0.10" apply false
}

allprojects {
    group = "de.dhbw.mosbach.webservices"
}

subprojects {
    repositories {
        mavenCentral()
        jcenter()
    }

    pluginManager.withPlugin("java") {
        extensions.configure(JavaPluginExtension::class.java) {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        tasks.withType(Test::class.java) {
            useJUnitPlatform()
        }
    }

    // Run Spring Configurations after Java-Plugin is also loaded
    pluginManager.withPlugin("org.springframework.boot") {
        extensions.configure(SpringBootExtension::class.java) {
            buildInfo()
        }

        pluginManager.withPlugin("java") {
            extensions.getByType(SourceSetContainer::class.java)
                .getByName("main")
                .resources.srcDir("src/main/filteredResources")
        }

        tasks.getByName<ProcessResources>("processResources"){
            with(copySpec {
                from("src/main/filteredResources")
                expand(project.properties)
            })
            with(copySpec {
                from("src/main/resources/schema")
                into("public")
            })
            exclude { it.path.contains("filteredResources") }
        }
    }

    // Add dependencies for spring
    pluginManager.withPlugin("io.spring.dependency-management") {
        pluginManager.withPlugin("java") {
            dependencies {
                // Spring Boot base
                add("implementation", "org.springframework.boot:spring-boot-starter-actuator")
                add("implementation", "org.springframework.boot:spring-boot-starter-web")
                add("annotationProcessor", "org.springframework.boot:spring-boot-configuration-processor")
                add("developmentOnly", "org.springframework.boot:spring-boot-devtools")

                add("testImplementation", "org.springframework.boot:spring-boot-starter-test")

                // GraphQL
                add("implementation", "com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter:4.0.1")

                // Lombok
                add("compileOnly", "org.projectlombok:lombok")
                add("annotationProcessor", "org.projectlombok:lombok")
                add("testCompileOnly", "org.projectlombok:lombok")
                add("testAnnotationProcessor", "org.projectlombok:lombok")
            }
        }
    }

    // DGS Codegen
    @OptIn(ExperimentalStdlibApi::class)
    pluginManager.withPlugin("com.netflix.dgs.codegen") {
        // Workaround for bug in DGS-Generation Plugin
        // The wrong generated Sources Dir is added, since the value of generatedSourcesDir in the Task is queried before this configuration is applied.
        val javaSourceSet = extensions.getByType(SourceSetContainer::class.java).getByName("main").java

        // Remove the wrong dir from sources and add the correct one
        javaSourceSet.setSrcDirs(javaSourceSet.srcDirs.filter { !it.path.equals(File("$buildDir/generated").path) })
        javaSourceSet.srcDir("$buildDir/generated/dgs/java/generated")

        tasks.getByName<GenerateJavaTask>("generateJava") {
            packageName = "de.dhbw.mosbach.webservices.ultimap.graphql"
            generatedSourcesDir = "$buildDir/generated/dgs/java"
            generateClient = false
            language = "JAVA"
        }

        val generateJavaTestClient = tasks.create("generateJavaTestClient", GenerateJavaTask::class.java) {
            packageName = "de.dhbw.mosbach.webservices.ultimap.client.test"
            generatedSourcesDir = "$buildDir/generated/dgs/client-test/java"
            generateClient = true
            language = "JAVA"
        }
        extensions.getByType(SourceSetContainer::class.java).getByName("test").java
            .srcDir("$buildDir/generated/dgs/client-test/java/generated")

        pluginManager.withPlugin("java") {
            tasks.withType(JavaCompile::class.java) {
                dependsOn(generateJavaTestClient)
            }
        }
        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            tasks.withType(KotlinCompile::class.java) {
                dependsOn(generateJavaTestClient)
            }
        }
    }
}