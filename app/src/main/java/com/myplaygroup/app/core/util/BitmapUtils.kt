package com.myplaygroup.app.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import kotlin.math.max

class BitmapUtils{
    companion object
    {
        fun cropBitmap(bitmap: Bitmap, canvasSize: Size, cutRect: Rect) : Bitmap {
            // Scale the cut rectangle to the bitmap
            val scalingFactor = max(bitmap.height, bitmap.width) / canvasSize.maxDimension
            val cutBitmapSize = cutRect.maxDimension * scalingFactor
            val cutCenterX = cutRect.center.x * scalingFactor
            val cutCenterY = cutRect.center.y * scalingFactor

            // Calculate the start point
            val isPortrait = bitmap.height > bitmap.width
            val outsideScreenX = if(isPortrait) (bitmap.width - canvasSize.width * scalingFactor) / 2 else 0f
            val outsideScreenY = if(!isPortrait) (bitmap.height - canvasSize.height * scalingFactor) / 2 else 0f
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
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap!!, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap!!, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap!!, 270)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> rotateImage(bitmap!!, 180, true)
                ExifInterface.ORIENTATION_TRANSPOSE -> rotateImage(bitmap!!, 90, true)
                ExifInterface.ORIENTATION_TRANSVERSE -> rotateImage(bitmap!!, 270, true)
                ExifInterface.ORIENTATION_NORMAL -> bitmap!!
                else -> bitmap!!
            }
        }

        private fun rotateImage(
            bitmap : Bitmap,
            degrees: Int,
            shouldFlip: Boolean = false
        ) : Bitmap {
            var rotatedImage = TransformationUtils.rotateImage(bitmap, degrees)
            val width = rotatedImage.width
            val height = rotatedImage.height

            if(shouldFlip){
                val matrix = Matrix()
                matrix.postScale(-1f, 1f, width.toFloat(), height.toFloat());
                rotatedImage = Bitmap.createBitmap(rotatedImage, 0, 0, width, height, matrix, true);
            }

            return rotatedImage
        }
    }
}


