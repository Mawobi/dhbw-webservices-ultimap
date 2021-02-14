import com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask

plugins {
    java
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.netflix.dgs.codegen")
}

addClientGenerationTask("carinfo")
addClientGenerationTask("routing")
addClientGenerationTask("weather")

version = "0.0.1"

dependencies {
    kotlin("stdlib")
}

@OptIn(ExperimentalStdlibApi::class)
fun addClientGenerationTask(client: String) {
    val generationTask = tasks.create("generate${client.capitalize()}ClientSources", GenerateJavaTask::class.java)

    generationTask.generatedSourcesDir = "$buildDir/generated/dgs/client-$client/java"
    sourceSets.getByName("main").java.srcDir(generationTask.generatedSourcesDir + "/generated")

    generationTask.schemaPaths = mutableListOf("$projectDir/src/main/resources/graphql/client/$client")
    generationTask.packageName = "de.dhbw.mosbach.webservices.ultimap.client.$client"
    generationTask.generateClient = true

    tasks.compileJava.apply {
        get().dependsOn(generationTask)
    }
    tasks.compileKotlin.apply {
        get().dependsOn(generationTask)
    }
}