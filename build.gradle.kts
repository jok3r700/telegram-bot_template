import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val ktor_version = libs.versions.ktor.get()
val jvmTargetVersion = JavaVersion.VERSION_17

plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.telegram.bot)
}

group = "com.example.heroku"
version = "0.0.1"
java.targetCompatibility = jvmTargetVersion

application {
    mainClass.set("com.example.heroku.HerokuAppKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(jvmTargetVersion.majorVersion)
    }
    jvmToolchain(jvmTargetVersion.majorVersion.toInt())
}

tasks.create("stage").dependsOn("installDist")
