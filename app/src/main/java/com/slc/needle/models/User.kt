package com.slc.needle.models

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
    var phone: String = "",
    var visible: Boolean = true,
    var images: ArrayList<String> = ArrayList(),
    var groups: ArrayList<String> = ArrayList()
): Serializable