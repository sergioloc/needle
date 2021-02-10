package com.slc.needle.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slc.needle.MainActivity
import com.slc.needle.R
import com.slc.needle.adapters.WalkthroughAdapter
import com.slc.needle.models.Tip
import kotlinx.android.synthetic.main.activity_walkthrough.*

class WalkthroughActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)

        val tips = listOf(
            Tip(R.raw.complete, resources.getString(R.string.tip_complete), resources.getString(R.string.tip_complete_des)),
            Tip(R.raw.security, resources.getString(R.string.tip_social), resources.getString(R.string.tip_social_des)),
            Tip(R.raw.cube, resources.getString(R.string.tip_group), resources.getString(R.string.tip_group_des)),
            Tip(R.raw.swipe, resources.getString(R.string.tip_swipe), resources.getString(R.string.tip_swipe_des))
        )

        vp_walk.adapter = WalkthroughAdapter(applicationContext, tips, object :
            OnTextClickListener {
            override fun onSkipClick() {
                goToMainActivity()
            }
        })
        tab_indicator.setupWithViewPager(vp_walk)
    }

    fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    interface OnTextClickListener {
        fun onSkipClick()
    }

}