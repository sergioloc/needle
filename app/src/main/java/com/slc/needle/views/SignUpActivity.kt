package com.slc.needle.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.slc.needle.R
import com.slc.needle.viewmodels.SignUpViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_dateOfBirth

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        signUpViewModel = SignUpViewModel()
        initListeners()
        initObserves()
    }

    private fun initListeners(){
        btn_signup.setOnClickListener {
            signUpViewModel.createUser(et_mail.text.toString(), et_password.text.toString(), et_confirm.text.toString(), et_name.text.toString(), et_dateOfBirth.text.toString())
        }
        et_dateOfBirth.setOnClickListener {
            val date = signUpViewModel.getCurrentDate()
            showCalendar(date.day, date.month, date.year)
        }
        et_dateOfBirth.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val date = signUpViewModel.getCurrentDate()
                showCalendar(date.day, date.month, date.year)
            }
        }
    }

    private fun initObserves() {
        signUpViewModel.state.observe(this,
            Observer<Result<Boolean>> {
                if (it.isSuccess){
                    showVerifyDialog()
                }
                else {
                    it.onFailure { result ->
                        Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    private fun showCalendar(day: Int, month: Int, year: Int) {
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, newYear, newMonth, newDay ->
            et_dateOfBirth.text.clear()
            et_dateOfBirth.text.insert(0, "$newDay-${newMonth+1}-$newYear")
        }, year, month, day).show()
    }

    private fun showVerifyDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_verify)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnOk = dialog.findViewById(R.id.btn_ok) as Button
        btnOk.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}