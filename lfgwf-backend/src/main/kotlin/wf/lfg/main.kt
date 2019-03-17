package wf.lfg

import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.response.header
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.*
import java.io.File
import java.nio.file.Paths

fun main() {
    embeddedServer(Netty, 8080) {
        routing {
            static("js") {
                staticRootFolder = File("lfgwf-frontend/build/app")
                files("js")
            }
            get("/") {
                println(Paths.get("").toAbsolutePath())
                call.respondHtml {
                    lang = "en"
                    head {
                        meta(charset = "UTF-8")
                        title { +"Console Output" }
                    }
                    body {
                        div {
                            style = "display:block"
                            label { +"Enter number of elements to fetch:" }
                            input {
                                id = "count_id"
                                name = "count"
                                value = "1"
                            }
                        }
                        button {
                            id = "button_id"
                            type = ButtonType.button
                            style = "display:block"
                            +"Submit"
                        }
                        textArea {
                            id = "textarea_id"
                            style = "width:100%;height:200px"
                        }
                        script {
                            type = "text/javascript"
                            src = "js/lib/kotlin.js"
                        }
                        script {
                            type = "text/javascript"
                            src = "js/lfgwf-frontend.js"
                        }
                    }
                }
            }
            get("/api/ping/{count?}") {
                var count = call.parameters["count"]?.toInt() ?: 1
                if (count < 1) count = 1
                val entries = Array(count) { Entry("$it: Hello, World!") }
                call.response.header("Access-Control-Allow-Origin", "*")
                call.respondText(Gson().toJson(entries), ContentType.Application.Json)
            }
        }
    }.start(wait = true)
}

data class Entry(val message: String)
