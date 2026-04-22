package com.example.simplifymypantry.scanner.data

import android.content.Context
import java.io.File

class AndroidImageSaver(private val context: Context) : ImageSaver {
    override fun getImageCacheDir(): String =
        context.cacheDir.absolutePath + "/product_images"

    override fun saveImage(code: String, name: String, bytes: ByteArray) {
        val dir = File(getImageCacheDir(), code).also { it.mkdirs() }
        File(dir, "$name.jpg").writeBytes(bytes)
    }

    override fun getImagePath(code: String, name: String): String =
        "${getImageCacheDir()}/$code/$name.jpg"
}