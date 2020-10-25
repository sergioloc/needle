package com.slc.amarn.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.slc.amarn.MainActivity
import com.slc.amarn.R
import com.slc.amarn.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_mail
import kotlinx.android.synthetic.main.activity_login.et_password
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel = LoginViewModel()
        initButtons()
        initObserves()
    }

    override fun onBackPressed() { }

    private fun initButtons(){
        tv_create.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btn_login.setOnClickListener {
            loginViewModel.signIn(et_mail.text.toString(), et_password.text.toString())
        }
    }

    private fun initObserves() {
        loginViewModel.state.observe(this,
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
}