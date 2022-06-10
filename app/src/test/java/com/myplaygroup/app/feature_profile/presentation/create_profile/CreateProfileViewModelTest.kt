package com.myplaygroup.app.feature_profile.presentation.create_profile

import android.graphics.Bitmap
import com.google.common.truth.Truth
import com.myplaygroup.app.core.data.repository.FakeImageRepository
import com.myplaygroup.app.core.data.settings.FakeUserSettingsManager
import com.myplaygroup.app.core.domain.settings.UserRole
import com.myplaygroup.app.core.utility.MainCoroutineRule
import com.myplaygroup.app.feature_profile.data.repository.FakeProfileRepository
import com.myplaygroup.app.feature_profile.domain.interactors.ProfileValidationInteractors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class CreateProfileViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var profileRepository: FakeProfileRepository
    private lateinit var imageRepository: FakeImageRepository
    private lateinit var userSettingsManager: FakeUserSettingsManager
    private lateinit var profileUseCases: ProfileValidationInteractors
    private lateinit var viewModel: CreateProfileViewModel

    val username = "vegard"
    val profileName = "Vegard"
    val password = "b11111111B"
    val phoneNumber = "12345678901"
    val profileBitmap = mock(Bitmap::class.java)

    @Before
    fun setUp() {
        profileRepository = FakeProfileRepository()
        imageRepository = FakeImageRepository()
        userSettingsManager = FakeUserSettingsManager()
        profileUseCases = ProfileValidationInteractors()

        viewModel = CreateProfileViewModel(
            profileRepository,
            imageRepository,
            userSettingsManager,
            profileUseCases
        )

        viewModel.state = CreateProfileState(
            profileName = profileName,
            password = password,
            repeatedPassword = password,
            phoneNumber = phoneNumber,
            profileBitmap = profileBitmap
        )
    }

    @Test
    fun `Save profile with wrong password, return error`() = runBlocking{

        val username = "vegard"
        val password = "1234"
        userSettingsManager.updateUsernameAndRole(username, UserRole.USER.name)

        viewModel.state = CreateProfileState(
            password = password,
            repeatedPassword = password,
        )

        viewModel.onEvent(CreateProfileScreenEvent.SaveProfile)
        delay(10)

        val appUser = profileRepository.users.first { u -> u.username == username }

        Truth.assertThat(viewModel.state.passwordError).isNotEmpty()
        Truth.assertThat(appUser.password).isNotEqualTo(password)
    }

    @Test
    fun `Save profile with correct password, returns data`() = runBlocking{

        userSettingsManager.updateUsernameAndRole(username, UserRole.USER.name)

        viewModel.onEvent(CreateProfileScreenEvent.SaveProfile)
        delay(10)

        val appUser = profileRepository.users.first { u -> u.username == username }
        val storedUri = imageRepository.profileBitmaps[username]

        Truth.assertThat(appUser.profileName).isEqualTo(profileName)
        Truth.assertThat(appUser.password).isEqualTo(password)
        Truth.assertThat(appUser.phoneNumber).isEqualTo(phoneNumber)
        Truth.assertThat(storedUri).isNotNull()
        Truth.assertThat(viewModel.state.passwordError).isNull()
    }
}