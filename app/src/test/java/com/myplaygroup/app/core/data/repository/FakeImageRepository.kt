package com.myplaygroup.app.core.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.util.Resource
import org.mockito.Mockito.mock

class FakeImageRepository : ImageRepository {

    val profileBitmaps: HashMap<String, Uri?> = HashMap()

    override suspend fun storeProfileImage(username: String, bitmap: Bitmap): Resource<Unit> {
        profileBitmaps[username] = mock(Uri::class.java)
        return Resource.Success()
    }

    override suspend fun getProfileImage(user: String, fetchFromRemote: Boolean): Resource<Uri?> {
        return Resource.Success(profileBitmaps[user])
    }
}