package com.slc.amarn.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.amarn.utils.Info

class SettingsViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
    }

    fun modifyVisibility(value: Boolean){
        Info.user.visible = value
        FirebaseAuth.getInstance().currentUser?.email?.let { it ->
            db.collection("users").document(it).set(Info.user).addOnFailureListener {
                Info.user.visible = !value
            }
        }
    }

}