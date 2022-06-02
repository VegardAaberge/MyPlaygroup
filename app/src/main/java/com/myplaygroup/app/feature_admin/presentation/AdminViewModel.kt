package com.myplaygroup.app.feature_admin.presentation

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val userSettingsManager: UserSettingsManager,
    private val imageRepository: ImageRepository
) : BaseViewModel() {

    val username = userSettingsManager.getFlow {
        it.map { u -> u.username }
    }

    val profileName = userSettingsManager.getFlow {
        it.map { u -> u.profileName }
    }

    var state by mutableStateOf(AdminState())

    init {
        viewModelScope.launch {
            loadProfileImage(username.first()){
                state = state.copy(
                    adminUri = it.data!!
                )
            }
        }
    }

    fun onEvent(event: AdminScreenEvent) {
        when(event){
            is AdminScreenEvent.EditProfileTapped -> {
                setUIEvent(
                    UiEvent.ShowSnackbar("Edit Profile tapped")
                )
            }
            is AdminScreenEvent.EditProfilePictureTapped -> {
                setUIEvent(
                    UiEvent.ShowSnackbar("Profile picture tapped")
                )
            }
            is AdminScreenEvent.logoutTapped -> {
                setUIEvent(
                    UiEvent.ShowSnackbar("Logout tapped")
                )
            }
            is AdminScreenEvent.routeUpdated -> {
                state = state.copy(currentRoute = event.route)
            }
        }
    }

    private fun loadProfileImage(
        user: String,
        setState: (Resource<Uri?>) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {

        val result = imageRepository.getProfileImage(
            user = user
        )

        if (result is Resource.Success) {
            launch(Dispatchers.Main) {
                setState(result)
            }
        } else if (result is Resource.Error) {
            setUIEvent(
                UiEvent.ShowSnackbar(result.message!!)
            )
        }
    }
}