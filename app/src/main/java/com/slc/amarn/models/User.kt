package com.slc.amarn.models

import java.io.Serializable

data class User(
    val name: String = "",
    val dateOfBirth: String = "",
    val city: String = "",
    val gender: Int = 0,
    val orientation: Int = 0,
    val description: String = "",
    val instagram: String = "",
    val facebook: String = "",
    val phone: String = ""
): Serializable