package com.free.tvtracker.expect

import android.content.Intent
import com.free.tvtracker.AndroidApplication
import com.free.tvtracker.activities.splash.SplashActivity
import com.free.tvtracker.expect.data.DatabaseNameAndroid

actual fun logout() {
    AndroidApplication.context.deleteDatabase(DatabaseNameAndroid)
    val intent = Intent(AndroidApplication.context, SplashActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    AndroidApplication.context.startActivity(intent)
    Runtime.getRuntime().exit(0)
}
