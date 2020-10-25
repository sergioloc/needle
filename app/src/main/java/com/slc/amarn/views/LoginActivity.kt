package com.slc.amarn.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.slc.amarn.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initButtons()
    }

    private fun initButtons(){
        tv_create.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}