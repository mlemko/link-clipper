package com.example.linkclipper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.Scanner
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class UpdateDatabaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLinksData()

        finish()
    }

    private fun getLinksData() {
        Log.d(null, "Attempting to get database...")
        downloadFileInInternalStorage("https://raw.githubusercontent.com/mlemko/link-clipper/master/link-db.json", "link-db.json")
        Log.d(null, "Request passed. Should be a message above here.")
        Log.d(null, "Current files:\n ${TextUtils.join(", ",fileList())}")
        if (fileList().contains("link-db.json")) {
            val jsonFile = File(filesDir, "link-db.json")
            val fileScanner = Scanner(jsonFile.inputStream())
            var tempString = String()
            while (fileScanner.hasNext()){
                tempString += fileScanner.nextLine()
            }
            fileScanner.close()

            val linkObj = JSONObject(tempString)
            Log.d(null, linkObj.toString())

        } else {
            Toast.makeText(this, "Getting link database failed...", Toast.LENGTH_SHORT).show()
        }
    }

    fun downloadFileInInternalStorage(link: String, fileName: String) {
        val mFileName = fileName.replace(" ", "_")
            .replace(Pattern.compile("[.][.]+").toRegex(), ".")

        val request = Request.Builder()
            .url(link)
            .build()
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS) // Change it as per your requirement
            .readTimeout(5, TimeUnit.SECONDS)// Change it as per your requirement
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()

        Log.d(null, "Client built, requesting...")
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val fileData = response.body?.byteStream()
                Log.d(null, "Download success! Writing to file...")
                if (fileData != null) {
                    try {
                        applicationContext.openFileOutput(mFileName, Context.MODE_PRIVATE)
                            .use { output ->
                                output.write(fileData.readBytes())
                            }
                        Log.d(null, "Writing successful.")
                        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                            sendClipIntent()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.e(null, "Download failed...")
                e.printStackTrace()
            }
        })
    }

    fun sendClipIntent() {
        val clipIntent = Intent(applicationContext, LinkHandlerActivity::class.java)
        clipIntent.type = "text/plain"
        clipIntent.putExtra(Intent.EXTRA_TEXT, intent.getStringExtra(Intent.EXTRA_TEXT))
        startActivity(clipIntent)
    }
}