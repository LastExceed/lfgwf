package wf.lfg

import org.w3c.dom.HTMLTableElement
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.browser.window

val requests = listOf(
    "Malox10",
    "Shareeza",
    "NiliusRex"
)


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

        val requestsTable = document.getElementById("table_requests") as HTMLTableElement
        requests.forEach { request ->
            requestsTable.tr {
                td { text(request) }
                td {
                    button {
                        text("Invite")
                        onclick = fun(_: Event) {
                            invite(request)
                        }
                    }
                }
                td { button { text("") } }
            }
        }

        val resultsTable = document.getElementById("table_results") as HTMLTableElement
        squads.forEach { squad ->
            resultsTable.tr {
                td {
                    text(squad.title)
                }
                td("center") {
                    text(squad.openSlots.toString())
                }
                td {
                    text(squad.host)
                }
                td("center") {
                    button { text("button") }
                }
            }
        }
    }
}

fun invite(name: String) {
    println("sent invite to '$name'")
}
