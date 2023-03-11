package com.dibanand.digiopdf.repository

import com.dibanand.digiopdf.data.UploadPdf
import com.dibanand.digiopdf.data.UploadPdfResponse
import com.dibanand.digiopdf.network.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class PdfRepository {

    suspend fun uploadPdf(pdfBody: UploadPdf): Response<UploadPdfResponse> {
        return RetrofitInstance.pdfService.uploadDocument(pdfBody)
    }

    suspend fun getDocDetails(docId: String): Response<UploadPdfResponse> {
        return RetrofitInstance.pdfService.getDocumentDetails(docId)
    }

    suspend fun getDocPdf(docId: String): Response<ResponseBody> {
        return RetrofitInstance.pdfService.getDocumentPdf(docId)
    }
}