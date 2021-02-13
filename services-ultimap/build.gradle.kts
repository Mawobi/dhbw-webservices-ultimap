import org.springframework.boot.gradle.dsl.SpringBootExtension

plugins {
    id("org.springframework.boot") version "2.4.2" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

allprojects {
    group = "de.dhbw.mosbach.webservices"
}

subprojects {
    repositories {
        mavenCentral()
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

        tasks.withType(ProcessResources::class.java) {
            with(copySpec {
                from("src/main/filteredResources")
                expand(project.properties)
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

                // GraphQL & GraphiQL (UI)
                val graphQlLibVersion = "11.0.0"
                add("implementation", "com.graphql-java-kickstart:graphql-spring-boot-starter:$graphQlLibVersion")
                add("implementation", "com.graphql-java-kickstart:graphiql-spring-boot-starter:$graphQlLibVersion")
                add("implementation", "com.graphql-java-kickstart:graphql-java-tools:$graphQlLibVersion")

                add("testImplementation", "com.graphql-java-kickstart:graphql-spring-boot-starter-test:$graphQlLibVersion")

                // Lombok
                add("compileOnly", "org.projectlombok:lombok")
                add("annotationProcessor", "org.projectlombok:lombok")
            }
        }
    }
}