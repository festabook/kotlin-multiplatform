import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.internal.utils.getLocalProperty
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

private val jksFilePath =
    getLocalProperty("JKS_FILE_PATH") ?: error("JKS_FILE_PATH가 local.properties에 없음")
private val storePasswordValue =
    getLocalProperty("STORE_PASSWORD") ?: error("STORE_PASSWORD가 local.properties에 없음")
private val keyPasswordValue =
    getLocalProperty("KEY_PASSWORD") ?: error("KEY_PASSWORD가 local.properties에 없음")
private val keyAliasValue =
    getLocalProperty("KEY_ALIAS") ?: error("KEY_ALIAS가 local.properties에 없음")
private val baseUrlDev =
    getLocalProperty("BASE_URL_DEV") ?: error("BASE_URL_DEV가 local.properties에 없음")
private val baseUrl =
    getLocalProperty("BASE_URL") ?: error("BASE_URL가 local.properties에 없음")

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.metro)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export(libs.metro.runtime)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktorfit.lib)
            implementation(libs.ktorfit.converters.response)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

buildkonfig {
    packageName = "com.daedan.festabook"

    defaultConfigs {
        buildConfigField(STRING, "FESTABOOK_URL", baseUrl)
    }
    targetConfigs {
        // android용 입니다.
        create("debug") {
            buildConfigField(STRING, "FESTABOOK_URL", baseUrlDev)
        }
        create("release") {
            buildConfigField(STRING, "FESTABOOK_URL", baseUrl)
        }
        // ios용 입니다.
        create("Debug") {
            buildConfigField(STRING, "FESTABOOK_URL", baseUrlDev)
        }
        create("Release") {
            buildConfigField(STRING, "FESTABOOK_URL", baseUrl)
        }
    }
}

android {
    namespace = "com.daedan.festabook"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    signingConfigs {
        create("release") {
            storeFile = file(jksFilePath)
            storePassword = storePasswordValue
            keyAlias = keyAliasValue
            keyPassword = keyPasswordValue
        }
    }

    defaultConfig {
        applicationId = "com.daedan.festabook"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            resValue("string", "app_name", "(Debug)Festabook")
        }

        release {
//            isMinifyEnabled = true
//            isShrinkResources = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro",
//            )
            resValue("string", "app_name", "Festabook")
            signingConfig = signingConfigs["release"]
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
