package com.slc.amarn.models

data class UserPreview(
    val name: String = "",
    val dateOfBirth: String = "",
    val city: String = "",
    var photos: ArrayList<String> = ArrayList()
)