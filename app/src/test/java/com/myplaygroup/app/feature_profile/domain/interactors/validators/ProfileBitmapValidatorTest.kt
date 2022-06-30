package com.myplaygroup.app.feature_profile.domain.interactors.validators

import android.graphics.Bitmap
import com.google.common.truth.Truth
import com.myplaygroup.app.core.domain.validation.user.ProfileBitmapValidator

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class ProfileBitmapValidatorTest {

    lateinit var profileBitmapValidator: ProfileBitmapValidator

    @Before
    fun setUp() {
        profileBitmapValidator = ProfileBitmapValidator(context)
    }

    @Test
    fun `bitmap is null, return false`(){
        val result = profileBitmapValidator(null)

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isNotEmpty()
    }

    @Test
    fun `bitmap is not null, return true`(){
        val mockedBitmap = mock(Bitmap::class.java)
        val result = profileBitmapValidator(mockedBitmap)

        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorMessage).isNull()
    }
}