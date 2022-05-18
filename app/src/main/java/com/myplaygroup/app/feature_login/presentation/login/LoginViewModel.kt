package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.destinations.CreateProfileScreenDestination
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScreenDestination
import com.myplaygroup.app.feature_login.data.responses.LoginResponse
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
) : BaseViewModel() {

    var state by mutableStateOf(LoginState())

    fun onEvent(event: LoginScreenEvent){
        when(event){
            is LoginScreenEvent.EnteredUsername -> {
                state = state.copy(username = event.username)
            }
            is LoginScreenEvent.EnteredPassword -> {
                state = state.copy(password = event.password)
            }
            is LoginScreenEvent.LoginTapped -> {
                viewModelScope.launch {
                    repository
                        .authenticate(state.username, state.password)
                        .collect { collectAuthenticateResponse(it) }
                }
            }
        }
    }

    private suspend fun collectAuthenticateResponse(result: Resource<LoginResponse>) {
        when(result){
            is Resource.Success -> {

                if(result.data!!.profile_created){
                    setUIEvent(
                        UiEvent.PopAndNavigateTo(
                            popRoute = LoginScreenDestination.route,
                            destination = MainScreenDestination
                        )
                    )
                }else{
                    setUIEvent(
                        UiEvent.NavigateTo(
                            destination = CreateProfileScreenDestination
                        )
                    )
                }
            }
            is Resource.Error -> {
                state = state.copy(password = "")

                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                isBusy(result.isLoading)
            }
        }
    }
}