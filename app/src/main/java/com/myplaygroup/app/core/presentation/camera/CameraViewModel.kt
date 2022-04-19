package com.myplaygroup.app.core.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
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
import com.myplaygroup.app.core.util.Constants.DEBUG_KEY
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
                state = state.copy(photoUri = event.uri)
            }
            is CameraScreenEvent.AcceptPhoto -> {
                val bitmapOrg = BitmapFactory.decodeFile(File(state.photoUri!!.getPath()).getAbsolutePath())

                val bitmap = if(event.imageSize.height > event.imageSize.width && bitmapOrg.width > bitmapOrg.height){
                    convertBitmap(bitmapOrg, true)
                }else{
                    convertBitmap(bitmapOrg, false)
                }

                val croppedBitmap = cropBitmap(
                    bitmap = bitmap,
                    canvasSize = event.imageSize,
                    cutRect = event.cutRect
                )

                val newUri = convertBitmapToUri(croppedBitmap)

                state = state.copy(photoUri = newUri)
            }
            is CameraScreenEvent.RejectPhoto -> {
                state = state.copy(photoUri = null)
            }
        }
    }

    fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension
        )

    fun convertBitmap(bitmap: Bitmap, shouldRotate: Boolean) : Bitmap {

        // Rotate the bitmap if it has different rotation than the canvas
        val matrix = Matrix()
        if(shouldRotate){
            matrix.postRotate(90f)
        }

        // create the scaled bitmap that will be passed to the converted bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)

        return Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )
    }

    fun cropBitmap(bitmap: Bitmap, canvasSize: Size, cutRect: Rect) : Bitmap {

        // Scale the cut rectange to the bitmap
        val scalingFactor = max(bitmap.height, bitmap.width) / canvasSize.maxDimension
        val cutBitmapSize = cutRect.maxDimension * scalingFactor
        val cutCenterX = cutRect.center.x * scalingFactor
        val cutCenterY = cutRect.center.y * scalingFactor

        // Calculate the start point
        val widthDiff = (bitmap.width - canvasSize.width * scalingFactor) / 2
        val startX = (widthDiff + cutCenterX - cutBitmapSize / 2).toInt()
        val startY = (cutCenterY - cutBitmapSize / 2).toInt()

        return Bitmap.createBitmap(
            bitmap,
            startX,
            startY,
            cutBitmapSize.toInt(),
            cutBitmapSize.toInt()
        )
    }

    fun convertBitmapToUri(bitmap: Bitmap) : Uri {

        // Get the bytes from the bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()

        // Create the URI from the bytes
        val outputDirectory = context.getOutputDirectory()
        val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
        photoFile.writeBytes(bytes)
        val newUri = Uri.fromFile(photoFile)

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

}

private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION = ".jpg"