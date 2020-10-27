package com.slc.amarn.viewmodels

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.slc.amarn.models.Photo
import com.slc.amarn.models.User
import com.slc.amarn.utils.Info
import java.io.ByteArrayOutputStream

class EditViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val MAX_PHOTOS = 3

    private val _drawables: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val drawables: LiveData<Result<Boolean>> get() = _drawables

    private val _photoState: MutableLiveData<Result<Int>> = MutableLiveData()
    val photoState: LiveData<Result<Int>> get() = _photoState


    fun saveChanges(user: User){
        FirebaseAuth.getInstance().currentUser?.email?.let { it ->
            db.collection("users").document(it).set(user).addOnSuccessListener {
                Info.callUserService = true
            }
        }
    }

    fun getPhotosURL(){
        val storage = FirebaseStorage.getInstance().getReference("users")
        storage.list(MAX_PHOTOS).addOnSuccessListener {
            Info.photos = ArrayList()
            for (i in 0 until it.items.size)
                it.items[i].downloadUrl.addOnSuccessListener {uri ->
                    Info.photos.add(Photo(it.items[i].path, uri.toString()))
                    _drawables.postValue(Result.success(true))
                }
        }
    }


    fun uploadPhoto(bitmap: Bitmap, position: Int){
        val storage = FirebaseStorage.getInstance().getReference("users/$position.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storage.putBytes(data)
        uploadTask.addOnFailureListener {
           
        }.addOnSuccessListener {
            getPhotosURL()
        }
    }

    fun deletePhoto(i: Int){
        val ref = FirebaseStorage.getInstance().getReference(Info.photos[i].path)
        ref.delete().addOnCompleteListener {
            _photoState.postValue(Result.success(i))
        }
    }
}