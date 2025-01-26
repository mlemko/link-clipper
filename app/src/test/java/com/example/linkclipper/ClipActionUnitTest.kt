package com.example.linkclipper

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before


class ClipActionUnitTest {
    @Test
    fun removeSIType() {
        val sampleMarkers = listOf<String>("bsb", "si", "sols", "293j")
        val sampleClipAction = ClipAction(ClipType.REMOVE_SI, sampleMarkers)
        val sampleLinks = listOf<String>("http://example.com/?si=S28ijr3", "http://example.com/", "http://example.com/?not=922923", "http://example.com/?bsb=S28ijr3#title2", "http://example.com/?bsb=S28ijr3&si=aan98238_2&not=922923&293j=losi2873&n=sinc")
        val sampleResults = listOf<String>("http://example.com/", "http://example.com/", "http://example.com/?not=922923", "http://example.com/#title2", "http://example.com/?not=922923&n=sinc")

        for (i in sampleLinks.indices) {
            assertEquals(sampleResults[i], sampleClipAction.executeOn(sampleLinks[i]))
        }
    }

    @Test
    fun removeQueryType() {
        val sampleClipAction = ClipAction(ClipType.REMOVE_QUERY, null)
        val sampleLinks = listOf<String>("http://example.com/?si=S28ijr3", "http://example.com/", "http://example.com/?not=922923#title2", "http://example.com/?not=922923&bsb=S28ijr3#title2", "http://example.com/?bsb=S28ijr3&si=aan98238_2&not=922923&293j=losi2873&n=sinc")
        val sampleResults = listOf<String>("http://example.com/", "http://example.com/", "http://example.com/#title2", "http://example.com/#title2", "http://example.com/")

        for (i in sampleLinks.indices) {
            assertEquals(sampleResults[i], sampleClipAction.executeOn(sampleLinks[i]))
        }
    }
}