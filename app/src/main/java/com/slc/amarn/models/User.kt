package com.slc.amarn.models

import java.io.Serializable

data class User(
    val name: String = "",
    val dateOfBirth: String = "",
    val city: String = "",
    var gender: Int = 0,
    var orientation: Int = 0,
    var description: String = "",
    var instagram: String = "",
    var facebook: String = "",
    var phone: String = ""
): Serializable