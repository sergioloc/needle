package com.slc.amarn.viewmodels

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

class EditViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val MAX_PHOTOS = 3
    private val _photoState: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val photoState: LiveData<Result<Boolean>> get() = _photoState


    fun saveChanges(user: User){
        FirebaseAuth.getInstance().currentUser?.email?.let { it ->
            db.collection("users").document(it).set(user).addOnSuccessListener {
                Info.callUserService = true
            }
        }
    }

    fun getPhotosURL(ivList: ArrayList<ImageView>){
        val storage = FirebaseStorage.getInstance().getReference("users")
        storage.list(MAX_PHOTOS).addOnSuccessListener {
            Info.photos = ArrayList()
            for (i in 0 until it.items.size)
                it.items[i].downloadUrl.addOnSuccessListener {uri ->
                    Info.photos.add(Photo(it.items[i].path, uri.toString()))
                    Glide.with(ivList[i].context).load(uri).into(object : SimpleTarget<Drawable?>() {
                        override fun onResourceReady(resource: Drawable,transition: Transition<in Drawable?>?) {
                            ivList[i].background = resource
                        }
                    })
                }
        }
    }

    fun setPhotoInImageView(ivList: ArrayList<ImageView>) {
        for (i in 0 until Info.photos.size){
            Glide.with(ivList[i].context).load(Info.photos[i].url).into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(resource: Drawable,transition: Transition<in Drawable?>?) {
                    ivList[i].background = resource
                }
            })
        }
    }

    fun deletePhoto(i: Int){
        val ref = FirebaseStorage.getInstance().getReference(Info.photos[i].path)
        ref.delete().addOnCompleteListener {
            _photoState.postValue(Result.success(true))
        }
    }
}