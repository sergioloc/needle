package com.slc.needle.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slc.needle.MainActivity
import com.slc.needle.R
import com.slc.needle.adapters.WalkthroughAdapter
import com.slc.needle.models.Step
import kotlinx.android.synthetic.main.activity_walkthrough.*

class WalkthroughActivity : AppCompatActivity() {

    private val steps = listOf(
        Step(R.raw.complete, "Completa tu perfil", "Rellena los campos necesarios para poder usar Needle."),
        Step(R.raw.security, "Accede a tu parte privada", "Tan solo los usuarios con los que tengas match podrán ver esa información."),
        Step(R.raw.cube, "Crea o únete a un grupo", "Podrás pertenecer a tantos grupos como quieras."),
        Step(R.raw.swipe, "Desliza entre los miembros", "Tan solo podrás deslizar usuarios que pertenezcan a un grupo del que formas parte. Aquí surge la chispa.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)
        vp_walk.adapter = WalkthroughAdapter(applicationContext, steps, object :
            OnTextClickListener {
            override fun onSkipClick() {
                goToMainActivity()
            }
        })
        tab_indicator.setupWithViewPager(vp_walk)
    }

    fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    interface OnTextClickListener {
        fun onSkipClick()
    }

}