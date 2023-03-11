package com.dibanand.digiopdf.data

import java.io.Serializable

data class SigningParty(
    val name: String,
    val identifier: String,
    val status: String,
    val type: String,
    val signatureType: String,
    val reason: String
) : Serializable
