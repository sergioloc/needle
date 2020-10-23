package com.slc.amarn.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.slc.amarn.R
import com.slc.amarn.adapters.PhotoAdapter
import kotlinx.android.synthetic.main.activity_user.*


class UserActivity : AppCompatActivity() {

    var images = arrayOf(R.drawable.bear, R.drawable.bear,R.drawable.bear)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        var adapter = PhotoAdapter(applicationContext, images)
        vp_photos.adapter = adapter
        chip_info.text = "1/${images.size}"

        vp_photos.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                chip_info.text = "${position+1}/${images.size}"
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}