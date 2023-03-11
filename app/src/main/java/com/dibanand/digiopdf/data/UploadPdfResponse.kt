package com.dibanand.digiopdf.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UploadPdfResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("is_agreement")
    val isAgreement: Boolean,
    @SerializedName("agreement_type")
    val agreementType: String,
    @SerializedName("agreement_status")
    val agreementStatus: String,
    @SerializedName("file_name")
    val fileName: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("self_signed")
    val selfSigned: Boolean,
    @SerializedName("self_sign_type")
    val selfSignType: Boolean,
    @SerializedName("no_of_pages")
    val numPages: Int,
    @SerializedName("signing_parties")
    val signingParties: MutableList<SigningParty>,
    @SerializedName("sign_request_details")
    val signRequestDetails: SignRequestDetail,
    @SerializedName("channel")
    val channel: String
)