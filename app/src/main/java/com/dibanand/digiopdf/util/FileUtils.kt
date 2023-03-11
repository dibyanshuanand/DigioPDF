package com.dibanand.digiopdf.util

import android.util.Base64
import android.util.Log
import android.widget.Toast
import okhttp3.ResponseBody
import java.io.*

object FileUtils {
    fun getFileNameFromPath(filePath: String): String {
        return filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1)
    }

    fun convertFileToBase64(file: File): String {
//        val inStream = FileInputStream(file)
//        val bos = ByteArrayOutputStream()
//        val b = ByteArray(1024)
//        var bytesRead: Int = inStream.read(b)
//        while(bytesRead != -1) {
//            bos.write(b, 0, bytesRead)
//            bytesRead = inStream.read(b)
//        }
//        val fileByteArray = bos.toByteArray()

        val base64Array = Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)
        Log.e("FileUtils", "Base64 file string: $base64Array")
        return base64Array
    }

    fun saveFile(body: ResponseBody?, filePath: String): String{
        if (body == null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(filePath)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return filePath
        } catch (e:Exception){
            Log.e("saveFile", e.toString())
        } finally {
            input?.close()
        }
        return ""
    }
}