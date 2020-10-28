package com.slc.amarn.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.slc.amarn.R
import com.slc.amarn.models.User
import com.slc.amarn.utils.Age
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ProfileViewModel()
        profileViewModel.getUserInfo()
        initButtons()
        initObservers()
    }

    private fun initButtons(){
        fab_edit.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            startActivity(intent)
        }

        fab_settings.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }

        iv_icon.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra("user", Info.user)
            intent.putExtra("email", FirebaseAuth.getInstance().currentUser?.email)
            startActivity(intent)
        }
    }

    private fun initObservers(){
        profileViewModel.user.observe(this,
            Observer<Result<User>> {
                it.onSuccess { user ->
                    tv_info.text = "${user.name}, ${Age().getAge(user.dateOfBirth)}"
                    tv_city.text = user.city
                    Info.user = user
                }
                it.onFailure { result ->
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}