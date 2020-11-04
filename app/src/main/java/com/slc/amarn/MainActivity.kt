package com.slc.amarn

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.slc.amarn.adapters.TabAdapter
import com.slc.amarn.views.MatchFragment
import com.slc.amarn.views.ProfileFragment
import com.slc.amarn.views.SwipeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var accentFilter: ColorFilter? = null
    private var grayFilter: ColorFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initVariables()
        initListeners()
    }

    private fun initVariables(){
        val adapter = TabAdapter(supportFragmentManager)

        adapter.addFragment(ProfileFragment(), "")
        adapter.addFragment(SwipeFragment(), "")
        adapter.addFragment(MatchFragment(), "")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)?.setIcon(R.drawable.ic_profile)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_logo)
        tabs.getTabAt(2)?.setIcon(R.drawable.ic_list)

        viewPager.currentItem = 1
        accentFilter = PorterDuffColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        grayFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.SRC_IN)
        tabs.getTabAt(0)?.icon?.colorFilter = grayFilter
        tabs.getTabAt(1)?.icon?.colorFilter = accentFilter
        tabs.getTabAt(2)?.icon?.colorFilter = grayFilter

    }

    private fun initListeners(){
        tabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.colorFilter = accentFilter
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.colorFilter = grayFilter
            }

            override fun onTabReselected(tab: TabLayout.Tab) { }

        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                if (position == 1)
                    viewPager.setPagingEnabled(false)
                else
                    viewPager.setPagingEnabled(true)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}