package com.du4r.businesscard.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.du4r.businesscard.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class Image {
    companion object {

    fun share(context: Context, card: View) {
        val bitmap = getScreenShotFromView(card)

        bitmap?.let {
            saveMediaStorage(context, bitmap)
        }
    }

    private fun getScreenShotFromView(card: View): Bitmap? {
        var screenShot: Bitmap? = null
        try {
            screenShot = Bitmap.createBitmap(
                card.measuredWidth,
                card.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(screenShot)
            card.draw(canvas)
        } catch (e: Exception) {
            Log.e("error ->", "falha ao capturar imagem: " + e.message)
        }
        return screenShot
    }

    private fun saveMediaStorage(context: Context, bitmap: Bitmap) {

        val fileName = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {

                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                fos = imageUri?.let {
                    shareIntent(context, imageUri)
                    resolver.openOutputStream(it)
                }
            }
        } else {
            // devices < Q
            val imagesdir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File(imagesdir, fileName)
            shareIntent(context, Uri.fromFile(image))
            fos = FileOutputStream(image)
        }
        fos.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(context, "imagem capturada com sucesso", Toast.LENGTH_SHORT).show()
        }

    }

    private fun shareIntent(context: Context, imageUri: Uri) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/jpg"
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.resources.getText(R.string.label_share)
                )
            )
        }
    }
}