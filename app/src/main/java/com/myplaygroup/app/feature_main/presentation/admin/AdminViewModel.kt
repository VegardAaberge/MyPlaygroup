package com.myplaygroup.app.feature_main.presentation.admin

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.destinations.*
import com.myplaygroup.app.feature_main.domain.use_cases.MainDaoUseCases
import com.myplaygroup.app.feature_main.presentation.admin.nav_drawer.NavDrawer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val userSettingsManager: UserSettingsManager,
    private val imageRepository: ImageRepository,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val mainDaoUseCases: MainDaoUseCases
) : BaseViewModel() {

    val username = userSettingsManager.getFlow {
        it.map { u -> u.username }
    }

    val profileName = userSettingsManager.getFlow {
        it.map { u -> u.profileName }
    }

    var state by mutableStateOf(AdminState())

    init {
        updateTitle(NavDrawer.CHAT)

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
                    UiEvent.NavigateTo(EditProfileScreenDestination)
                )
            }
            is AdminScreenEvent.EditProfilePictureTapped -> {
                setUIEvent(
                    UiEvent.NavigateTo(ProfileSelectorScreenDestination)
                )
            }
            is AdminScreenEvent.logoutTapped -> {
                logout()
            }
            is AdminScreenEvent.routeUpdated -> {
                updateTitle(event.route)
            }
            is AdminScreenEvent.NavigateToEditScreen -> {
                setUIEvent(
                    UiEvent.NavigateTo(EditParametersScreenDestination(event.id, event.type))
                )
            }
        }
    }

    private fun logout() = viewModelScope.launch {
        basicAuthInterceptor.accessToken = null
        userSettingsManager.clearData()
        mainDaoUseCases.clearAllTables()

        setUIEvent(
            UiEvent.PopAndNavigateTo(
                popRoute = MainScreenDestination.route,
                destination = LoginScreenDestination
            )
        )
    }

    private fun updateTitle(route: String){
        val currentNavItem = NavDrawer.items[route]
        val newTitle = currentNavItem?.title ?: "Admin Panel"
        state = state.copy(
            title = newTitle
        )
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