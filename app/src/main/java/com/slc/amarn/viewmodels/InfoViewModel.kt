package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class InfoViewModel: ViewModel() {

    private val calendar = Calendar.getInstance()

    private val _state: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val state: LiveData<Result<Boolean>> get() = _state

    init {
        //check info
    }

    fun getCurrentDate(): Date{
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        return Date(year, month, day)
    }

    fun saveData(name: String, day: Int, month: Int, year: Int){
        if (name.isNullOrEmpty() || day == 0 || month == 0 || year == 0){
            _state.postValue(Result.failure(Throwable("Complete all fields")))
        }
        else if (getAge(year, month, day) < 18){
            _state.postValue(Result.failure(Throwable("You must be 18 years old to access")))
        }
        else {
            _state.postValue(Result.success(true))
        }
    }

    private fun getAge(year: Int, month: Int, day: Int): Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age
    }
}