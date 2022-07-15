package com.myplaygroup.app.core.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.DEBUG_KEY
import com.myplaygroup.app.core.util.FileUtils
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class ImageRepositoryImpl @Inject constructor(
    private val playgroupApi: PlaygroupApi,
    @ApplicationContext private val context: Context,
) : ImageRepository {

    override suspend fun storeProfileImage(
        username: String,
        bitmap: Bitmap
    ) : Resource<Unit> {

        try {
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true)

            // Get the bytes from the bitmap
            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            val bytes = outputStream.toByteArray()

            val profileFile = FileUtils.saveProfileFile(bytes, username)

            val profileUri = Uri.fromFile(profileFile)
            FileUtils.makeUriVisible(context, profileUri)

            val data_part = bytes.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val data_multi_part = MultipartBody.Part.createFormData("image", "description", data_part)

            val response = playgroupApi.uploadProfileImage(data_multi_part)

            return if(response.isSuccessful && response.code() == 200 && response.body() != null){
                Resource.Success()
            }else{
                Resource.Error("Failed to upload profile image")
            }
        }catch (e: Exception){
            Log.e(Constants.DEBUG_KEY, e.stackTraceToString())
            return  Resource.Error("ERROR: " + e.message)
        }
    }

    override suspend fun getProfileImage(
        user: String,
        fetchFromRemote: Boolean
    ): Resource<Uri?> {
        try {
            var profileFile = FileUtils.getProfileFile(user)
            val shouldFetch = !profileFile.exists() || fetchFromRemote

            if(shouldFetch && checkForInternetConnection(context)){
                val response = playgroupApi.getProfileImage(user)
                val byteStream = response.byteStream()
                val bytes = byteStream.readBytes()

                FileUtils.saveProfileFile(bytes, user)

                profileFile = FileUtils.getProfileFile(user)

                Log.e(DEBUG_KEY, "Successfully downloaded the image")
            }

            return Resource.Success(Uri.fromFile(profileFile))
        }catch (e: Exception){
            Log.e(DEBUG_KEY, e.stackTraceToString())
            return Resource.Error("ERROR: " + e.message)
        }
    }

    override suspend fun clearProfileImages() {
        try {
            FileUtils.clearProfileImages()
        }catch (e: Exception){
            Log.e(DEBUG_KEY, e.stackTraceToString())
        }
    }
}