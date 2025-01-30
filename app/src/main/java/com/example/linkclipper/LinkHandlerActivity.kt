package com.example.linkclipper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File
import java.io.IOException

class LinkHandlerActivity : AppCompatActivity() {

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
                if (!fileList().contains("link-db.json")) {
                    startActivity(
                        Intent(applicationContext, UpdateDatabaseActivity::class.java).putExtra(Intent.EXTRA_TEXT, sharedText))
                    return
                }
                val jsonDB = readJsonFile()
                if (jsonDB != null) {
                    val linksMap = JSONHelper.jsonToLinkDict(jsonDB)
                    for (link in linksMap.values) {
                        if (link.domainFor(sharedText)) {
                            shareLinkActivity(link.clip(sharedText))
                            return
                        }
                    }
                }
                shareLinkActivity(sharedText)
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

    private fun readJsonFile() : JSONObject? {
        try {
           val fileSReader = File(filesDir, "link-db.json").bufferedReader()
            val fileStr = fileSReader.readText()
            fileSReader.close()
            return JSONObject(fileStr)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}