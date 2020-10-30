package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.slc.amarn.models.Group
import com.slc.amarn.models.User
import com.slc.amarn.utils.Info


class ProfileViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val MAX_PHOTOS = 3

    private val _drawables: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val drawables: LiveData<Result<Boolean>> get() = _drawables

    private val _groupId: MutableLiveData<Result<String>> = MutableLiveData()
    val groupId: LiveData<Result<String>> get() = _groupId

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

    fun createGroup(group: Group){
        db.collection("groups").add(group).addOnSuccessListener {
            joinGroup(it.path.replace("groups/",""), true)
        }
    }

    fun joinGroup(id: String, owner: Boolean){
        db.collection("groups").document(id).collection("members").document(FirebaseAuth.getInstance().currentUser?.email!!).set(Info.user).addOnSuccessListener {
            if (owner)
                _groupId.postValue(Result.success(id))
            else
                _groupId.postValue(Result.success(""))
            addGroupToUserList(id)
        }
    }

    private fun addGroupToUserList(id: String){
        if (Info.user.groups.contains(id)){
            _groupId.postValue(Result.failure(Throwable("You are already in this group")))
        }
        else{
            Info.user.groups.add(id)
            FirebaseAuth.getInstance().currentUser?.email?.let { it ->
                db.collection("users").document(it).set(Info.user)
            }
        }
    }
}