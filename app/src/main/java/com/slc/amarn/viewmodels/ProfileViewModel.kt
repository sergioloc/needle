package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.amarn.models.User

class ProfileViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _user: MutableLiveData<Result<User>> = MutableLiveData()
    val user: LiveData<Result<User>> get() = _user

    fun getUserInfo(){
        FirebaseAuth.getInstance().currentUser?.email?.let {
            db.collection("users").document(it).get().addOnSuccessListener { documentSnapshot ->
                val u = documentSnapshot.toObject(User::class.java)
                u?.let { _user.postValue(Result.success(u)) }
            }
        }
    }
}