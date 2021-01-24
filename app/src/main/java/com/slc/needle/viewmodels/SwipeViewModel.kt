package com.slc.needle.viewmodels

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.needle.R
import com.slc.needle.models.Group
import com.slc.needle.models.User
import com.slc.needle.models.UserPreview
import com.slc.needle.utils.Info
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SwipeViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var users = ArrayList<UserPreview>()
    private var ignore = ArrayList<String>()
    private val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
    var found = false

    private val _getUser: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val getUser: LiveData<Result<Boolean>> get() = _getUser

    private val _swipeList: MutableLiveData<Result<ArrayList<UserPreview>>> = MutableLiveData()
    val swipeList: LiveData<Result<ArrayList<UserPreview>>> get() = _swipeList

    fun getMyUserInfo(){
        Info.email = FirebaseAuth.getInstance().currentUser?.email!!
        Info.email.let {
            db.collection("users").document(it).get().addOnSuccessListener { documentSnapshot ->
                val u = documentSnapshot.toObject(User::class.java)
                u?.let {
                    Info.user = u
                    _getUser.postValue(Result.success(true))
                }
            }.addOnFailureListener {
                _getUser.postValue(Result.failure(Throwable("")))
            }
        }
    }

    fun getMembers(){
        users = ArrayList()
        ignore = ArrayList()

        //Get ignore emails
        db.collection("users").document(Info.email).collection("swiped").get().addOnSuccessListener {query ->
            for (i in 0 until query.documents.size)
                ignore.add(query.documents[i].id)

            for (id in Info.user.groups){
                getEmailsFromGroup(id, ignore)
            }
        }
    }

    private fun getEmailsFromGroup(id: String, ignore: ArrayList<String>){
        //Get group info
        db.collection("groups").document(id).get().addOnSuccessListener { documentSnapshot ->
            val group = documentSnapshot.toObject(Group::class.java)

            //Get group members
            db.collection("groups").document(id).collection("members").get().addOnSuccessListener {query ->
                if (query.documents.size == 0){ //Group leaved or deleted
                    Info.user.groups.remove(id)
                    db.collection("users").document(Info.email).set(Info.user)
                }
                else {
                    for (i in 0 until query.documents.size)
                        if (Info.email != query.documents[i].id) //If is not me
                            if (!ignore.contains(query.documents[i].id)) //If not swiped yet
                                getUserInfo(query.documents[i].id, group?.name ?: "")
                    if (!found)
                        _swipeList.postValue(Result.failure(Throwable("")))
                }
            }
        }
    }

    private fun getUserInfo(email: String, group: String){
        db.collection("users").document(email).get().addOnSuccessListener { documentSnapshot ->
            val u = documentSnapshot.toObject(User::class.java)
            u?.let {
                if (isCompatible(u)){
                    found = true
                    users.add(UserPreview(email, u.name, group, u.dateOfBirth, u.city, u.images))
                    _swipeList.postValue(Result.success(users))
                }
            }
        }
    }

    fun swipeUser(email: String, group: String, like: Boolean){
        if (like){
            db.collection("users").document(email).collection("swiped").document(Info.email).get().addOnSuccessListener {
                if (it.data == null) { //User didn't swipe me
                    db.collection("users").document(Info.email).collection("swiped").document(email).set(hashMapOf("like" to like))
                }
                else{
                    if (it.data!!["like"] as Boolean){ //User gave me like
                        Info.notification = true
                        db.collection("users").document(Info.email).collection("matched").document(email).set(hashMapOf("date" to dateFormat.format(Date()), "group" to group))
                        db.collection("users").document(Info.email).collection("swiped").document(email).set(hashMapOf("like" to like))
                        db.collection("users").document(email).collection("matched").document(Info.email).set(hashMapOf("date" to dateFormat.format(Date()), "group" to group))
                    }
                    else { //User gave me dislike
                        db.collection("users").document(Info.email).collection("swiped").document(email).set(hashMapOf("like" to like))
                    }
                }
            }
        }
        else {
            db.collection("users").document(Info.email).collection("swiped").document(email).set(hashMapOf("like" to like))
        }
    }

    private fun isCompatible(user: User): Boolean{
        if (user.visible && user.dateOfBirth.isNotEmpty()){
            //MAN
            if ((Info.user.gender == 1 || Info.user.gender == 4) && Info.user.orientation == 1){ // I am a man looking for a man
                if (user.gender == 1 || user.gender == 4) // Is a man
                    return true
            }
            else if ((Info.user.gender == 1 || Info.user.gender == 4) && Info.user.orientation == 2){ // I am a man looking for a woman
                if (user.gender == 2 || user.gender == 31) // Is a woman
                    return true
            }
            else if ((Info.user.gender == 1 || Info.user.gender == 4) && Info.user.orientation == 3){ // I am a man looking for a someone
                    return true
            }
            //WOMAN
            else if ((Info.user.gender == 2 || Info.user.gender == 3) && Info.user.orientation == 1){ // I am a woman looking for a man
                if (user.gender == 1 || user.gender == 4) // Is a man
                    return true
            }
            else if ((Info.user.gender == 2 || Info.user.gender == 3) && Info.user.orientation == 2){ // I am a woman looking for a woman
                if (user.gender == 2 || user.gender == 3) // Is a woman
                    return true
            }
            else if ((Info.user.gender == 2 || Info.user.gender == 3) && Info.user.orientation == 3){ // I am a woman looking for a someone
                    return true
            }
        }
        return false
    }

}