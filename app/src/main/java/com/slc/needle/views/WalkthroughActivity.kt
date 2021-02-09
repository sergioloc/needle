package com.slc.needle.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.slc.needle.R
import com.slc.needle.adapters.PhotoAdapter
import com.slc.needle.adapters.WalkthroughAdapter
import com.slc.needle.models.Step
import kotlinx.android.synthetic.main.activity_walkthrough.*

class WalkthroughActivity : AppCompatActivity() {

    val steps = listOf(
        Step(R.drawable.ic_circle, "Paso 1", "D1"),
        Step(R.drawable.ic_circle, "Paso 2", "D2"),
        Step(R.drawable.ic_circle, "Paso 3", "D3")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)
        vp_walk.adapter = WalkthroughAdapter(applicationContext, steps)
    }
}