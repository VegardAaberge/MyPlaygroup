package com.myplaygroup.app.core.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.core.net.toFile
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.presentation.camera.components.getOutputDirectory
import com.myplaygroup.app.core.util.getImageFromGallery
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.max


@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val context : Context
) : BaseViewModel() {

    var state by mutableStateOf(CameraScreenState())

    fun onEvent(event: CameraScreenEvent){
        when(event){
            is CameraScreenEvent.TakePhoto -> {

                val rotatedBitmap = context.getImageFromGallery(event.uri)

                state = state.copy(photoBitmap = rotatedBitmap)
            }
            is CameraScreenEvent.AcceptPhoto -> {

                val croppedBitmap = cropBitmap(
                    bitmap = state.photoBitmap!!,
                    canvasSize = event.imageSize,
                    cutRect = event.cutRect
                )

                state = state.copy(photoBitmap = croppedBitmap)
            }
            is CameraScreenEvent.RejectPhoto -> {
                state = state.copy(photoBitmap = null)
            }
        }
    }

    fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension
        )

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

    fun convertBitmapToUri(bitmap: Bitmap, oldPath: String) : Uri {

        // Get the bytes from the bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()

        // Create the URI from the bytes
        val outputDirectory = context.getOutputDirectory()
        val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
        photoFile.writeBytes(bytes)
        val newUri = Uri.fromFile(photoFile)

        // Move the Exif to the new file
        val newPath = File(newUri.getPath()).getAbsolutePath()
        copyExif(oldPath, newPath)

        // Make sure the file is visible to the system
        val mimeType = MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(newUri.toFile().extension)
        MediaScannerConnection.scanFile(
            context,
            arrayOf(newUri.toFile().absolutePath),
            arrayOf(mimeType)
        ) { _, uri ->

        }

        return newUri
    }

    fun copyExif(oldPath: String, newPath: String) {
        val oldExif = ExifInterface(oldPath)
        val attributes = arrayOf(
            ExifInterface.TAG_DATETIME,
            ExifInterface.TAG_DATETIME_DIGITIZED,
            ExifInterface.TAG_EXPOSURE_TIME,
            ExifInterface.TAG_FLASH,
            ExifInterface.TAG_FOCAL_LENGTH,
            ExifInterface.TAG_GPS_ALTITUDE,
            ExifInterface.TAG_GPS_ALTITUDE_REF,
            ExifInterface.TAG_GPS_DATESTAMP,
            ExifInterface.TAG_GPS_LATITUDE,
            ExifInterface.TAG_GPS_LATITUDE_REF,
            ExifInterface.TAG_GPS_LONGITUDE,
            ExifInterface.TAG_GPS_LONGITUDE_REF,
            ExifInterface.TAG_GPS_PROCESSING_METHOD,
            ExifInterface.TAG_GPS_TIMESTAMP,
            ExifInterface.TAG_MAKE,
            ExifInterface.TAG_MODEL,
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.TAG_SUBSEC_TIME,
            ExifInterface.TAG_WHITE_BALANCE
        )
        val newExif = ExifInterface(newPath)
        for (i in attributes.indices) {
            val value = oldExif.getAttribute(attributes[i])
            if (value != null) newExif.setAttribute(attributes[i], value)
        }
        newExif.saveAttributes()
    }
}

private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION = ".jpg"