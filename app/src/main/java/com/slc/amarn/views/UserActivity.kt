package com.slc.amarn.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}