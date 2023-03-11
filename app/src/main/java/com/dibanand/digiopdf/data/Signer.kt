package com.dibanand.digiopdf.data

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Signer(
    val identifier: String,
    val name: String,
    val reason: String
) : Serializable
