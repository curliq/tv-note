package com.free.tvtracker.ui.settings

interface FileExporter {
    fun export(content: String, fileName: String)
}
