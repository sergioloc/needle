package com.slc.amarn.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SettingsViewModel: ViewModel() {

    fun signOut(){
        FirebaseAuth.getInstance().signOut();
    }

}