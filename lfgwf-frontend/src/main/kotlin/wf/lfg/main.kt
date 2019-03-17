package wf.lfg

import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.browser.window

fun main() {
    window.onload = fun(_: Event) {
        fetch("1")

        val head = document.getElementsByTagName("head")[0]
        head?.appendChild(createStylesheetLink("style.css"))

        val input = document.getElementById("count_id") as HTMLInputElement
        val button = document.getElementById("button_id")
        button?.addEventListener("click", fun(_: Event) {
            fetch(input.value)
        })
    }
}

fun fetch(count: String) {
    val url = "http://localhost:8080/api/ping/$count"
    with(XMLHttpRequest()) {
        onloadend = fun(_: Event) {
            println(responseText)
            val entries = JSON.parse<Array<Entry>>(responseText)
            val textarea = document.getElementById("textarea_id") as HTMLTextAreaElement
            textarea.value = ""
            entries.forEach {
                textarea.value += "${it.message}\n"
            }
        }
        open("GET", url, async = true)
        send()
    }
}

fun createStylesheetLink(filePath: String): Element = document.createElement("link").apply {
    setAttribute("rel", "stylesheet")
    setAttribute("href", filePath)
}

external fun alert(message: Any?)

data class Entry(val message: String)
