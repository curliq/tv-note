package com.free.tvtracker.expect

import io.ktor.utils.io.core.toByteArray
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.readValue
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

//@OptIn(ExperimentalForeignApi::class)
//actual fun exportData(csvContent: String) {
//    val tempDir = NSTemporaryDirectory()
//    val filePath = tempDir + "tvtracker-data-export.csv"
//    val fileUrl = NSURL.fileURLWithPath(filePath)
//
//    NSFileManager.defaultManager.createFileAtPath(filePath, csvContent.encodeToByteArray().toNSData(), null)
////    dispatch_async(dispatch_get_main_queue()) {
////        val documentController = UIDocumentInteractionController().apply {
////            URL = fileUrl
////        }
////
////        documentController.presentOptionsMenuFromRect(
////            CGRectZero.readValue(),
////            UIApplication.sharedApplication.keyWindow!!.rootViewController!!.view,
////            true
////        )
////    }
//    dispatch_async(dispatch_get_main_queue()) {
//        val activityViewController = UIActivityViewController(
//            activityItems = listOf(fileUrl),
//            applicationActivities = null
//        )
//        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
//        rootViewController?.presentViewController(activityViewController, animated = true, completion = null)
//    }
//}
//
//@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
//private fun ByteArray.toNSData(): NSData {
//    return this.usePinned { pinned ->
//        NSData.create(bytes = pinned.addressOf(0), length = this.size.convert())
//    }
//}
