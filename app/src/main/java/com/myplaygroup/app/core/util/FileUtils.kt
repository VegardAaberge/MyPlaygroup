package com.myplaygroup.app.core.util

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import java.io.File
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*

class FileUtils {
    companion object{
        lateinit var outputDirectory: File

        private const val USER_DIR = "user"
        private const val PROFILE_FILE = "profile.jpg"

        fun saveProfileFile(bytes: ByteArray): File {
            val userDirectory = "$outputDirectory/$USER_DIR"
            verifyThatDirectoryExist(userDirectory)

            val newFile = File(userDirectory, PROFILE_FILE)
            WriteBytes(newFile, bytes)

            return newFile
        }

        fun getProfileFile() : File {
            val userDirectory = "$outputDirectory/$USER_DIR"
            verifyThatDirectoryExist(userDirectory)

            return File(userDirectory, PROFILE_FILE)
        }

        fun makeUriVisible(context: Context, newUri: Uri){
            val mimeType = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(newUri.toFile().extension)
            MediaScannerConnection.scanFile(
                context,
                arrayOf(newUri.toFile().absolutePath),
                arrayOf(mimeType)
            ) { _, uri -> }
        }

        private fun verifyThatDirectoryExist(directory: String) {
            val dir = File(directory)
            if (!dir.exists()){
                dir.mkdirs()
            }
        }

        private fun WriteBytes(newFile: File, bytes: ByteArray) {
            if(newFile.exists()){
                newFile.delete()
                newFile.createNewFile()
            }

            newFile.writeBytes(bytes)
        }
    }
}