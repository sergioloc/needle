package com.slc.amarn.views

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.slc.amarn.R
import com.slc.amarn.models.Group
import com.slc.amarn.models.User
import com.slc.amarn.utils.Age
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.dialog_join_group.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var profileViewModel: ProfileViewModel
    private var createGroupDialog: Dialog? = null
    private var joinGroupDialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ProfileViewModel()
        initButtons()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if (Info.user.name.isNotBlank()){
            tv_info.text = "${Info.user.name}, ${Age().getAge(Info.user.dateOfBirth)}"
            tv_city.text = Info.user.city
            setIconImage()
        }
    }

    private fun initButtons(){
        fab_edit.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            startActivity(intent)
        }
        fab_settings.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }
        iv_icon.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra("user", Info.user)
            startActivity(intent)
        }
        cg_create.setOnClickListener {
            showCreateDialog()
        }
        cg_join.setOnClickListener {
            showJoinDialog()
        }
    }

    private fun initObservers(){
        profileViewModel.groupId.observe(this,
            Observer<Result<String>> {result ->
                createGroupDialog?.dismiss()
                joinGroupDialog?.dismiss()
                result.onSuccess {code ->
                    if (code.isNotBlank())
                        showCodeDialog(code)
                }
                result.onFailure {
                    showErrorDialog(it.message.toString())
                }
            }
        )
    }

    private fun setIconImage(){
        if (Info.user.images.isNotEmpty()){
            Glide.with(context!!).load(Info.user.images[0]).into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    iv_icon.setImageDrawable(resource)
                }
            })
        }
    }

    //Dialogs --------------------------------------------------------------------------------------

    private fun showCreateDialog() {
        createGroupDialog = Dialog(context!!)
        createGroupDialog?.let {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            it.setContentView(R.layout.dialog_create_group)
            it.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btnCreate = it.findViewById(R.id.btn_create) as Button
            val etName = it.findViewById(R.id.et_name) as EditText
            val loader = it.findViewById(R.id.loader) as ProgressBar
            btnCreate.setOnClickListener {
                loader.visibility = View.VISIBLE
                etName.visibility = View.INVISIBLE
                profileViewModel.createGroup(Group(etName.text.toString(), FirebaseAuth.getInstance().currentUser?.email!!))
            }
            it.show()
        }
    }

    private fun showCodeDialog(code: String) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_code_group)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnCopy = dialog.findViewById(R.id.btn_copy) as Button
        val btnClose = dialog.findViewById(R.id.btn_close) as Button
        val tvCode = dialog.findViewById(R.id.tv_code) as TextView
        tvCode.text = code
        btnCopy.setOnClickListener {
            val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("text", code)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context!!, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showJoinDialog() {
        joinGroupDialog = Dialog(context!!)
        joinGroupDialog?.let {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            it.setContentView(R.layout.dialog_join_group)
            it.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btnJoin = it.findViewById(R.id.btn_join) as Button
            val etCode = it.findViewById(R.id.et_code) as EditText
            val loader = it.findViewById(R.id.loader) as ProgressBar
            btnJoin.setOnClickListener {
                if (etCode.text.isNotBlank()){
                    loader.visibility = View.VISIBLE
                    etCode.visibility = View.INVISIBLE
                    profileViewModel.joinGroup(etCode.text.toString(), false)
                }
                else
                    Toast.makeText(context!!, "Write a code", Toast.LENGTH_SHORT).show()
            }
            it.show()
        }
    }

    private fun showErrorDialog(message: String) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_error_group)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvMessage = dialog.findViewById(R.id.tv_message) as TextView
        tvMessage.text = message
        dialog.show()
    }
}