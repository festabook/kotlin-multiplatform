import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.google.devtools.ksp.gradle.KspAATask
import dev.mokkery.gradle.ApplicationRule
import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.internal.utils.getLocalProperty
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask
import org.jlleitschuh.gradle.ktlint.tasks.KtLintFormatTask

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

private val baseImageUrlDev =
    getLocalProperty("IMAGE_BASE_URL_DEV") ?: error("IMAGE_BASE_URL_DEV가 local.properties에 없음")

private val baseImageUrl =
    getLocalProperty("IMAGE_BASE_URL") ?: error("IMAGE_BASE_URL가 local.properties에 없음")

private val naverMapStyleId =
    getLocalProperty("NAVER_MAP_STYLE_ID") ?: error("NAVER_MAP_STYLE_ID가 local.properties에 없음")

private val naverMapClientId =
    getLocalProperty("NAVER_MAP_CLIENT_ID") ?: error("NAVER_MAP_CLIENT_ID가 local.properties에 없음")

private val appBundleId =
    getLocalProperty("APP_BUNDLE_ID") ?: error("APP_BUNDLE_ID가 local.properties에 없음")

private val appBundleIdDev =
    getLocalProperty("APP_BUNDLE_ID_DEV") ?: error("APP_BUNDLE_ID_DEV가 local.properties에 없음")

private val buildFlavor =
    project.properties["buildkonfig.flavor"]?.toString() ?: "dev"


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
    alias(libs.plugins.mokkery)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.firebaseCrashlytcis)
    alias(libs.plugins.google.gms.services)
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")

    cocoapods {
        version = "2.0.1"
        summary = "festabook"
        homepage = "https://landing.festabook.app/"
        ios.deploymentTarget = "17.0"

        pod("NMapsMap")
        pod("FirebaseCrashlytics")
        pod("FirebaseAnalytics")
        pod("FirebaseMessaging")
        pod("FirebaseCore")
    }
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
            implementation(libs.app.update.ktx)
            implementation(libs.map.sdk)
            implementation(libs.play.services.location)
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.appcompat)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.crashlytics.ndk)
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.messaging)
        }
        commonMain.dependencies {
            implementation(libs.compose.navigationevent)
            implementation(libs.compose.navigation)
            implementation(libs.coil.compose)
            implementation(libs.landscapist.coil3)
            implementation(libs.landscapist.placeholder)
            implementation(libs.landscapist.zoomable)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(libs.material.icons.core)
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
            implementation(libs.compottie)
            implementation(libs.metrox.viewmodel.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

buildkonfig {
    packageName = "com.daedan.festabook"

    defaultConfigs {
        buildConfigField(STRING, "BUILD_FLAVOR", buildFlavor)
        buildConfigField(STRING, "NAVER_MAP_STYLE_ID", naverMapStyleId)
        buildConfigField(STRING, "NAVER_MAP_CLIENT_ID", naverMapClientId)
        buildConfigField(STRING, "FESTABOOK_URL", baseUrlDev)
        buildConfigField(STRING, "FESTABOOK_IMAGE_URL", baseImageUrlDev)
        buildConfigField(STRING, "APP_BUNDLE_ID", appBundleIdDev)
    }

    defaultConfigs("release") {
        buildConfigField(STRING, "BUILD_FLAVOR", buildFlavor)
        buildConfigField(STRING, "NAVER_MAP_STYLE_ID", naverMapStyleId)
        buildConfigField(STRING, "NAVER_MAP_CLIENT_ID", naverMapClientId)
        buildConfigField(STRING, "FESTABOOK_URL", baseUrl)
        buildConfigField(STRING, "FESTABOOK_IMAGE_URL", baseImageUrl)
        buildConfigField(STRING, "APP_BUNDLE_ID", appBundleId)
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
    ktlintRuleset(libs.ktlint)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KtLintCheckTask>().configureEach {
    dependsOn(tasks.withType<KspAATask>())
}

tasks.withType<KtLintFormatTask>().configureEach {
    dependsOn(tasks.withType<KspAATask>())
}

mokkery {
    rule.set(ApplicationRule.All)
}

ktorfit {
    compilerPluginVersion.set("2.3.3")
}
