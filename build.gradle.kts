// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Specifies the Android application plugin but does not apply it immediately;
    // it is available for application in sub-projects
    id("com.android.application") version "8.1.3" apply false

    // Kotlin plugin for Android, specified but not applied at this level
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false

    // Google services plugin for things like Firebase, specified but not applied at this level
    id("com.google.gms.google-services") version "4.4.1" apply false

    // Kotlin Symbol Processing (KSP) for compile-time code generation, specified but not applied
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false

    id ("com.google.dagger.hilt.android") version "2.48" apply false
}


