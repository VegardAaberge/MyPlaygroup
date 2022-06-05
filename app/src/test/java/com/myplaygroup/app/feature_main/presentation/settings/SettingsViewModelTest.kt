package com.myplaygroup.app.feature_main.presentation.settings

import com.google.common.truth.Truth
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.repository.FakeImageRepository
import com.myplaygroup.app.core.data.settings.FakeUserSettingsManager
import com.myplaygroup.app.core.data.settings.UserSettings
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.utility.MainCoroutineRule
import com.myplaygroup.app.destinations.EditProfileScreenDestination
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScreenDestination
import com.myplaygroup.app.destinations.ProfileSelectorScreenDestination
import com.myplaygroup.app.feature_main.data.repository.FakeMainRepository
import com.myplaygroup.app.feature_main.presentation.user.MainViewModel
import com.myplaygroup.app.feature_main.presentation.user.settings.SettingsScreenEvent
import com.myplaygroup.app.feature_main.presentation.user.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: SettingsViewModel
    lateinit var mainViewModel: MainViewModel
    lateinit var basicAuthInterceptor: BasicAuthInterceptor
    lateinit var imageRepository: FakeImageRepository
    lateinit var mainRepository: FakeMainRepository
    lateinit var userSettingsManager: FakeUserSettingsManager

    @Before
    fun setUp() {
        basicAuthInterceptor = BasicAuthInterceptor()
        imageRepository = FakeImageRepository()
        userSettingsManager = FakeUserSettingsManager()
        mainRepository = FakeMainRepository()

        viewModel = SettingsViewModel(
            basicAuthInterceptor,
            imageRepository,
            mainRepository,
            userSettingsManager
        )

        viewModel.mainViewModel = MainViewModel(
            userSettingsManager,
            imageRepository
        )
    }

    @Test
    fun `Tap logout, data is reset`(){

        basicAuthInterceptor.accessToken = "accessToken"
        userSettingsManager.userSettings = UserSettings(
            refreshToken = "refreshToken",
            username = "vegard"
        )

        viewModel.onEvent(SettingsScreenEvent.LogoutButtonTapped)

        Truth.assertThat(basicAuthInterceptor.accessToken).isNull()
        Truth.assertThat(userSettingsManager.userSettings).isEqualTo(UserSettings())
        Truth.assertThat(mainRepository.messages).isEmpty()
    }

    @Test
    fun `Tap logout, page is popped`() = runBlocking {

        viewModel.onEvent(SettingsScreenEvent.LogoutButtonTapped)

        val uiEvent = viewModel.mainViewModel.eventChannelFlow.first()

        Truth.assertThat(uiEvent).isEqualTo(BaseViewModel.UiEvent.PopAndNavigateTo(
            popRoute = MainScreenDestination.route,
            destination = LoginScreenDestination
        ))
    }

    @Test
    fun `Tap edit profile, navigate to edit profile`() = runBlocking {

        viewModel.onEvent(SettingsScreenEvent.EditProfileTapped)

        val uiEvent = viewModel.mainViewModel.eventChannelFlow.first()

        Truth.assertThat(uiEvent).isEqualTo(BaseViewModel.UiEvent.NavigateTo(
            EditProfileScreenDestination
        ))
    }

    @Test
    fun `Tap profile picture, navigate to take picture`() = runBlocking {

        viewModel.onEvent(SettingsScreenEvent.EditProfilePictureTapped)

        val uiEvent = viewModel.mainViewModel.eventChannelFlow.first()

        Truth.assertThat(uiEvent).isEqualTo(BaseViewModel.UiEvent.NavigateTo(
            ProfileSelectorScreenDestination
        ))
    }
}