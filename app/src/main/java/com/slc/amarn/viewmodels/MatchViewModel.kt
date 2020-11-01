package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.amarn.models.Match
import com.slc.amarn.models.User
import com.slc.amarn.models.UserMatch
import com.slc.amarn.utils.Info

class MatchViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var emailList = ArrayList<Match>()
    private var infolist = ArrayList<UserMatch>()

    private val _matchList: MutableLiveData<Result<ArrayList<UserMatch>>> = MutableLiveData()
    val matchList: LiveData<Result<ArrayList<UserMatch>>> get() = _matchList

    fun getMatches(){
        db.collection("users").document(Info.email).collection("matched").get().addOnSuccessListener {query ->
            emailList = ArrayList()
            for (i in 0 until query.documents.size)
                emailList.add(Match(query.documents[i].id, query.documents[i].get("date") as String))
            getMatchesInfo()
        }
    }

    private fun getMatchesInfo(){
        for (match in emailList){
            db.collection("users").document(match.email).get().addOnSuccessListener {documentSnapshot ->
                val u = documentSnapshot.toObject(User::class.java)
                u?.let {
                    infolist.add(UserMatch(
                        name = u.name,
                        dateOfBirth = u.dateOfBirth,
                        city = u.city,
                        description = u.description,
                        instagram = u.instagram,
                        facebook = u.facebook,
                        phone = u.phone,
                        date = match.date
                    ))
                }
                _matchList.postValue(Result.success(infolist))
            }
        }

    }

}