package wf.lfg

import org.w3c.dom.*
import kotlin.browser.document
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.dom.addClass
import kotlin.dom.appendText

@DslMarker
private annotation class HtmlTag

/** create, connect, configure, return */
private inline fun <reified T : Element> cccr(
    parent: Element,
    elementString: String,
    classes: String?,
    id: String?,
    configure: T.() -> Unit
): T {
    contract { callsInPlace(configure, InvocationKind.EXACTLY_ONCE) }
    return (document.createElement(elementString) as T).apply {
        parent.appendChild(this)
        classes?.let { addClass(*classes.split(" ").toTypedArray()) }
        id?.let { this.id = id }
        configure()
    }
}

/**
 * @param classes a space-separated list of css classes
 */
@HtmlTag
fun HTMLElement.button(
    classes: String? = null,
    id: String? = null,
    configure: HTMLButtonElement.() -> Unit = {}
): HTMLButtonElement {
    contract { callsInPlace(configure, InvocationKind.EXACTLY_ONCE) }
    return cccr(this, "button", classes, id, configure)
}

fun HTMLElement.text(text: String) = appendText(text)

@HtmlTag
fun HTMLTableElement.tr(
    classes: String? = null,
    id: String? = null,
    configure: HTMLTableRowElement.() -> Unit = {}
): HTMLTableRowElement {
    contract { callsInPlace(configure, InvocationKind.EXACTLY_ONCE) }
    return cccr(this, "tr", classes, id, configure)
}

@HtmlTag
fun HTMLTableRowElement.th(
    classes: String? = null,
    id: String? = null,
    configure: HTMLTableCellElement.() -> Unit = {}
): HTMLTableCellElement {
    contract { callsInPlace(configure, InvocationKind.EXACTLY_ONCE) }
    return cccr(this, "th", classes, id, configure)
}

@HtmlTag
fun HTMLTableRowElement.td(
    classes: String? = null,
    id: String? = null,
    configure: HTMLTableCellElement.() -> Unit = {}
): HTMLTableCellElement {
    contract { callsInPlace(configure, InvocationKind.EXACTLY_ONCE) }
    return cccr(this, "td", classes, id, configure)
}
