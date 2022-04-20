package com.myplaygroup.app.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import com.bumptech.glide.load.resource.bitmap.TransformationUtils

fun Context.getImageFromGallery(uri: Uri): Bitmap {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
        val exifInterface = fileDescriptor?.let { ExifInterface(fileDescriptor) }

        return when (exifInterface?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(bitmap!!, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(bitmap!!, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(bitmap!!, 270)
            ExifInterface.ORIENTATION_NORMAL -> bitmap!!
            else -> bitmap!!
        }
    }finally {

    }
}