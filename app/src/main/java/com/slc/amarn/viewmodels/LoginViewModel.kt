package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    private val _state: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val state: LiveData<Result<Boolean>>
        get() = _state

    fun signIn(email: String, password: String){
        if (email.isNullOrEmpty() || password.isNullOrEmpty()){
            _state.postValue(Result.failure(Throwable("Complete all fields")))
        }
        else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    _state.postValue(Result.success(true))
                }
                else {
                    _state.postValue(Result.failure(Throwable(it.exception)))
                }
            }
        }
    }
}