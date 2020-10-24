package com.slc.amarn.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.slc.amarn.R
import com.slc.amarn.models.User
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    val myUser = User(1,"Sergio",24,"LBG Madrid", 2, 1, 2,"Hola", arrayListOf(R.drawable.bear, R.drawable.clouds), "sergioloc", "Sergio López", "696752807")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons(){
        fab_edit.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("user", myUser)
            startActivity(intent)
        }

        fab_settings.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }

        iv_icon.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra("user", myUser)
            startActivity(intent)
        }
    }
}