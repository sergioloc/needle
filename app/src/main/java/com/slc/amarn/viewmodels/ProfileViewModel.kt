package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.slc.amarn.models.User
import com.slc.amarn.utils.Info

class ProfileViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val MAX_PHOTOS = 3

    private val _user: MutableLiveData<Result<User>> = MutableLiveData()
    val user: LiveData<Result<User>> get() = _user

    private val _drawables: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val drawables: LiveData<Result<Boolean>> get() = _drawables

    fun getUserInfo(){
        FirebaseAuth.getInstance().currentUser?.email?.let {
            db.collection("users").document(it).get().addOnSuccessListener { documentSnapshot ->
                val u = documentSnapshot.toObject(User::class.java)
                u?.let { _user.postValue(Result.success(u)) }
            }
        }
    }

    fun getMyPhotosURL(){
        val storage = FirebaseStorage.getInstance().getReference("users/${FirebaseAuth.getInstance().currentUser?.email}")
        storage.list(MAX_PHOTOS).addOnSuccessListener {
            Info.photos = ArrayList()
            if (it.items.size == 0)
                _drawables.postValue(Result.success(false))
            else {
                for (i in it.items.size-1 downTo 0)
                    it.items[i].downloadUrl.addOnSuccessListener {uri ->
                        Info.photos.add(uri.toString())
                        _drawables.postValue(Result.success(true))
                    }
            }
        }
    }
}