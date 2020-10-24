package com.slc.amarn.models

import java.io.Serializable

data class User(
    val id: Int,
    val name: String,
    val age: Int,
    val lbg: String,
    val membership: Int,
    val gender: Int,
    val orientation: Int,
    val description: String,
    val photos: ArrayList<Int>,
    val instagram: String,
    val facebook: String,
    val phone: String
): Serializable