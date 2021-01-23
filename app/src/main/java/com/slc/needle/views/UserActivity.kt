package com.slc.needle.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slc.needle.R
import com.slc.needle.adapters.PhotoAdapter
import com.slc.needle.models.User
import com.slc.needle.utils.Age
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    var user: User? = null
    var adapter: PhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initVariables()
    }

    private fun initVariables(){
        user = intent.getSerializableExtra("user") as User
        tv_name.text = user?.name
        tv_age.text = "${user?.dateOfBirth?.let { Age().getAge(it) }} years"
        tv_city.text = user?.city
        tv_desciption.text = user?.description
        tv_instagram.text = user?.instagram
        tv_facebook.text = user?.facebook
        tv_phone.text = user?.phone
        user?.images?.let { setAdapter(it) }
    }

    private fun setAdapter(list: List<String>){
        adapter = PhotoAdapter(applicationContext, list)
        vp_photos.adapter = adapter
        tab_indicator.setupWithViewPager(vp_photos)
    }
}