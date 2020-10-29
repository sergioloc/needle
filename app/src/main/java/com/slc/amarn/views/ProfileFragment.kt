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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.slc.amarn.R
import com.slc.amarn.models.User
import com.slc.amarn.utils.Age
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ProfileViewModel()
        if (Info.user.name == "")
            profileViewModel.getUserInfo()
        if (Info.photos.isEmpty())
            profileViewModel.getMyPhotosURL()
        else
            setIconPhoto()
        initButtons()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if (Info.reloadPhotos){
            profileViewModel.getMyPhotosURL()
            Info.reloadPhotos = false
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
            intent.putExtra("email", FirebaseAuth.getInstance().currentUser?.email)
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
        profileViewModel.user.observe(this,
            Observer<Result<User>> {
                it.onSuccess { user ->
                    tv_info.text = "${user.name}, ${Age().getAge(user.dateOfBirth)}"
                    tv_city.text = user.city
                    Info.user = user
                }
                it.onFailure { result ->
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
        profileViewModel.drawables.observe(this,
            Observer<Result<Boolean>> {
                it.onSuccess {result ->
                    if (result){
                        setIconPhoto()
                    }
                }
            }
        )
    }

    private fun setIconPhoto(){
        if (Info.photos[0].isNotBlank()){
            Glide.with(context!!).load(Info.photos[0]).into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    iv_icon.setImageDrawable(resource)
                }
            })
        }
    }

    //Dialogs --------------------------------------------------------------------------------------

    private fun showCreateDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_group)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnCreate = dialog.findViewById(R.id.btn_create) as Button
        btnCreate.setOnClickListener {
            showCodeDialog("ADBSOE")
            dialog.dismiss()
        }
        dialog.show()
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
            Toast.makeText(context!!, "Text copied to clipboard", Toast.LENGTH_LONG).show()
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showJoinDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_join_group)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnJoin = dialog.findViewById(R.id.btn_join) as Button
        btnJoin.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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