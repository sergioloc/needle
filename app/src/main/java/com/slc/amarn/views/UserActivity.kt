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
        initButtons()
        initListeners()
    }

    private fun initVariables(){
        user = intent.getSerializableExtra("user") as User
        tv_name.text = user?.name
        tv_age.text = "${user?.age} years"
        tv_lbg.text = user?.lbg
        tv_desciption.text = user?.description
        tv_instagram.text = user?.instagram
        tv_facebook.text = user?.facebook
        tv_phone.text = user?.phone
        when(user?.membership){
            0 -> btn_membership.text = "Observer"
            1 -> btn_membership.text = "Baby"
            2 -> btn_membership.text = "Full"
            3 -> btn_membership.text = "Alumni"
        }
        var adapter = PhotoAdapter(applicationContext, user!!.photos)
        vp_photos.adapter = adapter
        chip_info.text = "1/${user?.photos?.size}"
    }

    private fun initButtons(){
        btn_back.setOnClickListener {
            if (vp_photos.currentItem != 0) vp_photos.currentItem = vp_photos.currentItem - 1
        }

        btn_next.setOnClickListener {
            if (vp_photos.currentItem != user?.photos?.size) vp_photos.currentItem = vp_photos.currentItem + 1
        }
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