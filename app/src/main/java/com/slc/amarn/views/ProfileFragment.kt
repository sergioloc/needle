package com.slc.amarn.views

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.slc.amarn.R
import com.slc.amarn.models.User
import com.slc.amarn.utils.Age
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ProfileViewModel()
        if (Info.user.name == "")
            profileViewModel.getUserInfo()
        if (Info.photos.isEmpty())
            profileViewModel.getMyPhotosURL()
        else
            setIconPhoto()
        initButtons()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if (Info.reloadPhotos){
            profileViewModel.getMyPhotosURL()
            Info.reloadPhotos = false
        }
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
        profileViewModel.drawables.observe(this,
            Observer<Result<Boolean>> {
                it.onSuccess {result ->
                    if (result){
                        setIconPhoto()
                    }
                }
            }
        )
    }

    private fun setIconPhoto(){
        if (Info.photos[0].isNotBlank()){
            Glide.with(context!!).load(Info.photos[0]).into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    iv_icon.setImageDrawable(resource)
                }
            })
        }
    }
}