package com.slc.amarn.models

import java.io.Serializable

data class Match(
    val name: String = "",
    val dateOfBirth: String = "",
    val city: String = "",
    var description: String = "",
    var instagram: String = "",
    var facebook: String = "",
    var phone: String = "",
    var group: String = "",
    var date: String = "",
    var images: ArrayList<String> = ArrayList()
): Serializable