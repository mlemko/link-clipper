package com.example.linkclipper

import android.util.Log
import org.json.JSONObject
import com.example.linkclipper.ClipType.*

class JSONHelper {
    companion object {
        fun jsonToLinkDict(j: JSONObject) : MutableMap<String, Link> {
            val d = mutableMapOf<String, Link>()
            if (j.has("domain_list")) return jsonToLinkDict(j.getJSONObject("domain_list"))
            for (i in 0..<(j.names()?.length() ?: -1)) {
                val name = j.names()?.getString(i)
                if (name != null) {
                    val link = jsonToLink(j.getJSONObject(name), name)
                    if (link != null) {
                        d[name] = link
                    }
                }
            }
            return d
        }
        fun jsonToLink(json: JSONObject, domain: String) : Link? {
            try {
                val clips = json.getJSONArray("clipActions")
                val clist = mutableListOf<ClipAction>()
                for (i in 0..<clips.length()) {
                    val clip = clips.getJSONObject(i)
                    when (val type = ClipType.getByInt(clip.getInt("type"))) {
                        REMOVE_SI -> {
                            val markers = mutableListOf<String>()
                            for (j in 0..<clip.getJSONArray("markers").length()) {
                                markers.add(clip.getJSONArray("markers").getString(j))
                            }
                            clist.add(ClipAction(type, markers.toList()))
                        }
                        REMOVE_QUERY -> {
                            clist.add(ClipAction(type, null))
                        }
                        null -> {}
                    }
                }
                return Link(domain, clist)
            } catch (e: Exception) {
              //  Log.e("JSON HELPER", "Error when converting to Link!:\n$e")
            }
            return null
        }
    }
}