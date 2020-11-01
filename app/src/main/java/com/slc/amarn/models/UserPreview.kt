package com.slc.amarn.models

data class UserPreview(
    val email: String = "",
    val name: String = "",
    val group: String,
    val dateOfBirth: String = "",
    val city: String = "",
    var images: ArrayList<String> = ArrayList()
)