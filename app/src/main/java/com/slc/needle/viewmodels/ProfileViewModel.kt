package com.slc.needle.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.needle.models.Group
import com.slc.needle.utils.Info


class ProfileViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _groupId: MutableLiveData<Result<String>> = MutableLiveData()
    val groupId: LiveData<Result<String>> get() = _groupId

    fun createGroup(group: Group){
        db.collection("groups").add(group).addOnSuccessListener {
            joinGroup(it.path.replace("groups/",""), true)
        }
    }

    fun joinGroup(id: String, owner: Boolean){
        db.collection("groups").document(id).collection("members").document(FirebaseAuth.getInstance().currentUser?.email!!).set(hashMapOf("a" to true)).addOnSuccessListener {
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