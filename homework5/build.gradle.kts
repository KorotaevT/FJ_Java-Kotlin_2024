plugins {
    id("java")
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("jacoco")
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(":simple-starter"))

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.wiremock:wiremock-standalone:3.6.0")
    testImplementation("org.wiremock.integrations.testcontainers:wiremock-testcontainers-module:1.0-alpha-13")
    testImplementation("org.testcontainers:junit-jupiter:1.19.7")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("io.projectreactor:reactor-core:3.6.10")

}

tasks.test {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.11"
}

val jacocoExclusions = listOf(
    "org/example/dto/**",
    "org/example/model/**",
    "org/example/configuration/**"
)

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(jacocoExclusions)
                }
            }
        )
    )
    reports {
        xml.required.set(true)
        csv.required.set(true)
        html.required.set(true)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }

}