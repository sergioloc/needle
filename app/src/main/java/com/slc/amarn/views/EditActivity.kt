package com.slc.amarn.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.slc.amarn.R
import kotlinx.android.synthetic.main.activity_edit.*
import java.io.FileNotFoundException
import java.io.InputStream


class EditActivity : AppCompatActivity() {

    private var chipMan = false
    private var chipWoman = false
    private val RESULT_LOAD_IMG = 1
    private var NUM_PHOTOS = 1


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

        iv_one.setOnClickListener {
            if (NUM_PHOTOS == 1)
                deleteDialog()
            else
                addPhoto()
        }

        iv_two.setOnClickListener {
            if (NUM_PHOTOS == 2)
                deleteDialog()
            else
                addPhoto()
        }

        iv_three.setOnClickListener {
            if (NUM_PHOTOS == 3)
                deleteDialog()
            else
                addPhoto()
        }

        iv_four.setOnClickListener {
            if (NUM_PHOTOS == 4)
                deleteDialog()
            else
                addPhoto()
        }

        iv_five.setOnClickListener {
            if (NUM_PHOTOS == 5)
                deleteDialog()
            else
                addPhoto()
        }

        iv_six.setOnClickListener {
            if (NUM_PHOTOS == 6)
                deleteDialog()
            else
                addPhoto()
        }
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