package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage

class UserViewModel: ViewModel() {

    private val _photos: MutableLiveData<Result<ArrayList<String>>> = MutableLiveData()
    val photos: LiveData<Result<ArrayList<String>>> get() = _photos

    private val MAX_PHOTOS = 3

    fun getUserPhotos(email: String){
        var list = ArrayList<String>()
        val storage = FirebaseStorage.getInstance().getReference("users/$email")
        storage.list(MAX_PHOTOS).addOnSuccessListener {
            for (i in it.items.size-1 downTo 0)
                it.items[i].downloadUrl.addOnSuccessListener {uri ->
                    list.add(uri.toString())
                    _photos.postValue(Result.success(list))
                }
        }
    }
}