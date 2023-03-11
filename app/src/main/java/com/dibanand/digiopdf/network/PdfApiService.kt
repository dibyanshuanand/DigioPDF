package com.dibanand.digiopdf.network

import com.dibanand.digiopdf.data.UploadPdf
import com.dibanand.digiopdf.data.UploadPdfResponse
import com.dibanand.digiopdf.util.Constants.CLIENT_ID
import com.dibanand.digiopdf.util.Constants.CLIENT_SECRET
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface PdfApiService {

    @Headers(
        "authorization: Basic QUlaNjdEVVNOWjhUR1dKVjREWjdESTNUNVoyTE4yVzI6QVNOOUFWS0hVNkhGNDFLVFU3MUczS05YTEcxRVQ3QkM=",
        "content-type: application/json"
    )
    @POST("v2/client/document/uploadpdf")
    suspend fun uploadDocument(
        @Body file: UploadPdf
    ): Response<UploadPdfResponse>

    @Headers(
        "authorization: Basic QUlaNjdEVVNOWjhUR1dKVjREWjdESTNUNVoyTE4yVzI6QVNOOUFWS0hVNkhGNDFLVFU3MUczS05YTEcxRVQ3QkM=",
    )
    @GET("v2/client/document/{documentId}")
    suspend fun getDocumentDetails(
        @Path("documentId") documentId: String
    ): Response<UploadPdfResponse>

    @Headers(
        "authorization: Basic QUlaNjdEVVNOWjhUR1dKVjREWjdESTNUNVoyTE4yVzI6QVNOOUFWS0hVNkhGNDFLVFU3MUczS05YTEcxRVQ3QkM=",
    )
    @GET("v2/client/document/download")
    suspend fun getDocumentPdf(
        @Query("document_id") documentId: String
    ): Response<ResponseBody>
}