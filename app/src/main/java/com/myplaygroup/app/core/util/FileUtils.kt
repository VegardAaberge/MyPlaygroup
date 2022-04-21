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
        private const val JPG_EXT = ".jpg"

        fun saveProfileFile(bytes: ByteArray, username: String): File {
            val userDirectory = "$outputDirectory/$USER_DIR"
            verifyThatDirectoryExist(userDirectory)

            val profilePath = username + JPG_EXT
            val newFile = File(userDirectory, profilePath)
            WriteBytes(newFile, bytes)

            return newFile
        }

        fun getProfileFile(username: String) : File {
            val userDirectory = "$outputDirectory/$USER_DIR"
            verifyThatDirectoryExist(userDirectory)
            
            return File(userDirectory, username + JPG_EXT)
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