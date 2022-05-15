package com.myplaygroup.app.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import com.myplaygroup.app.core.util.FileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences,
) : ImageRepository {

    override suspend fun storeProfileImage(bitmap: Bitmap) {

        // Get the bytes from the bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()

        val username = sharedPreferences.getString(Constants.KEY_USERNAME, NO_VALUE) ?: NO_VALUE
        val profileFile = FileUtils.saveProfileFile(bytes, username)

        val profileUri = Uri.fromFile(profileFile)
        FileUtils.makeUriVisible(context, profileUri)
    }

    override suspend fun getProfileImage(): Uri? {
        val username = sharedPreferences.getString(Constants.KEY_USERNAME, NO_VALUE) ?: NO_VALUE
        val profileFile = FileUtils.getProfileFile(username)
        if(!profileFile.exists())
            return null

        return Uri.fromFile(profileFile)
    }
}