package com.free.tvtracker.expect

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.free.tvtracker.AndroidApplication
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

import com.free.tvtracker.ui.settings.FileExporter

class AndroidFileExporter: FileExporter {
    override fun export(content: String, fileName: String) {
        try {
            // Get the Downloads directory
            val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsFolder.exists()) {
                downloadsFolder.mkdirs()
            }

            // Create the CSV file
            val file = File(downloadsFolder, fileName)
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()

            // Open share dialog to give the user the option to save it or upload somewhere
            // Create the Uri for the file
            val fileUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    AndroidApplication.context,
                    "${AndroidApplication.context.packageName}.fileprovider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }

            // Create the share intent
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, fileUri)
                type = "text/plain"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // Show the share dialog
            AndroidApplication.context.startActivity(Intent.createChooser(shareIntent, "File saved to Downloads."))
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(AndroidApplication.context, "Failed to save CSV file", Toast.LENGTH_LONG).show()
        }
    }
}
