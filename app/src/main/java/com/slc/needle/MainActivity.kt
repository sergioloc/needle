package com.slc.needle

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.slc.needle.adapters.TabAdapter
import com.slc.needle.components.NavBar
import com.slc.needle.views.MatchFragment
import com.slc.needle.views.ProfileFragment
import com.slc.needle.views.SwipeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var accentFilter: ColorFilter? = null
    private var grayFilter: ColorFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initVariables()
        initListeners()
        initButtons()
    }

    private fun initVariables(){
        val adapter = TabAdapter(supportFragmentManager)

        adapter.addFragment(ProfileFragment(), "")
        adapter.addFragment(SwipeFragment(), "")
        adapter.addFragment(MatchFragment(), "")

        viewPager.adapter = adapter
        viewPager.currentItem = 1
        accentFilter = PorterDuffColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        grayFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.SRC_IN)

    }

    private fun initListeners(){

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                when (position) {
                    1 -> {
                        nav_bar.setUpNavBarFrom(NavBar.SWIPE)
                        viewPager.setPagingEnabled(false)
                    }
                    else -> {
                        viewPager.setPagingEnabled(true)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun initButtons(){
        nav_bar.findViewById<ImageView>(R.id.profile_NavBar).setOnClickListener {
            nav_bar.setUpNavBarFrom(NavBar.PROFILE)
            viewPager.currentItem = 0
        }
        nav_bar.findViewById<ImageView>(R.id.swipe_NavBar).setOnClickListener {
            nav_bar.setUpNavBarFrom(NavBar.SWIPE)
            viewPager.currentItem = 1
        }
        nav_bar.findViewById<ImageView>(R.id.match_NavBar).setOnClickListener {
            nav_bar.setUpNavBarFrom(NavBar.MATCH)
            viewPager.currentItem = 2
        }
    }
}