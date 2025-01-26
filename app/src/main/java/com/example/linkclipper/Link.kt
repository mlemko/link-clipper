package com.example.linkclipper

/**
 * A sequence of [ClipAction]s that "clip" (remove tracking data) from a link belonging to a specific [domain].
 *
 * @param domain The specific domain that the [actions] are for.
 * @param actions A sequence of [ClipAction]s that are applied to "clip" a link.
 */
class Link(val domain: String, val actions: List<ClipAction>) {

    fun clip(link: String) : String {
        var clippedLink = link
        for (action in actions) {
            clippedLink = action.executeOn(clippedLink)
        }
        return clippedLink
    }

    fun domainFor(link: String) : Boolean {
        return link.substringBefore("?").indexOf(domain) > -1
    }
}