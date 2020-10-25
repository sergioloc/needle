package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel: ViewModel() {

    private val _state: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val state: LiveData<Result<Boolean>>
        get() = _state

    fun createUser(email: String, password: String, confirm: String){
        if (email.isNullOrEmpty() || password.isNullOrEmpty() || confirm.isNullOrEmpty()){
            _state.postValue(Result.failure(Throwable("Complete all fields")))
        }
        else if (password != confirm){
            _state.postValue(Result.failure(Throwable("Passwords don't match")))
        }
        else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
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