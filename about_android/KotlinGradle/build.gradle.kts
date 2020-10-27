// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    // before
    // ext.kotlin_version = "1.4.10"
    // after
    val kotlinVersion by rootProject.extra("1.4.10")
    repositories {
        google()
        jcenter()
        // before
        // maven { url 'https://jitpack.io' }
        // after
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
}

// before
//task clean(type: Delete)  {
//    delete rootProject.buildDir
//}
// after
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}