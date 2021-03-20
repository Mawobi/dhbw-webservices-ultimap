plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.netflix.dgs.codegen")
}

version = "0.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    //This is needed to be able to package the service to jar. For some strange reason it is not included by default.
    implementation("net.minidev:json-smart:2.3")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    testRuntimeOnly("com.h2database:h2")
}
