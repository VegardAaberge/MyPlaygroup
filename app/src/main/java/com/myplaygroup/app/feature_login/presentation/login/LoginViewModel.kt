package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.feature_login.domain.model.User
import com.myplaygroup.app.feature_login.domain.use_case.LoginUseCases
import com.myplaygroup.app.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private val _user = mutableStateOf<String>("")
    val user: State<String> = _user

    private val _password = mutableStateOf<String>("")
    val password: State<String> = _password

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var userResponse: MutableLiveData<Resource<User?>> = MutableLiveData()

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EnteredUsername -> {
                _user.value = event.user
            }
            is LoginEvent.EnteredPassword -> {
                _password.value = event.password
            }
            is LoginEvent.LoginTapped -> {
                viewModelScope.launch {
                    userResponse.postValue(Resource.Loading())
                    val response = loginUseCases.authenticate(_user.value, _password.value)

                    when(response){
                        is Resource.Success -> {
                            userResponse.postValue(Resource.Success(response.data!!))
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar("Success")
                            )
                        }
                        is Resource.Error -> {
                            userResponse.postValue(Resource.Error("Failed to load"))
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar("Fail")
                            )
                        }
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}