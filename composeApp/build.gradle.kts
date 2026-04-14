import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}


kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.sqldelight.android.driver)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.navigation.compose)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
            //implementation(libs.datastore)

            implementation(libs.ktor.clientCore)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.clientContentNegotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kermit.logging)
            implementation(libs.settings)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.sqldelight.coroutines.extensions)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.sqldelight.sqlite.driver)
        }
        // Since iosArm64 and iosSimulatorArm64 are defined, we use their shared source set or specific ones
        // Usually, there is an 'iosMain' created by convention if you define ios targets
    }
}

    android {
        namespace = "com.example.simplifymypantry"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        defaultConfig {
            applicationId = "com.example.simplifymypantry"
            minSdk = libs.versions.android.minSdk.get().toInt()
            targetSdk = libs.versions.android.targetSdk.get().toInt()
            versionCode = 1
            versionName = "1.0"
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    sqldelight {
        databases {
            create("PantryDatabase") {
                packageName.set("com.example.simplifymypantry.pantry.data")
                srcDirs.setFrom("src/commonMain/sqldelight/Pantry")
            }
            create("AccountDatabase") {
                packageName.set("com.example.simplifymypantry.account.data")
                srcDirs.setFrom("src/commonMain/sqldelight/Account")
            }
        }
    }

    dependencies {
        debugImplementation(libs.compose.uiTooling)
    }

    compose.desktop {
        application {
            mainClass = "com.example.simplifymypantry.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "com.example.simplifymypantry"
                packageVersion = "1.0.0"
            }
        }
    }
