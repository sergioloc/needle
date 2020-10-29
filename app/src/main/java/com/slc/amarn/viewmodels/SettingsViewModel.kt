package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.amarn.models.Group
import com.slc.amarn.models.GroupId
import com.slc.amarn.models.User
import com.slc.amarn.utils.Info

class SettingsViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _groupList: MutableLiveData<Result<ArrayList<GroupId>>> = MutableLiveData()
    val groupList: LiveData<Result<ArrayList<GroupId>>> get() = _groupList

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
    }

    fun modifyVisibility(value: Boolean){
        Info.user.visible = value
        FirebaseAuth.getInstance().currentUser?.email?.let { it ->
            db.collection("users").document(it).set(Info.user).addOnFailureListener {
                Info.user.visible = !value
            }
        }
    }

    fun getGroupList(idList: ArrayList<String>){
        var list = ArrayList<GroupId>()
        for (id in idList){
            db.collection("groups").document(id).get().addOnSuccessListener { documentSnapshot ->
                val group = documentSnapshot.toObject(Group::class.java)
                group?.let {
                    list.add(GroupId(id, it.name, it.numMax, it.owner))
                }
                _groupList.postValue(Result.success(list))
            }
        }
    }

}