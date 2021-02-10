package com.slc.needle.utils

import java.util.*

class Age {
    fun getAge(dateOfBirth: String): Int {
        if (dateOfBirth.isEmpty())
            return 0
        val date = dateOfBirth.split("-").toTypedArray()
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[date[2].toInt(), date[1].toInt()] = date[0].toInt()
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age
    }
}