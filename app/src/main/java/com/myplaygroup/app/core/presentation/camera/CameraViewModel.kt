package com.myplaygroup.app.core.presentation.camera

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.myplaygroup.app.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(

) : BaseViewModel() {

    var state by mutableStateOf(CameraScreenState())

    fun onEvent(event: CameraScreenEvent){
        when(event){
            is CameraScreenEvent.TakePhoto -> {
                state = state.copy(photoUri = event.uri)
            }
            is CameraScreenEvent.AcceptPhoto -> {

            }
            is CameraScreenEvent.RejectPhoto -> {
                state = state.copy(photoUri = null)
            }
        }
    }
}