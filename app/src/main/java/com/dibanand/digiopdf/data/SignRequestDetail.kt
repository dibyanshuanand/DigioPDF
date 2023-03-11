package com.dibanand.digiopdf.data

import java.io.Serializable

data class SignRequestDetail(
    val name: String,
    val identifier: String,
    val requestedOn: String,
    val expireOn: String,
    val requesterType: String
) : Serializable
