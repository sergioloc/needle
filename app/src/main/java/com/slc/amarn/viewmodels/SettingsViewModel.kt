package com.slc.amarn.viewmodels

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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

    private val _textCopied: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val textCopied: LiveData<Result<Boolean>> get() = _textCopied

    private val _leave: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val leave: LiveData<Result<Boolean>> get() = _leave

    fun signOut(){
        Info.user = User()
        Info.photos = ArrayList()
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

    fun getGroupInfo(idList: ArrayList<String>){
        val list = ArrayList<GroupId>()
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

    fun leaveGroup(id: String){
        db.collection("groups").document(id).collection("members").document(FirebaseAuth.getInstance().currentUser?.email!!).delete().addOnSuccessListener {
            Info.user.groups.remove(id)
            db.collection("users").document(FirebaseAuth.getInstance().currentUser?.email!!).set(Info.user).addOnSuccessListener {
                _leave.postValue(Result.success(true))
            }
        }
    }

    fun copyId(context: Context, id: String){
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("text", id)
        clipboard.setPrimaryClip(clip)
        _textCopied.postValue(Result.success(true))
    }

}