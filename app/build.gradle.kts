plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

// Codemagic provides these when a keystore is attached via android_signing
val keystorePath: String? = System.getenv("CM_KEYSTORE_PATH")

android {
    namespace = "com.ginjahub.app"
    compileSdk = 36
    buildFeatures { buildConfig = true }

    defaultConfig {
        applicationId = "pt.ginjasfit.quintadasginjeiras.obidos.farmstay.countryside.reservations.app.gf7k29xq"
        minSdk = 24
        targetSdk = 36
        val handoffKey = providers.environmentVariable("AF_HANDOFF_CONFIG_KEY").orElse("p7a39e15d8c024bf691e2").get()
        require(handoffKey.matches(Regex("[A-Za-z0-9_]+")))
        buildConfigField("String", "AF_HANDOFF_CONFIG_KEY", "\"$handoffKey\"")
        val appsFlyerDevKey = providers.environmentVariable("APPSFLYER_DEV_KEY").orElse("").get()
        require(appsFlyerDevKey.matches(Regex("[A-Za-z0-9_-]*"))) {
            "APPSFLYER_DEV_KEY contains unsupported characters"
        }
        buildConfigField("String", "APPSFLYER_DEV_KEY", "\"$appsFlyerDevKey\"")
        val returnHost = providers.gradleProperty("returnHost").orElse("not-configured.invalid").get()
        require(returnHost.matches(Regex("[A-Za-z0-9.-]+")))
        buildConfigField("String", "RETURN_HOST", "\"$returnHost\"")
        manifestPlaceholders["returnHost"] = returnHost
        versionCode = maxOf(5, (project.findProperty("versionCode") as String?)?.toIntOrNull() ?: 5)
        versionName = "1.0.2"
    }

    signingConfigs {
        if (keystorePath != null) {
            create("release") {
                storeFile = file(keystorePath)
                storePassword = System.getenv("CM_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("CM_KEY_ALIAS")
                keyPassword = System.getenv("CM_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}


dependencies {
    implementation("com.appsflyer:af-android-sdk:6.17.1")
    implementation("androidx.activity:activity:1.11.0")
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.webkit:webkit:1.14.0")
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-analytics")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20240303")
}
