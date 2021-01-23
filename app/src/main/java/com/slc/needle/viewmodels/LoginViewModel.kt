package com.slc.needle.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    private val _state: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val state: LiveData<Result<Boolean>> get() = _state

    private val _googleSignInClient: MutableLiveData<Result<GoogleSignInClient>> = MutableLiveData()
    val googleSignInClient: LiveData<Result<GoogleSignInClient>> get() = _googleSignInClient

    init {
        if (FirebaseAuth.getInstance().currentUser != null)
            _state.postValue(Result.success(true))
    }

    fun signInWithMain(email: String, password: String){
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

    fun getGoogleCredential(id: String, context: Context){
        val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(id)
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(context, googleConfig)
        googleClient.signOut()
        _googleSignInClient.postValue(Result.success(googleClient))
    }

    fun signInWithCredential(credential: AuthCredential){
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                _state.postValue(Result.success(true))
            }
            else {
                _state.postValue(Result.failure(Throwable(it.exception)))
            }
        }
    }
}