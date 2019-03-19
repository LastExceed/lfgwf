@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform") version "1.3.21"
}

group = "wf.lfg"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

val slf4jVersion = "1.7.26"
val ktorVersion = "1.1.3"
val gsonVersion = "2.8.5"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        val main by compilations.getting {
            defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
                    implementation("io.ktor:ktor-server-core:$ktorVersion")
                    implementation("io.ktor:ktor-server-netty:$ktorVersion")
                    implementation("io.ktor:ktor-html-builder:$ktorVersion")
                    implementation("io.ktor:ktor-websockets:$ktorVersion")
                    implementation("com.google.code.gson:gson:$gsonVersion")
                }
            }
        }
        val test by compilations.getting {
            defaultSourceSet {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                }
            }
        }
    }

    js {
        compilations.all {
            kotlinOptions {
                languageVersion = "1.3"
                moduleKind = "umd"
                sourceMap = true
                sourceMapEmbedSources = "always"
                metaInfo = true
                freeCompilerArgs = listOf("-Xuse-experimental=kotlin.contracts.ExperimentalContracts")
            }
        }
        val main by compilations.getting {
            defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-js"))
                }
            }
        }
        val test by compilations.getting {
            defaultSourceSet {
                dependencies {
                    implementation(kotlin("test-js"))
                }
            }
        }
    }
}

val webDir = project.file("src/jsMain/web")
val jsCompilations = kotlin.targets["js"].compilations

val populateWebDir by tasks.creating {
    dependsOn("jsMainClasses")

    doLast {
        copy {
            from(jsCompilations["main"].output)
            from(kotlin.sourceSets["jsMain"].resources.srcDirs)
            jsCompilations["test"].compileDependencyFiles.forEach {
                if (it.exists() && !it.isDirectory) {
                    from(zipTree(it.absolutePath).matching { include("*.js", "*.js.map") })
                }
            }
            into(webDir)
        }
    }
}

val jsJar by tasks.getting { dependsOn(populateWebDir) }

val run by tasks.creating(JavaExec::class) {
    dependsOn("jvmMainClasses", "jsJar")

    main = "wf.lfg.MainKt"
    classpath(
        kotlin.targets["jvm"].compilations["main"].output.allOutputs.files,
        configurations["jvmRuntimeClasspath"]
    )
    args = listOf()
}
