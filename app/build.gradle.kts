plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit)
    implementation(libs.guava)
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("ch.qos.logback:logback-core:1.5.7")
    implementation("ch.qos.logback:logback-classic:1.5.7")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("org.example.App")
}