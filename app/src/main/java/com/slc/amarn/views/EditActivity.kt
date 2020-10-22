package com.slc.amarn.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.slc.amarn.R
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    private var chipMan = false
    private var chipWoman = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        initButtons()
    }

    private fun initButtons(){
        btn_men.setOnClickListener {
            if (chipMan)
                btn_men.background = resources.getDrawable(R.drawable.chip_white)
            else
                btn_men.background = resources.getDrawable(R.drawable.chip_accent)
            chipMan = !chipMan
        }

        btn_women.setOnClickListener {
            if (chipWoman)
                btn_women.background = resources.getDrawable(R.drawable.chip_white)
            else
                btn_women.background = resources.getDrawable(R.drawable.chip_accent)
            chipWoman = !chipWoman
        }
    }


}