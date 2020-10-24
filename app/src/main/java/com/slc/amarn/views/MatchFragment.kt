package com.slc.amarn.views

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

class MatchFragment : Fragment() {

    private val matches = arrayListOf(
        User(1,"Sergio",24,"LBG Madrid", 3, 1, 2,"Hola"),
        User(1,"Sergio",24,"LBG Madrid", 3, 1, 2,"Hola")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_matches.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MatchAdapter(matches)
        }
    }

}