plugins {
    id("java")
}

group = "dev.cosrnic"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("net.minestom:minestom-snapshots:1_20_5-323c75f8a5")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}