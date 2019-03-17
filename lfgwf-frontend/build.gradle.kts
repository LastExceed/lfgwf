import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

// ignore errors. IDE is dumb but gradle gets it

plugins {
    id("kotlin2js")
}

dependencies {
    compile(kotlin("stdlib-js"))
}

tasks.withType<Kotlin2JsCompile> {
    kotlinOptions {
        metaInfo = true
        sourceMap = true
        outputFile = "$buildDir/app/js/${project.name}.js"
    }
}

sourceSets {
    val main by getting {
        output.setResourcesDir("$buildDir/app")
    }
}

val build by tasks.getting {
    doLast {
        configurations.compile.forEach { file ->
            copy {
                includeEmptyDirs = false
                from(zipTree(file.absolutePath))
                into("$buildDir/app/js/lib")
                include {
                    it.path.endsWith(".js") || it.path.endsWith(".js.map")
                }
            }
        }
    }
}
