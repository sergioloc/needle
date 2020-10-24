package com.slc.amarn.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.slc.amarn.R
import com.slc.amarn.adapters.PhotoAdapter
import com.slc.amarn.models.User
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initVariables()
        initListeners()
    }

    private fun initVariables(){
        user = intent.getSerializableExtra("user") as User
        tv_name.text = user?.name
        tv_age.text = "${user?.age} years"
        tv_city.text = user?.city
        tv_desciption.text = user?.description
        tv_instagram.text = user?.instagram
        tv_facebook.text = user?.facebook
        tv_phone.text = user?.phone
        var adapter = PhotoAdapter(applicationContext, user!!.photos)
        vp_photos.adapter = adapter
        chip_info.text = "1/${user?.photos?.size}"
    }


    private fun initListeners(){
        vp_photos.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                chip_info.text = "${position+1}/${user?.photos?.size}"
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}