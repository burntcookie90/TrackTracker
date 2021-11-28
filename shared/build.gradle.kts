plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("com.squareup.sqldelight")
  id("com.google.devtools.ksp") version "1.5.31-1.0.1"
}

sqldelight {
  database("Database") {
    packageName = "me.vishnu.tracktracker.db"
    schemaOutputDirectory = file("build/schemas")
  }
}

dependencies {
  add("kspMetadata", "me.tatarka.inject:kotlin-inject-compiler-ksp:0.4.0")
}

kotlin {
  android()

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach {
    it.binaries.framework {
      baseName = "shared"
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt") {
          version {
            strictly("1.5.2-native-mt")
          }
        }
        implementation("com.squareup.sqldelight:coroutines-extensions:1.5.3")
        implementation("io.github.aakira:napier:2.1.0")
        api("me.tatarka.inject:kotlin-inject-runtime:0.4.0")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val androidMain by getting {
      dependencies {
        dependsOn(commonMain)
        implementation("com.squareup.sqldelight:android-driver:1.5.3")
      }
    }
    val androidTest by getting {
      dependencies {
        dependsOn(commonTest)
        implementation(kotlin("test-junit"))
        implementation("junit:junit:4.13.2")
      }
    }
    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosMain by creating {
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      dependencies {
        implementation("com.squareup.sqldelight:native-driver:1.5.3")
      }
      iosSimulatorArm64Main.dependsOn(this)
    }
    val iosX64Test by getting
    val iosArm64Test by getting
    val iosSimulatorArm64Test by getting
    val iosTest by creating {
      dependsOn(commonTest)
      iosX64Test.dependsOn(this)
      iosArm64Test.dependsOn(this)
      iosSimulatorArm64Test.dependsOn(this)
    }
  }
}

// Generate common code with ksp instead of per-platform, hopefully this won't be needed in the future.
// https://github.com/google/ksp/issues/567
kotlin.sourceSets.commonMain {
  kotlin.srcDir("build/generated/ksp/commonMain/kotlin")
}
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
  if (name != "kspKotlinMetadata") {
    dependsOn("kspKotlinMetadata")
  }
}

android {
  compileSdk = 31
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = 24
    targetSdk = 31
  }
}