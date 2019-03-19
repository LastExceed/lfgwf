package wf.lfg

import azadev.kotlin.css.*
import azadev.kotlin.css.dimens.px
import configureWebSocket
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.html.*
import java.io.File

@ObsoleteCoroutinesApi
fun main() {
    embeddedServer(Netty, 8080) {
        routing {
            configureStatic()
            configureWebSocket()
            configureRoot()
        }
    }.start(wait = true)
}

fun Routing.configureStatic() {
    val currentDir = File(".").absoluteFile
    application.environment.log.info("Current directory: $currentDir")

    val webDir = listOf("web", "../src/jsMain/web", "src/jsMain/web")
        .map { File(currentDir, it) }
        .firstOrNull { it.isDirectory }
        ?.absoluteFile
        ?: error("Can't find 'web' directory")

    static("js") {
        files(webDir)
    }
}

fun Routing.configureRoot() {
    get("/") {
        call.respondHtml {
            lang = "en"
            head {
                meta(charset = "UTF-8")
                title { +"Console Output" }
                style {
                    unsafe {
                        +Stylesheet {
                            div {
                                verticalAlign = CENTER
                            }
                            table {
                                borderCollapse = COLLAPSE
                            }
                            th {
                                padding = 4.px
                            }
                            td {
                                padding = 4.px
                                border = "1px solid black"
                            }
                            ".left" {
                                textAlign = LEFT
                            }
                            ".center" {
                                textAlign = CENTER
                            }
                            ".plus-minus" {
                                padding = 0
                            }
                        }.render()
                    }
                }
            }
            body {
                table {
                    id = "table"
                    tr {
                        th(classes = "left") { +"Title" }
                        th { +"Open Slots" }
                        th { +"Host" }
                        th {}
                    }
                }
                script { src = "js/kotlin.js" }
                script { src = "js/kotlinx-html-js.js" }
                script { src = "js/lfgwf.js" }
            }
        }
    }
}
