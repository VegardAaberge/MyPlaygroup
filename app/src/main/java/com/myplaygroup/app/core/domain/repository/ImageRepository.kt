package com.myplaygroup.app.core.domain.repository

import android.graphics.Bitmap
import android.net.Uri

interface ImageRepository {
    suspend fun saveProfileImage(bitmap: Bitmap)

    suspend fun getProfileImage(): Uri?
}