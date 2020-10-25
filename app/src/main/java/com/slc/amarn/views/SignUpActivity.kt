package com.slc.amarn.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.slc.amarn.MainActivity
import com.slc.amarn.R
import com.slc.amarn.viewmodels.SignUpViewModel
import kotlinx.android.synthetic.main.activity_register.*

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
            signUpViewModel.createUser(et_mail.text.toString(), et_password.text.toString(), et_confirm.text.toString())
        }
    }

    private fun initObserves() {
        signUpViewModel.state.observe(this,
            Observer<Result<Boolean>> {
                if (it.isSuccess){
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else {
                    it.onFailure { result ->
                        Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}