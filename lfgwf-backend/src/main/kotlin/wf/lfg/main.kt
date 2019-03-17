package wf.lfg

import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.header
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, 8080) {
        routing {
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
