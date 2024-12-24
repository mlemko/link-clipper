package com.example.linkclipper

import com.example.linkclipper.ClipType.*

class ClipAction (private val type: ClipType, private val content: List<String>) {
    fun executeOn(link: String): String {
        var clippedLink = link
        when(type) {
            REMOVE_SI -> {
                val beforeParams = link.substringBefore('?')
                val afterParams = link.substringAfter('#', missingDelimiterValue = "")
                val params = link.substringBefore('#').substringAfter('?')
                val paramMap = UrlHelper.urlParamParse(params)
                for (sidMarker in content) {
                    paramMap.remove(sidMarker)
                    clippedLink = beforeParams
                    if (paramMap.isNotEmpty()) {
                        clippedLink += "?"
                        for ((param, value) in paramMap) {
                            clippedLink += "$param=$value&"
                        }
                        clippedLink.trimEnd('&')
                        if (afterParams.isNotEmpty()) {
                            clippedLink += "#$afterParams"
                        }
                    }
                }
            }
            REMOVE_QUERY -> {
                val beforeParams = link.substringBefore('?')
                val afterParams = link.substringAfter('#', missingDelimiterValue = "")
                clippedLink = "$beforeParams#$afterParams"
            }
        }
        return clippedLink
    }
}