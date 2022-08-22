plugins {
    kotlin("jvm")
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks.getByName("bootJar") {
    enabled = false
}