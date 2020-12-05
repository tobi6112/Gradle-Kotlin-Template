plugins {
    application
    java
    kotlin("jvm") version "1.4.20"
    id("com.diffplug.spotless") version "5.8.2"
}

group = "org.tobi6112"
version = "1.0-SNAPSHOT"

tasks.withType<Test> {
    testLogging.showStandardStreams = true
    useJUnitPlatform()
}

tasks.register<Jar>("uberJar") {
    archiveClassifier.set("uber")

    manifest {
        attributes(
            "Main-Class" to application.mainClassName
        )
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks.register<Copy>("installGitHooks") {
    from(File(rootProject.rootDir, "pre-commit.sh")).into {
        File (rootProject.rootDir, ".git/hooks")
    }
}

tasks {
    build {
        dependsOn("installGitHooks")
    }
}

application {
    mainClassName = "org.tobi6112.MainKt"
}

spotless {
    ratchetFrom("origin/master")
    encoding("UTF-8")
    lineEndings = com.diffplug.spotless.LineEnding.UNIX

    format("misc") {
        target("Dockerfile", "*.md", ".gitignore")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    kotlin {
        ktlint()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

dependencies {
    implementation(kotlin("stdlib"))
    testCompile("junit", "junit", "4.12")
}
