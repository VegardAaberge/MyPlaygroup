package com.myplaygroup.app.core.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.myplaygroup.app.core.util.Resource

interface ImageRepository {
    suspend fun storeProfileImage(bitmap: Bitmap) : Resource<String>

    suspend fun getProfileImage(): Resource<Uri?>
}