package com.slc.needle.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.needle.models.User
import java.util.*

class SignUpViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val calendar = Calendar.getInstance()
    private val _state: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val state: LiveData<Result<Boolean>> get() = _state

    fun createUser(email: String, password: String, confirm: String, name: String, dateOfBirth: String){
        if (email.isNullOrEmpty() || password.isNullOrEmpty() || confirm.isNullOrEmpty() || name.isNullOrEmpty() || dateOfBirth.isNullOrEmpty()){
            _state.postValue(Result.failure(Throwable("Complete all fields")))
        }
        else if (password != confirm){
            _state.postValue(Result.failure(Throwable("Passwords don't match")))
        }
        else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    _state.postValue(Result.success(true))
                    //Firestore
                    var user = User(name,dateOfBirth,"",0,0,"","","","", true)
                    FirebaseAuth.getInstance().currentUser?.email?.let { it ->
                        db.collection("users").document(it).set(user).addOnCompleteListener {
                            _state.postValue(Result.success(true))
                        }
                    }
                }
                else {
                    _state.postValue(Result.failure(Throwable(it.exception)))
                }
            }
        }
    }

    fun getCurrentDate(): Date {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        return Date(year, month, day)
    }

}