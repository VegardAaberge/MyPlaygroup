package com.myplaygroup.app.core.presentation.camera

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.BitmapUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val context : Context,
    private val imageRepository : ImageRepository
) : BaseViewModel() {

    var state by mutableStateOf(CameraScreenState())

    fun onEvent(event: CameraScreenEvent){
        when(event){
            is CameraScreenEvent.TakePhoto -> {
                viewModelScope.launch {
                    val rotatedBitmap = BitmapUtils.rotateImageFromExif(event.uri, context)

                    state = state.copy(photoBitmap = rotatedBitmap)
                }
            }
            is CameraScreenEvent.AcceptPhoto -> {
                viewModelScope.launch {
                    val croppedBitmap = BitmapUtils.cropBitmap(
                        bitmap = state.photoBitmap!!,
                        canvasSize = event.imageSize,
                        cutRect = event.cutRect
                    )

                    imageRepository.saveProfileImage(croppedBitmap)

                    _eventFlow.emit(UiEvent.PopPage)
                }
            }
            is CameraScreenEvent.RejectPhoto -> {
                state = state.copy(photoBitmap = null)
            }
        }
    }
}