val kotlinVersion by extra("1.3.0")
val tornadofxVersion by extra("1.7.17")

plugins {
    java
    kotlin("jvm") version "1.3.72"
    application
}

application {
    mainClassName = "eu.bunburya.hexify.app.MainApp"
}

group = "eu.bunburya"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("no.tornado:tornadofx:$tornadofxVersion")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}