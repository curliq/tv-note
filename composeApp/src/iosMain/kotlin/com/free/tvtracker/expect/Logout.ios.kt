package com.free.tvtracker.expect

import com.free.tvtracker.expect.data.DatabaseNameIos
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.posix.exit

actual fun logout() {
    deleteDatabaseFile(DatabaseNameIos)
    exit(0)
}

/**
 * Deletes the specified database file from Library/Application Support/databases directory
 * @param databaseName Name of the database file
 * @return true if deletion was successful, false otherwise
 */
@OptIn(ExperimentalForeignApi::class)
private fun deleteDatabaseFile(databaseName: String): Boolean {
    val fileManager = NSFileManager.defaultManager

    // Get the Application Support directory path
    val appSupportUrl = fileManager.URLsForDirectory(
        NSApplicationSupportDirectory,
        NSUserDomainMask
    ).firstOrNull() as? NSURL

    return appSupportUrl?.let { supportUrl ->
        // Construct path to databases folder
        val databasesFolderUrl = supportUrl.URLByAppendingPathComponent("databases")

        // Create databases directory if it doesn't exist
        if (!fileManager.fileExistsAtPath(databasesFolderUrl!!.path!!)) {
            try {
                fileManager.createDirectoryAtURL(
                    databasesFolderUrl,
                    true, // withIntermediateDirectories
                    null, // attributes
                    null  // error
                )
            } catch (e: Throwable) {
                println("Failed to create databases directory: ${e.message}")
                return false
            }
        }

        // Construct the full path to the database file
        val dbPath = databasesFolderUrl.URLByAppendingPathComponent(databaseName)?.path

        dbPath?.let { path ->
            if (fileManager.fileExistsAtPath(path)) {
                try {
                    fileManager.removeItemAtPath(path, null)
                    println("Successfully deleted database at path: $path")
                    true
                } catch (e: Throwable) {
                    println("Failed to delete database: ${e.message}")
                    false
                }
            } else {
                println("Database file does not exist at path: $path")
                false
            }
        } ?: false
    } ?: false
}
