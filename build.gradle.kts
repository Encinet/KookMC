plugins {
    kotlin("jvm") version "1.7.21"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.encinet"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven ("https://repo.purpurmc.org/snapshots")
    maven ("https://jitpack.io")
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.20.1-R0.1-SNAPSHOT")
    implementation("com.github.hank9999:kook-kt:0.0.4")
    implementation("org.yaml:snakeyaml:1.29")
}

application {
    mainClass.set("org.encinet.kookmc.KookMC")
}

