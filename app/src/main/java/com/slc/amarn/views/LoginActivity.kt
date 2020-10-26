package com.slc.amarn.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.slc.amarn.MainActivity
import com.slc.amarn.R
import com.slc.amarn.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_mail
import kotlinx.android.synthetic.main.activity_login.et_password
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel = LoginViewModel()
        initButtons()
        initObserves()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun initButtons(){
        tv_create.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btn_login.setOnClickListener {
            loginViewModel.signInWithMain(et_mail.text.toString(), et_password.text.toString())
        }

        btn_google.setOnClickListener {
            loginViewModel.getGoogleCredential(getString(R.string.default_web_client_id), this)
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

        loginViewModel.googleSignInClient.observe(this,
            Observer<Result<GoogleSignInClient>> {
                it.onSuccess { result ->
                    startActivityForResult(result.signInIntent, GOOGLE_SIGN_IN)
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task =  GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                loginViewModel.signInWithCredential(GoogleAuthProvider.getCredential(account?.idToken, null))
            }catch (e: ApiException){
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}