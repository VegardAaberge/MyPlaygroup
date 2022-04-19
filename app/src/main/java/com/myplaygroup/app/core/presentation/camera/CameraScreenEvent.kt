package com.myplaygroup.app.core.presentation.camera

import android.net.Uri

sealed class CameraScreenEvent {
    object RejectPhoto : CameraScreenEvent()
    object AcceptPhoto : CameraScreenEvent()
    data class TakePhoto(val uri: Uri, val fromGallery: Boolean) : CameraScreenEvent()
}