package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.slc.amarn.models.User
import com.slc.amarn.models.UserPreview
import com.slc.amarn.utils.Info

class SwipeViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var emails = ArrayList<String>()
    private var users = ArrayList<UserPreview>()
    private val MAX_PHOTOS = 3

    private val _user: MutableLiveData<Result<User>> = MutableLiveData()
    val user: LiveData<Result<User>> get() = _user

    private val _userList: MutableLiveData<Result<ArrayList<UserPreview>>> = MutableLiveData()
    val userList: LiveData<Result<ArrayList<UserPreview>>> get() = _userList

    private var myEmail = ""

    fun getUsers(list: ArrayList<String>){
        emails = ArrayList()
        users = ArrayList()
        for (id in list){
            getEmailsFromGroup(id)
        }
    }

    private fun getEmailsFromGroup(id: String){
        db.collection("groups").document(id).collection("members").get().addOnSuccessListener {query ->
            for (i in 0 until query.documents.size)
                if (myEmail != query.documents[i].id) //Ignore myself
                    //emails.add(query.documents[i].id)
                    getUserInfo(query.documents[i].id)
        }
    }

    private fun getUserInfo(email: String){
        db.collection("users").document(email).get().addOnSuccessListener { documentSnapshot ->
            val u = documentSnapshot.toObject(User::class.java)
            u?.let {
                if (isCompatible(u)){
                    users.add(UserPreview(u.name, u.dateOfBirth, u.city))
                    getUserPhotos(users.size-1, email)
                }
            }
        }
    }

    private fun getUserPhotos(position: Int, email: String){
        val storage = FirebaseStorage.getInstance().getReference("users/$email")
        storage.list(MAX_PHOTOS).addOnSuccessListener {
            for (i in it.items.size-1 downTo 0)
                it.items[i].downloadUrl.addOnSuccessListener {uri ->
                    users[position].photos.add(uri.toString())
                    if (users[position].photos.size == it.items.size)
                        _userList.postValue(Result.success(users))
                }
        }
    }

    fun getMyUserInfo(){
        myEmail = FirebaseAuth.getInstance().currentUser?.email!!
        myEmail.let {
            db.collection("users").document(it).get().addOnSuccessListener { documentSnapshot ->
                val u = documentSnapshot.toObject(User::class.java)
                u?.let {
                    Info.user = u
                    _user.postValue(Result.success(u))
                }
            }
        }
    }

    private fun isCompatible(user: User): Boolean{
        if (user.visible){
            if (Info.user.orientation == 1){ // Man interested
                if (user.gender == 1 || user.gender == 4)
                    return true
            }
            else if (Info.user.orientation == 2){ // Woman interested
                if (user.gender == 2 || user.gender == 3)
                    return true
            }
            else if (Info.user.orientation == 3){ // Both interested
                return true
            }
        }
        return false
    }

}