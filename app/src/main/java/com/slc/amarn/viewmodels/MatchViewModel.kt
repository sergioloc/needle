package com.slc.amarn.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.slc.amarn.models.MatchPreview
import com.slc.amarn.models.User
import com.slc.amarn.models.Match
import com.slc.amarn.utils.Info

class MatchViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var infolist = ArrayList<Match>()

    private val _matchList: MutableLiveData<Result<ArrayList<Match>>> = MutableLiveData()
    val matchList: LiveData<Result<ArrayList<Match>>> get() = _matchList

    fun getMatches(){
        db.collection("users").document(Info.email).collection("matched").get().addOnSuccessListener {query ->
            var matchPreviewList = ArrayList<MatchPreview>()
            for (i in 0 until query.documents.size)
                matchPreviewList.add(MatchPreview(query.documents[i].id, query.documents[i].get("group") as String, query.documents[i].get("date") as String))
            getMatchesInfo(matchPreviewList)
        }
    }

    private fun getMatchesInfo(list: ArrayList<MatchPreview>){
        for (match in list){
            db.collection("users").document(match.email).get().addOnSuccessListener {documentSnapshot ->
                val u = documentSnapshot.toObject(User::class.java)
                u?.let {
                    infolist.add(
                        Match(
                            name = u.name,
                            dateOfBirth = u.dateOfBirth,
                            city = u.city,
                            description = u.description,
                            instagram = u.instagram,
                            facebook = u.facebook,
                            phone = u.phone,
                            group = match.group,
                            date = match.date,
                            images = u.images
                        )
                    )
                }
                _matchList.postValue(Result.success(infolist))
            }
        }

    }

}