package com.dibanand.digiopdf.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class UploadPdf(
    @SerializedName("signers")
    val signer: MutableList<Signer>,
    @SerializedName("expire_in_days")
    val expireInDays: Int,
    @SerializedName("display_on_page")
    val displayOnPage: String,
    @SerializedName("file_name")
    val fileName: String,
    @SerializedName("file_data")
    val fileData: String
) : Serializable
