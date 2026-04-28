package com.example.simplifymypantry.scanner.presentation

import com.example.simplifymypantry.scanner.data.ImageSaver
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.writeToFile

class IosImageSaver : ImageSaver {
    override fun getImageCacheDir(): String {
        val docDir = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory, NSUserDomainMask, true
        ).first() as String
        return "$docDir/product_images"
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override fun saveImage(code: String, name: String, bytes: ByteArray) {
        val dir = getImageCacheDir() + "/$code"
        NSFileManager.defaultManager.createDirectoryAtPath(
            dir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null
        )
        val path = "$dir/$name.jpg"
        val nsData = bytes.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
        }
        nsData.writeToFile(path, atomically = true)
    }

    override fun getImagePath(code: String, name: String): String =
        "${getImageCacheDir()}/$code/$name.jpg"
}