package com.slc.amarn.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.slc.amarn.R
import com.slc.amarn.adapters.MatchAdapter
import com.slc.amarn.models.User
import kotlinx.android.synthetic.main.fragment_match.*

class MatchFragment : Fragment(), MatchAdapter.OnMatchClickListener {

    private val matches = arrayListOf(
        User(1,"Sergio",24,"LBG Madrid", 2, 1, 2,"Hola", arrayListOf(R.drawable.bear, R.drawable.clouds), "sergioloc", "Sergio López", "696752807")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_matches.layoutManager = LinearLayoutManager(context)
        rv_matches.adapter = MatchAdapter(matches, this)
    }

    override fun onMatchClick(user: User) {
        val i = Intent(context, UserActivity::class.java)
        i.putExtra("user", user)
        startActivity(i)
    }

}