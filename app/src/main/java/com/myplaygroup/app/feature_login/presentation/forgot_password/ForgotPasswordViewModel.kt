package com.myplaygroup.app.feature_login.presentation.forgot_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.domain.model.User
import com.myplaygroup.app.feature_login.domain.use_case.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : BaseViewModel() {

    var emailResponse: MutableLiveData<Resource<String>> = MutableLiveData()

    private val _email = mutableStateOf<String>("")
    val email: State<String> = _email

    fun onEvent(event: ForgotPasswordEvent)
    {
        when(event){
            is ForgotPasswordEvent.EnteredEmail -> {
                _email.value = event.email
            }
            is ForgotPasswordEvent.ResetPassword -> {
                viewModelScope.launch {
                    emailResponse.postValue(Resource.Loading())
                    val response = loginUseCases.resetPassword(_email.value)

                    if(response is Resource.Success){
                        emailResponse.postValue(Resource.Success("Success"))
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar("Sent, please check your email")
                        )
                    }else {
                        emailResponse.postValue(Resource.Error("Fail"))
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar("Failed to send")
                        )
                    }
                }
            }
        }
    }
}