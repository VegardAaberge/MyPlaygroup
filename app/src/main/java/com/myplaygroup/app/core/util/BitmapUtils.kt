package com.myplaygroup.app.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import kotlin.math.max

class BitmapUtils(){
    companion object
    {
        fun cropBitmap(bitmap: Bitmap, canvasSize: Size, cutRect: Rect) : Bitmap {
            // Scale the cut rectangle to the bitmap
            val scalingFactor = max(bitmap.height, bitmap.width) / canvasSize.maxDimension
            val cutBitmapSize = cutRect.maxDimension * scalingFactor
            val cutCenterX = cutRect.center.x * scalingFactor
            val cutCenterY = cutRect.center.y * scalingFactor

            // Calculate the start point
            val isPotrait = bitmap.height > bitmap.width
            val outsideScreenX = if(isPotrait) (bitmap.width - canvasSize.width * scalingFactor) / 2 else 0f
            val outsideScreenY = if(!isPotrait) (bitmap.height - canvasSize.height * scalingFactor) / 2 else 0f
            val startX = (outsideScreenX + cutCenterX - cutBitmapSize / 2).toInt()
            val startY = (outsideScreenY + cutCenterY - cutBitmapSize / 2).toInt()

            return Bitmap.createBitmap(
                bitmap,
                startX,
                startY,
                cutBitmapSize.toInt(),
                cutBitmapSize.toInt()
            )
        }

        fun rotateImageFromExif(uri: Uri, context: Context): Bitmap {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
            val exifInterface = fileDescriptor?.let { ExifInterface(fileDescriptor) }

            return when (exifInterface?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(bitmap!!, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(bitmap!!, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(bitmap!!, 270)
                ExifInterface.ORIENTATION_NORMAL -> bitmap!!
                else -> bitmap!!
            }
        }
    }
}


