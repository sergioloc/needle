package com.slc.amarn.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slc.amarn.R
import com.slc.amarn.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settingsViewModel = SettingsViewModel()
        initButtons()
    }

    private fun initButtons(){
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        tv_signout.setOnClickListener {
            settingsViewModel.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}