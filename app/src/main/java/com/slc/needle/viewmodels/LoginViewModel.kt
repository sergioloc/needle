package com.slc.needle.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.needle.models.User

class LoginViewModel: ViewModel() {

    private val _state: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val state: LiveData<Result<Boolean>> get() = _state

    private val _googleSignInClient: MutableLiveData<Result<GoogleSignInClient>> = MutableLiveData()
    val googleSignInClient: LiveData<Result<GoogleSignInClient>> get() = _googleSignInClient

    private val db = FirebaseFirestore.getInstance()
    private var user: FirebaseUser? = null

    init {
        user = FirebaseAuth.getInstance().currentUser
        user?.let {
            if (it.isEmailVerified)
                _state.postValue(Result.success(true))
        }
    }

    fun signInWithMain(email: String, password: String){
        if (email.isEmpty() || password.isEmpty()){
            _state.postValue(Result.failure(Throwable("Complete all fields")))
        }
        else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    user?.let {u ->
                        if (u.isEmailVerified)
                            _state.postValue(Result.success(true))
                        else
                            _state.postValue(Result.failure(Throwable("Email is not verify")))
                    }
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

    fun signInWithCredential(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                user = FirebaseAuth.getInstance().currentUser
                saveInFirestore(User(account.givenName!!,"", "",0,0,"","","","",false))
            }
            else {
                _state.postValue(Result.failure(Throwable(it.exception)))
            }
        }
    }

    private fun saveInFirestore(u: User){
        user?.email?.let { it ->
            db.collection("users").document(it).set(u).addOnCompleteListener {
                _state.postValue(Result.success(true))
            }
        }!!
    }
}