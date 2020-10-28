package com.slc.amarn.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.slc.amarn.R
import com.slc.amarn.adapters.PhotoAdapter
import com.slc.amarn.models.User
import com.slc.amarn.utils.Age
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    var user: User? = null
    var email: String = ""
    var adapter: PhotoAdapter? = null
    var userViewModel: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initVariables()
        initObservers()
    }

    private fun initVariables(){
        userViewModel = UserViewModel()
        user = intent.getSerializableExtra("user") as User
        email = intent.getStringExtra("email")!!
        tv_name.text = user?.name
        tv_age.text = "${user?.dateOfBirth?.let { Age().getAge(it) }} years"
        tv_city.text = user?.city
        tv_desciption.text = user?.description
        tv_instagram.text = user?.instagram
        tv_facebook.text = user?.facebook
        tv_phone.text = user?.phone

        if (email == FirebaseAuth.getInstance().currentUser?.email){
            setAdapter(Info.photos)
        }
        else
            userViewModel?.getUserPhotos(email)

    }

    private fun initObservers(){
        userViewModel?.photos?.observe(this,
            Observer<Result<ArrayList<String>>> {
                it.onSuccess {result ->
                    setAdapter(result)
                }
            }
        )
    }

    private fun setAdapter(list: List<String>){
        adapter = PhotoAdapter(applicationContext, list)
        vp_photos.adapter = adapter
        tab_indicator.setupWithViewPager(vp_photos)
    }
}