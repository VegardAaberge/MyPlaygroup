package com.myplaygroup.app.core.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.myplaygroup.app.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun storeProfileImage(username: String, bitmap: Bitmap) : Resource<Unit>

    suspend fun getProfileImage(user: String): Resource<Uri?>
}