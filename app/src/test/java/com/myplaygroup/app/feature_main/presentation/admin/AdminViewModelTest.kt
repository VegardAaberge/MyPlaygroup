package com.myplaygroup.app.feature_main.presentation.admin

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
import com.myplaygroup.app.feature_main.domain.interactors.FakeMainDaoInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AdminViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: AdminViewModel
    lateinit var basicAuthInterceptor: BasicAuthInterceptor
    lateinit var imageRepository: FakeImageRepository
    lateinit var userSettingsManager: FakeUserSettingsManager
    lateinit var mainDaoInteractor: FakeMainDaoInteractor

    @Before
    fun setUp() {
        basicAuthInterceptor = BasicAuthInterceptor()
        imageRepository = FakeImageRepository()
        userSettingsManager = FakeUserSettingsManager()
        mainDaoInteractor = FakeMainDaoInteractor()

        /*
        viewModel = AdminViewModel(
            userSettingsManager,
            imageRepository,
            basicAuthInterceptor,
            mainDaoInteractor
        )
        */
    }

    @Test
    fun `Tap logout, data is reset`(){

        userSettingsManager.userSettings = UserSettings(
            refreshToken = "refreshToken",
            username = "vegard"
        )

        viewModel.onEvent(AdminScreenEvent.LogoutTapped)

        Truth.assertThat(basicAuthInterceptor.accessToken).isNull()
        Truth.assertThat(userSettingsManager.userSettings).isEqualTo(UserSettings())
        //Truth.assertThat(mainRepository.messages).isEmpty()
    }

    @Test
    fun `Tap logout, page is popped`() = runBlocking {

        basicAuthInterceptor.accessToken = "accessToken"
        viewModel.onEvent(AdminScreenEvent.LogoutTapped)

        val uiEvent = viewModel.eventChannelFlow.first()

        Truth.assertThat(uiEvent).isEqualTo(BaseViewModel.UiEvent.PopAndNavigateTo(
            popRoute = MainScreenDestination.route,
            destination = LoginScreenDestination
        ))
    }

    @Test
    fun `Tap edit profile, navigate to edit profile`() = runBlocking {

        viewModel.onEvent(AdminScreenEvent.EditProfileTapped)

        val uiEvent = viewModel.eventChannelFlow.first()

        Truth.assertThat(uiEvent).isEqualTo(BaseViewModel.UiEvent.NavigateTo(
            EditProfileScreenDestination
        ))
    }

    @Test
    fun `Tap profile picture, navigate to take picture`() = runBlocking {

        viewModel.onEvent(AdminScreenEvent.EditProfilePictureTapped)

        val uiEvent = viewModel.eventChannelFlow.first()

        Truth.assertThat(uiEvent).isEqualTo(BaseViewModel.UiEvent.NavigateTo(
            ProfileSelectorScreenDestination
        ))
    }
}