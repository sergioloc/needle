package com.slc.amarn.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.slc.amarn.R
import com.slc.amarn.adapters.GroupAdapter
import com.slc.amarn.models.GroupId
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), GroupAdapter.OnGroupClickListener {

    private lateinit var settingsViewModel: SettingsViewModel
    private var initSwitch: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settingsViewModel = SettingsViewModel()
        initVariables()
        initButtons()
        initObservers()
        setAdapter()
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
                    rv_groups.adapter = GroupAdapter(list, this)
                    loader.visibility = View.GONE
                }
            }
        )
        settingsViewModel.textCopied.observe(this,
            Observer<Result<Boolean>> {
                it.onSuccess {
                    Toast.makeText(applicationContext, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
                }
            }
        )
        settingsViewModel.leave.observe(this,
            Observer<Result<Boolean>> {
                it.onSuccess {
                    Toast.makeText(applicationContext, "Leave group", Toast.LENGTH_SHORT).show()
                    setAdapter()
                }
            }
        )
    }

    private fun setAdapter(){
        if (Info.user.groups.isEmpty()){
            loader.visibility = View.GONE
            tv_empty.visibility = View.VISIBLE
            rv_groups.visibility = View.GONE
        }
        else{
            loader.visibility = View.VISIBLE
            rv_groups.visibility = View.VISIBLE
            settingsViewModel.getGroupInfo(Info.user.groups)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (initSwitch != swt_visible.isChecked)
            settingsViewModel.modifyVisibility(swt_visible.isChecked)
    }

    override fun onCopyClick(id: String) {
        settingsViewModel.copyId(applicationContext, id)
    }

    override fun onLeaveClick(id: String) {
       leaveGroupDialog(id)
    }

    override fun onDeleteClick(id: String) {
        deleteGroupDialog(id)
    }

    private fun leaveGroupDialog(id: String){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("Do you want to leave this group?")
        alertDialog.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        alertDialog.setPositiveButton("Yes"){ _, _ ->
            settingsViewModel.leaveGroup(id)
        }
        alertDialog.show()
    }

    private fun deleteGroupDialog(id: String){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("Do you want to remove this group?")
        alertDialog.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        alertDialog.setPositiveButton("Yes"){ _, _ ->
            settingsViewModel.removeGroup(id)
        }
        alertDialog.show()
    }

}