package com.slc.amarn.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.slc.amarn.models.User
import com.slc.amarn.utils.Info
import java.io.ByteArrayOutputStream

class EditViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val QUALITY = 50

    private val _deletePhoto: MutableLiveData<Result<Int>> = MutableLiveData()
    val deletePhoto: LiveData<Result<Int>> get() = _deletePhoto

    private val _uploadPhoto: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val uploadPhoto: LiveData<Result<Boolean>> get() = _uploadPhoto

    fun saveChanges(user: User){
        FirebaseAuth.getInstance().currentUser?.email?.let { it ->
            db.collection("users").document(it).set(user).addOnSuccessListener {
                Info.user = user
            }
        }
    }

    fun uploadPhoto(bitmap: Bitmap, position: Int){
        val storage = FirebaseStorage.getInstance().getReference("users/${FirebaseAuth.getInstance().currentUser?.email}/$position.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos)
        val data = baos.toByteArray()

        var uploadTask = storage.putBytes(data)
        uploadTask.addOnSuccessListener {
            _uploadPhoto.postValue(Result.success(true))
        }.addOnFailureListener {
            _uploadPhoto.postValue(Result.failure(Throwable()))
        }
    }

    fun deletePhoto(i: Int){
        val ref = FirebaseStorage.getInstance().getReference("users/${FirebaseAuth.getInstance().currentUser?.email}/${i+1}.jpg")
        ref.delete().addOnCompleteListener {
            _deletePhoto.postValue(Result.success(i))
        }
    }
}