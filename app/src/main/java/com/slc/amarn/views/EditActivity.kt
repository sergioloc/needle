package com.slc.amarn.views

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.slc.amarn.R
import com.slc.amarn.models.User
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.toolbar
import java.io.FileNotFoundException
import java.io.InputStream

class EditActivity : AppCompatActivity() {

    private var chipMen = false
    private var chipWomen = false
    private val RESULT_LOAD_IMG = 1
    private var NUM_PHOTOS = 0
    private var GENDER = 0
    private var user: User? = null
    lateinit var imgView: ArrayList<ImageView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        initVariables()
        initButtons()
    }

    private fun initVariables(){
        user = intent.getSerializableExtra("user") as User

        //Photos
        imgView = arrayListOf(iv_one, iv_two, iv_three, iv_four, iv_five, iv_six)
        NUM_PHOTOS = user?.photos!!.size
        for (i in 0 until NUM_PHOTOS) {
            imgView[i].setImageResource(user!!.photos[i])
            imgView[i].setPadding(0,0,0,0)
        }

        //Description
        et_description.text.insert(0, user?.description)

        //Orientation
        when (user?.orientation) {
            1 -> {
                btn_men.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
                chipMen = true
            }
            2 -> {
                btn_women.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
                chipWomen = true
            }
            3 -> {
                btn_men.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
                btn_women.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
                chipMen = true
                chipWomen = true
            }
        }
    }

    private fun initButtons(){
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        //Gender
        btn_man.setOnClickListener {
            checkGenderMan()
        }
        btn_woman.setOnClickListener {
            checkGenderWoman()
        }
        btn_other.setOnClickListener {
            showGenderDialog()
        }

        //Orientation
        btn_men.setOnClickListener {
            if (chipMen)
                btn_men.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
            else
                btn_men.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
            chipMen = !chipMen
        }
        btn_women.setOnClickListener {
            if (chipMen)
                btn_women.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
            else
                btn_women.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
            chipWomen = chipWomen
        }

        //Images
        for (i in imgView.indices) {
            imgView[i].setOnClickListener {
                if (i < NUM_PHOTOS)
                    deleteDialog()
                else
                    addPhoto()
            }
        }
    }

    private fun showGenderDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_gender)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnOk = dialog.findViewById(R.id.fab_ok) as FloatingActionButton
        val rbTransman = dialog.findViewById(R.id.rb_transman) as RadioButton
        val rbTranswoman = dialog.findViewById(R.id.rb_transwoman) as RadioButton
        btnOk.setOnClickListener {
            if (rbTransman.isChecked){
                checkGenderOther(3)
            }
            else if (rbTranswoman.isChecked){
                checkGenderOther(4)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun checkGenderMan(){
        GENDER = 1
        btn_man.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
        btn_woman.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_other.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
    }

    private fun checkGenderWoman(){
        GENDER = 2
        btn_man.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_woman.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
        btn_other.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
    }

    private fun checkGenderOther(gender: Int){
        GENDER = gender
        btn_man.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_woman.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_other.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
    }

    private fun addPhoto(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
    }

    private fun deleteDialog(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("Do you want to delete this picture?")
        alertDialog.setNegativeButton("No"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.setPositiveButton("Yes"
        ) { _, _ -> deletePhoto() }
        alertDialog.show()
    }

    private fun deletePhoto(){
        Toast.makeText(applicationContext, "Photo deleted", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                val imageUri: Uri? = data?.data
                val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                var imageView: ImageView? = null
                when(NUM_PHOTOS){
                    0 -> imageView = iv_one
                    1 -> imageView = iv_two
                    2 -> imageView = iv_three
                    3 -> imageView = iv_four
                    4 -> imageView = iv_five
                    5 -> imageView = iv_six
                }
                imageView?.setImageBitmap(selectedImage)
                imageView?.setPadding(0,0,0,0)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }


}