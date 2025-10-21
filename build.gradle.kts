plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.nitish"
version = "0.0.1-SNAPSHOT"
description = "Practice project of Spring Boot Instagram clone"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation") // For @Valid and validation annotations
    //CREATE SMTP SYSTEM
    implementation("org.springframework.boot:spring-boot-starter-mail")
    // implementation("org.springframework.boot:spring-boot-starter-oauth2-client") // OAUTH2 LOGIN
    //FIREBASE NOTIFICATION
    implementation("com.google.firebase:firebase-admin:9.2.0")
    //TO VERIRY THE GOOGLE ID TOKEN
    implementation("com.google.api-client:google-api-client:2.1.0")
    implementation("com.google.http-client:google-http-client-jackson2:1.45.0")
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    //Cloudinary
    implementation("com.cloudinary:cloudinary-http44:1.37.0")
    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // ModelMapper
    implementation("org.modelmapper:modelmapper:3.2.0")

    // Database
    implementation("com.mysql:mysql-connector-j:9.0.0")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Jakarta Validation API (optional since spring-boot-starter-validation includes it)
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // Development only
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    // ignoreFailures=true
}
