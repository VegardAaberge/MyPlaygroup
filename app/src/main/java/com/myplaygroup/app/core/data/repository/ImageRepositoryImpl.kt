package com.myplaygroup.app.core.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import com.myplaygroup.app.core.data.pref.UserSettings
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.DEBUG_KEY
import com.myplaygroup.app.core.util.FileUtils
import com.myplaygroup.app.core.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class ImageRepositoryImpl @Inject constructor(
    private val playgroupApi: PlaygroupApi,
    private val dataStore: DataStore<UserSettings>,
    @ApplicationContext private val context: Context,
) : ImageRepository {

    override suspend fun storeProfileImage(
        bitmap: Bitmap
    ) : Resource<String> {

        try {
            // Get the bytes from the bitmap
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val bytes = outputStream.toByteArray()

            val username = dataStore.data.map { u -> u.username }.first()
            val profileFile = FileUtils.saveProfileFile(bytes, username)

            val profileUri = Uri.fromFile(profileFile)
            FileUtils.makeUriVisible(context, profileUri)

            val data_part = bytes.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val data_multi_part = MultipartBody.Part.createFormData("image", "description", data_part)

            val response = playgroupApi.uploadProfileImage(data_multi_part);

            return if(response.isSuccessful && response.code() == 200 && response.body() != null){
                Resource.Success(response.body()!!.message)
            }else{
                Resource.Error("Failed to upload profile image")
            }
        }catch (e: Exception){
            Log.e(Constants.DEBUG_KEY, e.stackTraceToString())
            return  Resource.Error("ERROR: " + e.message)
        }
    }

    override suspend fun getProfileImage(): Resource<Uri?> {
        try {
            val username = dataStore.data.map { u -> u.username }.first()
            var profileFile = FileUtils.getProfileFile(username)

            if(!profileFile.exists()){
                val response = playgroupApi.getProfileImage(username);
                val byteStream = response.byteStream()
                val bytes = byteStream.readBytes();

                FileUtils.saveProfileFile(bytes, username)

                profileFile = FileUtils.getProfileFile(username)

                Log.e(DEBUG_KEY, "Successfully downloaded the image")
            }

            return Resource.Success(Uri.fromFile(profileFile))
        }catch (e: Exception){
            Log.e(DEBUG_KEY, e.stackTraceToString())
            return Resource.Error("ERROR: " + e.message)
        }
    }
}