package com.slc.amarn.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slc.amarn.R
import com.slc.amarn.adapters.PhotoAdapter
import com.slc.amarn.models.User
import com.slc.amarn.utils.Age
import com.slc.amarn.utils.Info
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    var user: User? = null
    var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initVariables()
    }

    private fun initVariables(){
        user = intent.getSerializableExtra("user") as User
        email = intent.getStringExtra("email")!!
        tv_name.text = user?.name
        tv_age.text = "${user?.dateOfBirth?.let { Age().getAge(it) }} years"
        tv_city.text = user?.city
        tv_desciption.text = user?.description
        tv_instagram.text = user?.instagram
        tv_facebook.text = user?.facebook
        tv_phone.text = user?.phone
        var adapter = PhotoAdapter(applicationContext, Info.photos.reversed())
        vp_photos.adapter = adapter
        tab_indicator.setupWithViewPager(vp_photos)
    }
}