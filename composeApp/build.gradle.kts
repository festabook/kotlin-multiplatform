import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.google.devtools.ksp.gradle.KspAATask
import dev.mokkery.gradle.ApplicationRule
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

private val appVersionName = providers.gradleProperty("APP_VERSION_NAME").orNull
    ?: error("APP_VERSION_NAME가 gradle.properties에 없음")
private val appVersionCode = providers.gradleProperty("APP_VERSION_CODE").orNull
    ?: error("APP_VERSION_CODE가 gradle.properties에 없음")


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
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")

    cocoapods {
        version = "2.0.1"
        summary = "festabook"
        homepage = "https://landing.festabook.app/"
        ios.deploymentTarget = "16.0"

        pod("NMapsMap") {
            version = "3.23.1"
        }
    }
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
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
            implementation(libs.map.sdk)
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.appcompat)
        }
        commonMain.dependencies {
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
        buildConfigField(STRING, "NAVER_MAP_STYLE_ID", naverMapStyleId)
        buildConfigField(STRING, "NAVER_MAP_CLIENT_ID", naverMapClientId)
        buildConfigField(STRING, "FESTABOOK_URL", baseUrl)
        buildConfigField(STRING, "FESTABOOK_IMAGE_URL", baseImageUrl)
        buildConfigField(STRING, "APP_VERSION_NAME", appVersionName)
    }
    targetConfigs {
        // android용 입니다.
        create("debug") {
            buildConfigField(STRING, "FESTABOOK_IMAGE_URL", baseImageUrlDev)
            buildConfigField(STRING, "FESTABOOK_URL", baseUrlDev)
        }
        create("release") {
            buildConfigField(STRING, "FESTABOOK_IMAGE_URL", baseImageUrl)
            buildConfigField(STRING, "FESTABOOK_URL", baseUrl)
        }
        // ios용 입니다.
        create("Debug") {
            buildConfigField(STRING, "FESTABOOK_IMAGE_URL", baseImageUrlDev)
            buildConfigField(STRING, "FESTABOOK_URL", baseUrlDev)
        }
        create("Release") {
            buildConfigField(STRING, "FESTABOOK_IMAGE_URL", baseImageUrl)
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
        versionCode = appVersionCode.toInt()
        versionName = appVersionName
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

val updateIosVersion by tasks.registering {

    val plistFile = rootProject.layout.projectDirectory.file("iosApp/iosApp/Info.plist")
    val versionName = providers.gradleProperty("APP_VERSION_NAME")
    val versionCode = providers.gradleProperty("APP_VERSION_CODE")

    inputs.file(plistFile)
    inputs.property("versionName", versionName)
    inputs.property("versionCode", versionCode)

    doLast {
        val file = plistFile.asFile

        var text = file.readText()

        text = text.replace(
            Regex("<key>CFBundleShortVersionString</key>\\s*<string>.*</string>"),
            "<key>CFBundleShortVersionString</key>\n\t\t<string>${versionName.get()}</string>"
        )

        text = text.replace(
            Regex("<key>CFBundleVersion</key>\\s*<string>.*</string>"),
            "<key>CFBundleVersion</key>\n\t\t<string>${versionCode.get()}</string>"
        )

        file.writeText(text)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>().configureEach {
    dependsOn(updateIosVersion)
}

tasks.named("build") {
    dependsOn(updateIosVersion)
}