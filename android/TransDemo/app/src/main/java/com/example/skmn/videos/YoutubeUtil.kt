package com.example.skmn.videos

import java.util.regex.Pattern

object YoutubeUtil {
    fun getVideoId(url: String?): String {
        var vId = ""
        val pattern = Pattern.compile(
                "^.*(?:(?:youtu\\.be\\/|v\\/|vi\\/|u\\/\\w\\/|embed\\/)|(?:(?:watch)?\\?v(?:i)?=|\\&v(?:i)?=))([^#\\&\\?]*).*",
                Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(url ?: "")
        if (matcher.matches()) {
            vId = matcher.group(1) ?: ""
        }
        return vId
    }
    fun isYoutube(url: String?): Boolean {
        return getVideoId(url).isNotEmpty()
    }
}
