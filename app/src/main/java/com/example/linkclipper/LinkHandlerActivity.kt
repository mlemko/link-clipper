package com.example.linkclipper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import java.io.Console
import java.io.Serializable



class LinkHandlerActivity : AppCompatActivity() {

    private val domainToSidMarker: Map<String, String> = mapOf(
        "youtube.com" to "si",
        "youtu.be" to "si",
        "instagram.com" to "igsh",
        "tumblr.com" to "source",
        "spotify.com" to "si"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data: Uri? = intent?.data
        if (intent?.type == "text/plain") {
            //handle intent
            handleSentText(intent)
        }
        finish()
    }

    private fun handleSentText(thIntent: Intent) {
        val sharedText: String? = thIntent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            val linkStart = sharedText.indexOf("http")
            if (linkStart > -1) {
                val linkEnd = sharedText.lastIndexOf(' ', linkStart)
                val link: String = if (linkEnd > -1) {
                    sharedText.substring(linkStart, linkEnd);
                } else {
                    sharedText.substring(linkStart);
                }
                val beforeParams = link.substringBefore('?')
                val params = link.substringBefore('#').substringAfter('?')
                val afterParams = link.substringAfter('#', missingDelimiterValue = "")
                val paramMap = urlParamParse(params)
                for ((domain, sidMarker) in domainToSidMarker) {
                    if (link.indexOf(domain) > -1) {
                        paramMap.remove(sidMarker)
                        var newLink = beforeParams
                        if (paramMap.isNotEmpty()) {
                            newLink += "?"
                            for ((param, value) in paramMap) {
                                newLink += "${param}=${value}&"
                            }
                        }
                        if (afterParams.isNotEmpty()) {
                            newLink += "#${afterParams}"
                        }
                        shareLinkActivity(newLink)
                        return
                    }
                }
                shareLinkActivity(link)
            }
        }
    }
    private fun shareLinkActivity(link: String) {
        val sendIntent = Intent()
            .setAction(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, link)
            .setType("text/plain")
        startActivity(Intent.createChooser(sendIntent, "Share clipped link:"))
    }
    private fun urlParamParse(params: String): MutableMap<String, String> {
        val paramMap = mutableMapOf<String, String>()
        var keyOrValue = false
        var key = ""
        var value = ""
        for (c in params) {
            if (c == '='){
                keyOrValue = true
            } else if (c == '&'){
                paramMap[key] = value
                key = ""
                value = ""
                keyOrValue = false
            } else {
                if (keyOrValue) {
                    value += c
                } else {
                    key += c
                }
            }
        }
        paramMap[key] = value
        return paramMap
    }
}