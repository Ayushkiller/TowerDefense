import fr.xpdustry.toxopid.dsl.mindustryDependencies
import fr.xpdustry.toxopid.spec.ModMetadata

plugins {
    java
    id("fr.xpdustry.toxopid") version "3.2.0"
}

repositories {
    mavenCentral()
    maven(url = "https://raw.githubusercontent.com/Zelaux/MindustryRepo/master/repository")
    maven(url = "https://jitpack.io")
}

val metadata = ModMetadata.fromJson(file("src/main/resources/plugin.json"))
project.version = metadata.version

toxopid {
    compileVersion.set("v${metadata.minGameVersion}")
}

dependencies {
    mindustryDependencies()
    val usefulHash = "8caee092db"
    implementation("com.github.xzxADIxzx.useful-stuffs:bundle:$usefulHash")
}

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Little quirk of toxopid, since we are not building a mod,
// we need to remove the plugin from the mindustry client
tasks.runMindustryClient {
    mods.setFrom()
}
