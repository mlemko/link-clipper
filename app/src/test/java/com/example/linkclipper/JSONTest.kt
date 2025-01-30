package com.example.linkclipper

import org.junit.Test
import org.json.*
import org.junit.Assert.*


class JSONTest {
    val samplejson =
        """{"domain_list":{"youtube.com":{"clipActions":[{"type":0,"markers":["si"]}]},"youtu.be":{"clipActions":[{"type":0,"markers":["si"]}]},"instagram.com":{"clipActions":[{"type":0,"markers":["igsh"]}]},"tumblr.com":{"clipActions":[{"type":0,"markers":["source"]}]},"open.spotify.com":{"clipActions":[{"type":0,"markers":["si"]}]}}}"""

    val samplelinks = listOf(
        Link("youtube.com", listOf(ClipAction(ClipType.REMOVE_SI, listOf("si")))),
        Link("youtu.be", listOf(ClipAction(ClipType.REMOVE_SI, listOf("si")))),
        Link("instagram.com", listOf(ClipAction(ClipType.REMOVE_SI, listOf("igsh")))),
        Link("tumblr.com", listOf(ClipAction(ClipType.REMOVE_SI, listOf("source")))),
        Link("open.spotify.com", listOf(ClipAction(ClipType.REMOVE_SI, listOf("si")))))

    @Test
    fun jsonToLinksTest() {
        val generatedLinks = JSONHelper.jsonToLinkDict(JSONObject(samplejson))

        for (link in samplelinks) {
            assertTrue("Generated links does not contain key: ${link.domain}" ,generatedLinks.containsKey(link.domain))
            assertEquals(generatedLinks[link.domain]?.domain, link.domain)
            assertEquals(generatedLinks[link.domain]?.actions?.size, link.actions.size)
            for (i in link.actions.indices) {
                assertEquals(generatedLinks[link.domain]?.actions?.get(i), link.actions[i])
            }
        }
    }
}

