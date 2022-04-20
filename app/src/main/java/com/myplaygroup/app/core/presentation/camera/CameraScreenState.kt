package com.myplaygroup.app.core.presentation.camera

import android.graphics.Bitmap
import android.net.Uri

data class CameraScreenState (
    val shouldCrop: Boolean = false,
    val photoBitmap: Bitmap? = null
)