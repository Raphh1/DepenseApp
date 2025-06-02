plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "3.0.1"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.example.demo1")
    mainClass.set("com.example.demo1.HelloApplication")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
}


tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.43.2.2")
}

jlink {
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "demo1"
    }
    jpackage {
        imageName = "DemoApp"
        installerName = "demoapp-installer"
        installerType = "deb"
        appVersion = "1.0.0"
        vendor = "TonNomOuEntreprise"
        description = "Application JavaFX de démonstration"
        icon = "src/main/resources/icon.png"
        installerType = when (System.getProperty("os.name")) {
            "Linux" -> "deb"
            "Mac OS X" -> "pkg"
            "Windows 10", "Windows 11" -> "msi"
            else -> "app-image"
        }
    }
}


