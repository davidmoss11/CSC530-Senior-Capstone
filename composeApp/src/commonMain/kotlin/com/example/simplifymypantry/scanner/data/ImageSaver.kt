package com.example.simplifymypantry.scanner.data


interface ImageSaver {
    fun getImageCacheDir(): String
    fun saveImage(code: String, name: String, bytes: ByteArray)
    fun getImagePath(code: String, name: String): String
}