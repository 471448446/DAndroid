//before
//apply plugin: 'com.android.application'
//apply plugin: 'kotlin-android'
//apply plugin: 'kotlin-android-extensions'
// after way1
//apply(plugin = "com.android.application")
//apply(plugin = "kotlin-android")
//apply(plugin = "kotlin-android-extensions")
// way2
plugins {
    id("com.android.application")
    id("kotlin-android")
    // or
//    kotlin("android")
    kotlin("android.extensions")
}


android {
    compileSdkVersion(29)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "com.better.learn.kotlingradle"
        minSdkVersion(16)
        targetSdkVersion(29)
        versionCode = Version.appVersion
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        //before
//        release {
//            minifyEnabled false
//            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
        //after
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                File("proguard-rules.pro")
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // before
//    implementation fileTree (dir: 'libs', include: ['*.jar'])
    // after
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //before
//    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    //after
//    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra.get("$kotlinVersion")}") //这种失败
//    implementation(kotlin("stdlib-jdk7", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION)) // 可以
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.kotlin}") // 使用自定义的变量
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}