package com.slc.amarn.views

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.slc.amarn.MainActivity
import com.slc.amarn.R
import com.slc.amarn.viewmodels.InfoViewModel
import kotlinx.android.synthetic.main.activity_info.*


class InfoActivity : AppCompatActivity() {

    lateinit var infoViewModel: InfoViewModel
    private var day = 0
    private var month = 0
    private var year = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        infoViewModel = InfoViewModel()
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        et_birthday.setOnClickListener {
            val date = infoViewModel.getCurrentDate()
            showCalendar(date.day, date.month, date.year)
        }
        
        et_birthday.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val date = infoViewModel.getCurrentDate()
                showCalendar(date.day, date.month, date.year)
            }
        }

        btn_continue.setOnClickListener {
            infoViewModel.saveData(et_name.text.toString(), day, month, year)
        }
    }

    private fun initObservers(){
        infoViewModel.state.observe(this,
            Observer<Result<Boolean>> {
                it.onSuccess {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                it.onFailure { result ->
                    Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun showCalendar(day: Int, month: Int, year: Int) {
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, newYear, newMonth, newDay ->
            et_birthday.text.clear()
            et_birthday.text.insert(0, "$newDay-${newMonth+1}-$newYear")
            this.day = newDay
            this.month = newMonth
            this.year = newYear
        }, year, month, day).show()
    }
}