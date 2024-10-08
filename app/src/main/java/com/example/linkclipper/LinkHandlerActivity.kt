package com.example.linkclipper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import  okhttp3.Response
import java.io.IOException
import java.util.regex.Pattern


class LinkHandlerActivity : AppCompatActivity() {


    // Contains the related source identifier markers for each domain.
    private val domainToSidMarker: Map<String, String> = mapOf(
        "youtube.com" to "si",
        "youtu.be" to "si",
        "instagram.com" to "igsh",
        "tumblr.com" to "source",
        "spotify.com" to "si"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.type == "text/plain") {
            //handle link
            handleSentText(intent)
        }
        finish()
    }

    private fun handleSentText(thIntent: Intent) {
        val sharedText: String? = thIntent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            val linkStart = sharedText.indexOf("http")
            if (linkStart > -1) {
                val linkEnd = sharedText.lastIndexOf(' ', linkStart)
                val link: String = if (linkEnd > -1) {
                    sharedText.substring(linkStart, linkEnd)
                } else {
                    sharedText.substring(linkStart)
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
            if (c == '=') {
                keyOrValue = true
            } else if (c == '&') {
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

    private fun downloadFileInInternalStorage(link: String, fileName: String) {
        val mFileName = fileName.replace(" ", "_")
            .replace(Pattern.compile("[.][.]+").toRegex(), ".")

        val request = Request.Builder()
            .url(link)
            .build()
        val client = OkHttpClient.Builder()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val fileData = response.body?.byteStream()
                if (fileData != null) {
                    try {
                        applicationContext.openFileOutput(mFileName, Context.MODE_PRIVATE)
                            .use { output ->
                                output.write(fileData.readBytes())
                            }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}