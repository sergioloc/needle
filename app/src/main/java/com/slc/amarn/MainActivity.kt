package com.slc.amarn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.slc.amarn.adapters.PagerAdapter
import com.slc.amarn.views.MatchFragment
import com.slc.amarn.views.ProfileFragment
import com.slc.amarn.views.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTabs()
    }

    private fun setupTabs(){
        val adapter = PagerAdapter(supportFragmentManager)

        adapter.addFragment(ProfileFragment(), "Profile")
        adapter.addFragment(SearchFragment(), "Seach")
        adapter.addFragment(MatchFragment(), "Match")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        //tabs.getTabAt(0)?.setIcon(R.drawable.ic_launcher_background)
        //tabs.getTabAt(1)?.setIcon(R.drawable.ic_launcher_background)
        //tabs.getTabAt(1)?.setIcon(R.drawable.ic_launcher_background)
    }
}