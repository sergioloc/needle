package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.amarn.R
import com.slc.amarn.models.User
import java.util.*

class InfoViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
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

    fun saveData(name: String, dateOfBirth: String){
        if (name.isNullOrEmpty() || dateOfBirth.isNullOrEmpty()){
            _state.postValue(Result.failure(Throwable("Complete all fields")))
        }
        else if (getAge(dateOfBirth) < 18){
            _state.postValue(Result.failure(Throwable("You must be 18 years old to access")))
        }
        else {
            val user = User(name,dateOfBirth,"Madrid", 1, 2, "ey", "sergioloc", "Sergio López", "696752807")
            FirebaseAuth.getInstance().currentUser?.email?.let {
                db.collection("users").document(it).set(user).addOnCompleteListener {
                    _state.postValue(Result.success(true))
                }
            }
        }
    }

    private fun getAge(dateOfBirth: String): Int {
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