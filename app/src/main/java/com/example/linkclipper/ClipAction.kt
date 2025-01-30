package com.example.linkclipper

import com.example.linkclipper.ClipType.*

/**
 * Represents a single step in "clipping". ClipActions are domain-independent,
 * they do not check for domain name (or contain any domain information)
 * before executing the specified action.
 *
 * @param type The kind of clipping this ClipAction does.
 * @param content Extra information required by this ClipAction to perform a clip.
 */
class ClipAction(private val type: ClipType, private val content: List<String>?) {
    /**
     * Perform this [ClipAction] on a given link.
     */
    fun executeOn(link: String): String {
        var clippedLink = link
        when (type) {
            REMOVE_SI -> {
                val beforeParams = link.substringBefore('?')
                val afterParams = link.substringAfter('#', missingDelimiterValue = "")
                val params = link.substringBefore('#').substringAfter('?', "")
                val paramMap = UrlHelper.urlParamParse(params)
                if (content != null) {
                    for (sidMarker in content) {
                        paramMap.remove(sidMarker)
                    }
                    clippedLink = beforeParams
                    if (paramMap.isNotEmpty()) {
                        clippedLink += "?"
                        for ((param, value) in paramMap) {
                            clippedLink += "$param=$value&"
                        }
                        clippedLink = clippedLink.trimEnd('&')
                    }
                    if (afterParams.isNotEmpty()) {
                        clippedLink += "#$afterParams"
                    }
                }
            }

            REMOVE_QUERY -> {
                val beforeParams = link.substringBefore('?')
                val afterParams = link.substringAfter('#', missingDelimiterValue = "")
                clippedLink = beforeParams
                if (afterParams.isNotEmpty()) {
                    clippedLink += "#$afterParams"
                }
            }
        }
        return clippedLink
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is ClipAction) return false
        if (type != other.type) return false
        if (!content.isNullOrEmpty()) {
            if (other.content.isNullOrEmpty() || other.content.size != content.size) return false
            for (i in content.indices) {
                if (content[i] != other.content[i]) return false
            }
        } else {
            if (!other.content.isNullOrEmpty()) return false
        }
        return true
    }

    override fun hashCode(): Int {
        return (type.toString() + content.toString()).hashCode()
    }
}