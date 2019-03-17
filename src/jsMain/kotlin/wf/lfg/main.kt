package wf.lfg

import kotlinx.html.button
import kotlinx.html.dom.create
import kotlinx.html.td
import kotlinx.html.tr
import org.w3c.dom.HTMLTableElement
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.browser.window

val squads = listOf(
    Squad("high index 1h for john prodman", 3, "LastExceed"),
    Squad("ESO 8 waves need buff", 1, "LastExceed"),
    Squad("Lith b5 radshare", 2, "LastExceed"),
    Squad("vault runs", 3, "LastExceed")
)

fun main() {
    window.onload = fun(_: Event) {
        with(WebSocket("ws://${window.location.host}/")) {
            onopen = fun(_: Event) {
                send("Here's some text")
            }
            onmessage = fun(event: MessageEvent) {
                println(event.data)
                send("bye")
            }
        }

        val table = document.getElementById("table") as HTMLTableElement
        squads.forEach { squad ->
            table.appendChild(document.create.tr {
                td { +squad.title }
                td(classes = "center") { +squad.openSlots.toString() }
                td { +squad.host }
                td(classes = "center") { button { +"button" } }
            })
        }
    }
}
