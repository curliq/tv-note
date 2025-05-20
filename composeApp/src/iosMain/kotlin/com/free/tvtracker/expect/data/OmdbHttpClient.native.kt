package com.free.tvtracker.expect.data

import platform.Foundation.NSBundle
import platform.Foundation.NSDictionary
import platform.Foundation.dictionaryWithContentsOfFile

actual fun getOmdbToken(): String {
    val secrets = NSBundle.mainBundle.pathForResource("Secrets", "plist") ?:""
    return NSDictionary.dictionaryWithContentsOfFile(secrets)?.get("OMDB_TOKEN") as? String ?: ""
}
