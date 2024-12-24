package com.example.linkclipper

class Link(val domain: String, val actions: List<ClipAction>) {

    fun clip(link: String) : String {
        var clippedLink = link
        for (action in actions) {
            clippedLink = action.executeOn(clippedLink)
        }
        return clippedLink
    }

    fun domainFor(link: String) : Boolean {
        return link.indexOf(domain) > -1
    }
}