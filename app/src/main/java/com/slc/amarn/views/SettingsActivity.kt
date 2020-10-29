package com.slc.amarn.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.slc.amarn.R
import com.slc.amarn.adapters.GroupAdapter
import com.slc.amarn.models.GroupId
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
        initObservers()
        settingsViewModel.getGroupList(Info.user.groups)
    }

    private fun initVariables(){
        initSwitch = Info.user.visible
        swt_visible.isChecked = initSwitch
        rv_groups.layoutManager = LinearLayoutManager(applicationContext)
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

    private fun initObservers(){
        settingsViewModel.groupList.observe(this,
            Observer<Result<ArrayList<GroupId>>> {
                it.onSuccess {list ->
                    rv_groups.adapter = GroupAdapter(list)
                }
            }
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (initSwitch != swt_visible.isChecked)
            settingsViewModel.modifyVisibility(swt_visible.isChecked)
    }
}