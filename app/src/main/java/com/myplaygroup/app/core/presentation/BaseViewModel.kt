package com.myplaygroup.app.core.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.destinations.DirectionDestination
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val _eventChannel = Channel<UiEvent>()
    val eventChannelFlow = _eventChannel.receiveAsFlow()

    private val _isBusy = mutableStateOf(false)
    val isBusy: State<Boolean> = _isBusy

    fun setUIEvent(event: BaseViewModel.UiEvent){
        viewModelScope.launch {
            _eventChannel.send(event)
        }
    }

    fun isBusy(value: Boolean){
        _isBusy.value = value
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object PopPage : UiEvent()
        data class NavigateTo(val destination: Direction) : UiEvent()
        data class PopAndNavigateTo(val popRoute: String, val destination: DirectionDestination) : UiEvent()
    }
}
