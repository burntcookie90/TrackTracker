plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("com.squareup.sqldelight")
}

sqldelight {
  database("Database") {
    packageName = "me.vishnu.tracktracker.db"
    schemaOutputDirectory = file("build/schemas")
  }
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

android {
  compileSdk = 31
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = 24
    targetSdk = 31
  }
}