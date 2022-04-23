package com.myplaygroup.app.feature_main.presentation.settings

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.KEY_EMAIL
import com.myplaygroup.app.core.util.Constants.KEY_PASSWORD
import com.myplaygroup.app.core.util.Constants.KEY_PHONE_NUMBER
import com.myplaygroup.app.core.util.Constants.KEY_PROFILE_NAME
import com.myplaygroup.app.core.util.Constants.KEY_USERNAME
import com.myplaygroup.app.core.util.Constants.NO_PASSWORD
import com.myplaygroup.app.core.util.Constants.NO_USERNAME
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScreenDestination
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import com.myplaygroup.app.feature_main.presentation.home.HomeScreenEvent
import com.myplaygroup.app.feature_main.presentation.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    lateinit var mainViewModel: MainViewModel
    lateinit var username: String
    lateinit var profileName: String

    var state by mutableStateOf(HomeState())

    init {
        username = sharedPref.getString(KEY_USERNAME, NO_USERNAME) ?: NO_USERNAME
        profileName = sharedPref.getString(KEY_PROFILE_NAME, "") ?: ""

        viewModelScope.launch {
            val uri = imageRepository.getProfileImage()
            state = state.copy(
                imageUri = uri
            )
        }
    }

    fun onEvent(event: HomeScreenEvent){
        when(event){
            is HomeScreenEvent.LogoutButtonTapped -> {
                logout()
            }
        }
    }

    fun logout(){
        basicAuthInterceptor.username = null
        basicAuthInterceptor.password = null
        sharedPref.edit {
            putString(KEY_USERNAME, NO_USERNAME)
            putString(KEY_PASSWORD, NO_PASSWORD)
            putString(KEY_EMAIL, "")
            putString(KEY_PHONE_NUMBER, "")
            putString(KEY_PROFILE_NAME, "")
        }
        mainViewModel.setUIEvent(
            BaseViewModel.UiEvent.PopAndNavigateTo(
                popRoute = MainScreenDestination.route,
                destination = LoginScreenDestination
            )
        )
    }
}