@file:Suppress("PropertyName")

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

val this_version: String by rootProject.extra
val mavenForPublishing: (PublishingExtension) -> Unit by rootProject.extra
val ktor_version: String by rootProject.extra
val kotlinx_coroutines_version: String by rootProject.extra
val kotlin_version: String by rootProject.extra
val jackson_version: String by rootProject.extra
val assertk_version: String by rootProject.extra

fun setGroupAndVersion(publication: MavenPublication) {
    publication.groupId = "org.jetbrains"
    publication.version = this_version
}

kotlin {
    explicitApi()
    jvm {
        compilations.configureEach {
            tasks.named(compileKotlinTaskName).configure {
                kotlinOptions {
                    jvmTarget = "1.8"
                }
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
        mavenPublication {
            setGroupAndVersion(this)
            artifactId = "space-sdk-runtime-jvm"
            pom {
                name.set("Space SDK runtime")
                description.set("Runtime for JetBrains Space SDK")
            }
        }
    }

    js(BOTH) {
        browser {
        }
        nodejs {
        }
        mavenPublication {
            setGroupAndVersion(this)
            artifactId = "space-sdk-runtime-js"
            pom {
                name.set("Space SDK runtime")
                description.set("Runtime for JetBrains Space SDK")
            }
        }

        compilations.configureEach {
            tasks.named(compileKotlinTaskName).configure {
                kotlinOptions {
                    metaInfo = true
                    sourceMap = true
                    sourceMapEmbedSources = "always"
                    moduleKind = "commonjs"
                    main = "call"
                }
            }
        }
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                api("io.ktor:ktor-client-core:$ktor_version")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines_version")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.2.0")
                implementation("io.github.microutils:kotlin-logging:2.0.3")
            }
        }

        getByName("jvmMain") {
            dependencies {
                api("com.fasterxml.jackson.core:jackson-core:$jackson_version")
                api("com.fasterxml.jackson.core:jackson-databind:$jackson_version")
            }
        }

        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation("com.willowtreeapps.assertk:assertk-jvm:$assertk_version")
            }
        }

        getByName("jsMain") {
            dependencies {
                api("io.ktor:ktor-client-js:$ktor_version")
            }
        }
    }
}

publishing {
    mavenForPublishing(this)
    publications {
        getByName("kotlinMultiplatform", MavenPublication::class) {
            setGroupAndVersion(this)
            artifactId = "space-sdk-runtime"
            pom {
                name.set("Space SDK runtime")
                description.set("Runtime for JetBrains Space SDK")
            }
        }
    }
}
