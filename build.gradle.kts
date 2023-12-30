// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    kotlin("kapt") version "1.8.0"
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.8.0"))
    }
}
