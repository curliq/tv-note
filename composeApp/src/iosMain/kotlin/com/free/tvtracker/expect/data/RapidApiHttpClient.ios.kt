package com.free.tvtracker.expect.data

import platform.Foundation.NSBundle
import platform.Foundation.NSDictionary
import platform.Foundation.dictionaryWithContentsOfFile

actual fun getRapidApiToken(): String {
    val secrets = NSBundle.mainBundle.pathForResource("Secrets", "plist") ?:""
    return NSDictionary.dictionaryWithContentsOfFile(secrets)?.get("RAPID_API_IMDB_TOKEN") as? String ?: ""
}
