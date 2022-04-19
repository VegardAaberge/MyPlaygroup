package com.myplaygroup.app.core.presentation.camera

import android.net.Uri
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

sealed class CameraScreenEvent {
    object RejectPhoto : CameraScreenEvent()
    data class TakePhoto(val uri: Uri, val fromGallery: Boolean) : CameraScreenEvent()
    data class AcceptPhoto(val cutRect: Rect, val imageSize: Size) : CameraScreenEvent()
}