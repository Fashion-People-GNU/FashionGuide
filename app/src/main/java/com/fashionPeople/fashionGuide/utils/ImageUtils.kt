package com.fashionPeople.fashionGuide.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.fashionPeople.fashionGuide.AccountAssistant
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class ImageUtils {

    private fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun createImagePartFromUri(context: Context, uri: Uri): MultipartBody.Part {
        // 이미지 파일의 저장 위치 설정
        val outputFile = File(context.cacheDir, AccountAssistant.getUID().toString() + ".jpg")

        // 입력 스트림에서 비트맵 생성
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // 비트맵을 압축하여 출력 파일에 저장
        val compressedBitmap = compressBitmap(bitmap, 80) // 품질 80으로 압축
        FileOutputStream(outputFile).use { output ->
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
        }

        // 압축된 이미지 파일을 RequestBody로 변환
        val imageRequestBody = outputFile.asRequestBody("image/*".toMediaTypeOrNull())

        // MultipartBody.Part 생성 및 반환
        return MultipartBody.Part.createFormData("image", outputFile.name, imageRequestBody)
    }
}