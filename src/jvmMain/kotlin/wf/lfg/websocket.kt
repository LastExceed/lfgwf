import io.ktor.application.install
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.Routing
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import java.nio.ByteBuffer

private const val NAME_CHANGE = 0
private const val SQUAD_TITLE_CHANGE = 1
private const val SQUAD_SLOT_CHANGE = 2
private const val BUMP = 3
private const val REQUEST_INVITE = 4
private const val WITHDRAW = 5

@ObsoleteCoroutinesApi
fun Routing.configureWebSocket() {
    application.install(WebSockets)

    webSocket("/") {
        incoming.consumeEach { frame ->
            when (frame) {
                is Frame.Text -> {
                    val text = frame.readText()
                    outgoing.send(Frame.Text("YOU SAID $text"))
                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said bye"))
                    }
                }
                is Frame.Binary -> {
                    val data = frame.buffer
                    when (data.get().toInt()) {
                        NAME_CHANGE -> {
                            val newName = data.getString()
                            //do something with the new name
                        }
                        SQUAD_TITLE_CHANGE -> {
                            val newSquadTitle = data.getString()
                            //do something with the new name
                        }
                        SQUAD_SLOT_CHANGE -> {
                            val newSlotCount = data.get()
                        }
                        BUMP -> {
                            //bump contains no further data
                        }
                        REQUEST_INVITE -> {
                            val targetSquad = data.getInt()
                        }
                        WITHDRAW -> {
                            val targetSquad = data.getInt()
                        }
                    }
                }
            }
        }
    }
}

fun ByteBuffer.getString(): String {
    val stringData = ByteArray(this.get().toInt())
    this.get(stringData)
    return String(stringData)
}
