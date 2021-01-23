package com.slc.needle.models

import java.io.Serializable

data class Group(
    val name: String = "",
    val owner: String = ""
): Serializable
