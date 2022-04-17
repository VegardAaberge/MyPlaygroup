package com.myplaygroup.app.core.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.myplaygroup.app.destinations.DirectionDestination
import com.myplaygroup.app.destinations.MainScrenDestination
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

open class BaseViewModel : ViewModel() {

    protected val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    protected val _isBusy = mutableStateOf(false)
    val isBusy: State<Boolean> = _isBusy

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class NavigateTo(val destination: DirectionDestination) : UiEvent()
        data class PopAndNavigateTo(val popRoute: String, val destination: DirectionDestination) : UiEvent()
    }
}
