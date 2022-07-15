package com.myplaygroup.app.feature_main.presentation.admin

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.destinations.*
import com.myplaygroup.app.feature_main.domain.interactors.ChatInteractor
import com.myplaygroup.app.feature_main.domain.interactors.MainDaoInteractor
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.model.Payment
import com.myplaygroup.app.feature_main.domain.repository.DailyClassesRepository
import com.myplaygroup.app.feature_main.domain.repository.MonthlyPlansRepository
import com.myplaygroup.app.feature_main.domain.repository.PaymentRepository
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import com.myplaygroup.app.feature_main.presentation.admin.nav_drawer.NavDrawer
import com.myplaygroup.app.feature_profile.domain.model.EditProfileType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val userSettingsManager: UserSettingsManager,
    private val usersRepository: UsersRepository,
    private val monthlyPlansRepository: MonthlyPlansRepository,
    private val dailyClassesRepository: DailyClassesRepository,
    private val paymentRepository: PaymentRepository,
    private val chatInteractor: ChatInteractor,
    private val imageRepository: ImageRepository,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val daoInteractor: MainDaoInteractor
) : BaseViewModel() {

    val username = userSettingsManager.getFlow {
        it.map { u -> u.username }
    }

    val profileName = userSettingsManager.getFlow {
        it.map { u -> u.profileName }
    }

    var initDone = false
    val userFlow = MutableStateFlow(listOf<AppUser>())
    val monthlyPlansFlow = MutableStateFlow(listOf<MonthlyPlan>())
    val paymentFlow = MutableStateFlow(listOf<Payment>())
    val dailyClassesFlow = MutableStateFlow(listOf<DailyClass>())

    var state by mutableStateOf(AdminState())

    fun init() {
        if(initDone)
            return
        initDone = true

        updateTitle(NavDrawer.CHAT)

        loadDataFromServer()
    }

    fun onEvent(event: AdminScreenEvent) {
        when(event){
            is AdminScreenEvent.EditProfileTapped -> {
                setUIEvent(
                    UiEvent.NavigateTo(EditProfileScreenDestination(EditProfileType.NONE.name))
                )
            }
            is AdminScreenEvent.EditProfilePictureTapped -> {
                setUIEvent(
                    UiEvent.NavigateTo(ProfileSelectorScreenDestination)
                )
            }
            is AdminScreenEvent.LogoutTapped -> {
                logout()
            }
            is AdminScreenEvent.routeUpdated -> {
                updateTitle(event.route)
            }
            is AdminScreenEvent.NavigateToEditScreen -> {
                setUIEvent(
                    UiEvent.NavigateTo(EditParametersScreenDestination(event.clientId, event.type.name))
                )
            }
            is AdminScreenEvent.NavigateToCreateMonthlyPlan -> {
                setUIEvent(
                    UiEvent.NavigateTo(CreatePlansScreenDestination)
                )
            }
            is AdminScreenEvent.NavigateToChatScreen -> {
                setUIEvent(
                    UiEvent.NavigateTo(ChatScreenDestination(
                        receivers = arrayOf(event.chatGroup.username),
                        isAdmin = true
                    ))
                )
            }
        }
    }

    fun loadDailyClassesFromServer() = viewModelScope.launch(Dispatchers.IO){
        dailyClassesRepository.getAllDailyClasses(true)
            .collect { collectMergeFlow(it) }
    }

    fun loadDataFromServer() = viewModelScope.launch(Dispatchers.IO){
        val usersResult = usersRepository.getAllUsers(true)
        val monthlyResult = monthlyPlansRepository.getMonthlyPlans(true)
        val dailyClassesResult = dailyClassesRepository.getAllDailyClasses(true)
        val standardPlansResult = monthlyPlansRepository.getStandardPlans(true)
        val paymentsResult = paymentRepository.getAllPayments(true)

        launch {
            loadProfileImage(username.first()){
                state = state.copy(
                    adminUri = it.data!!
                )
            }
        }

        merge(usersResult, monthlyResult, dailyClassesResult, standardPlansResult, paymentsResult)
            .collect { collectMergeFlow(it) }
    }

    private fun collectMergeFlow(result: Resource<out List<Any>>) = viewModelScope.launch(Dispatchers.Main) {
        when (result) {
            is Resource.Success -> {
                onMergeFlowSuccess(result.data!!)
            }
            is Resource.Error -> {
                Log.d(Constants.DEBUG_KEY, result.message!!)
            }
            is Resource.Loading -> {
                isBusy(result.isLoading)
            }
        }
    }

    private suspend fun onMergeFlowSuccess(data: List<Any>) {

        when(data.firstOrNull()){
            is AppUser -> {
                userFlow.emit(
                    data.filterIsInstance<AppUser>()
                )
            }
            is MonthlyPlan -> {
                monthlyPlansFlow.emit(
                    data.filterIsInstance<MonthlyPlan>()
                )
            }
            is DailyClass -> {
                dailyClassesFlow.emit(
                    data.filterIsInstance<DailyClass>()
                )
            }
            is Payment -> {
                paymentFlow.emit(
                    data.filterIsInstance<Payment>()
                )
            }
            else -> { }
        }
    }

    private fun logout() = viewModelScope.launch {
        basicAuthInterceptor.accessToken = null
        userSettingsManager.clearData()
        daoInteractor.clearAllTables()
        imageRepository.clearProfileImages()

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
            Log.d(Constants.DEBUG_KEY, result.message!!)
        }
    }
}