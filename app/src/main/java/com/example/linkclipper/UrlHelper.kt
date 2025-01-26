package com.example.linkclipper

class UrlHelper {
    companion object {
        fun urlParamParse(params: String): MutableMap<String, String> {
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
            if (key.isNotEmpty()){
                paramMap[key] = value
            }
            return paramMap
        }
    }
}