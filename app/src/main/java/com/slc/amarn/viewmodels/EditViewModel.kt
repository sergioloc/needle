package com.slc.amarn.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.amarn.models.User

class EditViewModel(): ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun saveChanges(user: User){
        FirebaseAuth.getInstance().currentUser?.email?.let { it ->
            db.collection("users").document(it).set(user)
        }
    }
}