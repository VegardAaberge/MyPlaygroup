package com.myplaygroup.app.core.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.destinations.DirectionDestination
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _isBusy = mutableStateOf(false)
    val isBusy: State<Boolean> = _isBusy

    fun setUIEvent(event: BaseViewModel.UiEvent){
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun isBusy(value: Boolean){
        _isBusy.value = value
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object PopPage : UiEvent()
        data class NavigateTo(val destination: DirectionDestination) : UiEvent()
        data class PopAndNavigateTo(val popRoute: String, val destination: DirectionDestination) : UiEvent()
    }
}
