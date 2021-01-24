package com.slc.needle.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.slc.needle.R
import com.slc.needle.adapters.MatchAdapter
import com.slc.needle.models.Match
import com.slc.needle.models.User
import com.slc.needle.utils.Info
import com.slc.needle.viewmodels.MatchViewModel
import kotlinx.android.synthetic.main.fragment_match.*

class MatchFragment : Fragment(), MatchAdapter.OnMatchClickListener {

    private val matchViewModel = MatchViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariables()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if (Info.notification)
            matchViewModel.getMatches()
    }

    private fun initVariables(){
        rv_matches.layoutManager = LinearLayoutManager(context)
    }

    private fun initObservers(){
        matchViewModel.matchList.observe(this,
            Observer<Result<ArrayList<Match>>> {
                it.onSuccess {list ->
                    list.sortByDescending { match ->  match.date }
                    rv_matches.adapter = MatchAdapter(list, this)
                    Info.notification = false
                }
            }
        )
    }

    override fun onMatchClick(match: Match) {
        val i = Intent(context, UserActivity::class.java)
        val u = User(
            name = match.name,
            dateOfBirth = match.dateOfBirth,
            city = match.city,
            description = match.description,
            instagram = match.instagram,
            facebook = match.facebook,
            phone = match.phone,
            images = match.images

        )
        i.putExtra("user", u)
        startActivity(i)
    }

}