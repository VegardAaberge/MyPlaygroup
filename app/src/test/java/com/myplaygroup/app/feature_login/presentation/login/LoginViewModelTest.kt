package com.myplaygroup.app.feature_login.presentation.login

import com.google.common.truth.Truth
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.utility.MainCoroutineRule
import com.myplaygroup.app.destinations.AdminScreenDestination
import com.myplaygroup.app.destinations.CreateProfileScreenDestination
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScreenDestination
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.feature_login.presentation.data.repository.FakeLoginRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: LoginViewModel
    lateinit var loginRepository: LoginRepository

    @Before
    fun setUp() {
        loginRepository = FakeLoginRepository()
        viewModel = LoginViewModel(loginRepository)
    }

    @Test
    fun `wrong username, provides error`() = runBlocking {

        viewModel.state = LoginState(
            username = "vegar",
            password = "b11111111B"
        )

        viewModel.onEvent(LoginScreenEvent.LoginTapped)

        val event = viewModel.eventChannelFlow.first()

        Truth.assertThat(event).isEqualTo(
            BaseViewModel.UiEvent.ShowSnackbar(FakeLoginRepository.INCORRECT_USERNAME_PASSWORD)
        )
    }

    @Test
    fun `wrong password, provides error`() = runBlocking {

        viewModel.state = LoginState(
            username = "vegard",
            password = "b11111112B"
        )

        viewModel.onEvent(LoginScreenEvent.LoginTapped)

        val event = viewModel.eventChannelFlow.first()

        Truth.assertThat(event).isEqualTo(
            BaseViewModel.UiEvent.ShowSnackbar(FakeLoginRepository.INCORRECT_USERNAME_PASSWORD)
        )
    }

    @Test
    fun `correct credentials for admin, navigate to Admin`() = runBlocking {

        viewModel.state = LoginState(
            username = FakeLoginRepository.ADMIN,
            password = FakeLoginRepository.PASSWORD
        )

        viewModel.onEvent(LoginScreenEvent.LoginTapped)

        val event = viewModel.eventChannelFlow.first()

        Truth.assertThat(event).isEqualTo(
            BaseViewModel.UiEvent.PopAndNavigateTo(
                popRoute = LoginScreenDestination.route,
                destination = AdminScreenDestination
            )
        )
    }

    @Test
    fun `correct credentials for user with profile, navigate to Main`() = runBlocking {

        viewModel.state = LoginState(
            username = FakeLoginRepository.HAS_PROFILE,
            password = FakeLoginRepository.PASSWORD
        )

        viewModel.onEvent(LoginScreenEvent.LoginTapped)

        val event = viewModel.eventChannelFlow.first()

        Truth.assertThat(event).isEqualTo(
            BaseViewModel.UiEvent.PopAndNavigateTo(
                popRoute = LoginScreenDestination.route,
                destination = MainScreenDestination
            )
        )
    }

    @Test
    fun `correct credentials for user without profile, navigate to CreateProfile`() = runBlocking {

        viewModel.state = LoginState(
            username = FakeLoginRepository.NO_PROFILE,
            password = FakeLoginRepository.PASSWORD
        )

        viewModel.onEvent(LoginScreenEvent.LoginTapped)

        val event = viewModel.eventChannelFlow.first()

        Truth.assertThat(event).isEqualTo(
            BaseViewModel.UiEvent.NavigateTo(
                destination = CreateProfileScreenDestination
            )
        )
    }
}