package com.slc.amarn.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slc.amarn.R
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var initSwitch: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settingsViewModel = SettingsViewModel()
        initVariables()
        initButtons()
    }

    private fun initVariables(){
        initSwitch = Info.user.visible
        swt_visible.isChecked = initSwitch
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (initSwitch != swt_visible.isChecked)
            settingsViewModel.modifyVisibility(swt_visible.isChecked)
    }
}