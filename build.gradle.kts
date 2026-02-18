plugins {
    id("java")
}

group = "dev.term4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //testImplementation(platform("org.junit:junit-bom:5.10.0"))
    //testImplementation("org.junit.jupiter:junit-jupiter")
    //testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("net.minestom:minestom:2026.02.09-1.21.11")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.0")
}

tasks.test {
    useJUnitPlatform()
}