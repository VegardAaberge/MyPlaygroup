package com.myplaygroup.app.core.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.util.FileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageRepository {

    override suspend fun saveProfileImage(bitmap: Bitmap) {

        // Get the bytes from the bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()

        val profileFile = FileUtils.saveProfileFile(bytes)

        val profileUri = Uri.fromFile(profileFile)
        FileUtils.makeUriVisible(context, profileUri)
    }

    override suspend fun getProfileImage(): Uri? {
        val profileFile = FileUtils.getProfileFile()
        if(!profileFile.exists())
            return null

        return Uri.fromFile(profileFile)
    }
}