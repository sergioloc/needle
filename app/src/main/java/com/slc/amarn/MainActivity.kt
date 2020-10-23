package com.slc.amarn

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.slc.amarn.adapters.TabAdapter
import com.slc.amarn.views.MatchFragment
import com.slc.amarn.views.ProfileFragment
import com.slc.amarn.views.SwipeFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTabs()
    }

    private fun setupTabs(){
        val adapter = TabAdapter(supportFragmentManager)

        adapter.addFragment(ProfileFragment(), "")
        adapter.addFragment(SwipeFragment(), "")
        adapter.addFragment(MatchFragment(), "")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)?.setIcon(R.drawable.ic_profile)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_hot)
        tabs.getTabAt(2)?.setIcon(R.drawable.ic_message)

        viewPager.currentItem = 1
        val accentFilter: ColorFilter = PorterDuffColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        val whiteFilter: ColorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.SRC_IN)
        tabs.getTabAt(1)?.icon?.colorFilter = accentFilter

        tabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.colorFilter = accentFilter
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.colorFilter = whiteFilter
            }

            override fun onTabReselected(tab: TabLayout.Tab) { }

        })
    }
}