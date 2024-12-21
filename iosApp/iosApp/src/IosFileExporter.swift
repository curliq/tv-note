import Foundation
import UIKit
import ComposeApp

///
/// This is all AI copy paste
///
class IosFileExporter: FileExporter {

    func export(content: String, fileName: String) {
        if let fileURL = createTextFile(with: content, fileName: fileName) {
            shareTextFile(fileURL: fileURL)
        } else {
            print("error export file")
        }
    }

    func createTextFile(with content: String, fileName: String) -> URL? {
        // Get the document directory URL
        guard let documentDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
            print("Could not find document directory")
            return nil
        }

        // Create the file URL
        let fileURL = documentDirectory.appendingPathComponent(fileName)

        do {
            // Write the content to the file
            try content.write(to: fileURL, atomically: true, encoding: .utf8)
            return fileURL
        } catch {
            print("Error creating file: \(error)")
            return nil
        }
    }

    func shareTextFile(fileURL: URL) {
        let activityViewController = UIActivityViewController(activityItems: [fileURL], applicationActivities: nil)

        // Present the activity view controller
        DispatchQueue.main.async {
            // Find the topmost view controller
            guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
                  let rootViewController = windowScene.windows.first?.rootViewController else {
                print("Could not find root view controller")
                return
            }

            var topViewController = rootViewController
            while let presentedViewController = topViewController.presentedViewController {
                topViewController = presentedViewController
            }

            topViewController.present(activityViewController, animated: true, completion: nil)
        }
    }
}
