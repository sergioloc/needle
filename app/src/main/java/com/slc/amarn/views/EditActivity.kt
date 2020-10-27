package com.slc.amarn.views

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.slc.amarn.R
import com.slc.amarn.models.Photo
import com.slc.amarn.models.User
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.EditViewModel
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.photo.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.ClassCastException

class EditActivity: AppCompatActivity() {

    private var chipMen = false
    private var chipWomen = false
    private val RESULT_LOAD_IMG = 1
    private var GENDER = 0
    private var ORIENTATION = 0
    private var user: User? = null
    lateinit var editViewModel: EditViewModel
    lateinit var grayDrawable: Drawable
    private var newPhotoPosition = 0
    private var isImageOneEmpty = true
    private var isImageTwoEmpty = true
    private var isImageThreeEmpty = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        editViewModel = EditViewModel()
        initVariables()
        initButtons()
        initObservers()
    }

    private fun initVariables(){
        user = intent.getSerializableExtra("user") as User

        //Photos
        grayDrawable = ContextCompat.getDrawable(applicationContext, R.color.gray)!!
        if (Info.photos.isEmpty())
            editViewModel.getPhotosURL()
        else
            setPhotoInImageView()
        //Description
        et_description.text.insert(0, user?.description)

        //Gender
        when (user?.gender){
            1 -> setGenderMan()
            2 -> setGenderWoman()
            3 -> setGenderOther(user?.gender!!)
        }

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

        //Social media
        et_instagram.text.insert(0, user?.instagram)
        et_facebook.text.insert(0, user?.facebook)
        et_phone.text.insert(0, user?.phone)
    }

    private fun initButtons(){
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        btn_save.setOnClickListener {
            saveData()
        }

        //Gender
        btn_man.setOnClickListener {
            setGenderMan()
        }
        btn_woman.setOnClickListener {
            setGenderWoman()
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
            if (chipWomen)
                btn_women.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
            else
                btn_women.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
            chipWomen = !chipWomen
        }

        iv_one.setOnClickListener {
            if (isImageOneEmpty)
                addPhoto(0)
            else
                deletePhotoDialog(0)
        }
        iv_two.setOnClickListener {
            if (isImageTwoEmpty)
                addPhoto(1)
            else
                deletePhotoDialog(1)
        }
        iv_three.setOnClickListener {
            if (isImageThreeEmpty)
                addPhoto(2)
            else
                deletePhotoDialog(2)
        }
    }

    private fun initObservers(){
        editViewModel.photoState.observe(this,
            Observer<Result<Int>> {
                it.onSuccess {result ->
                    when (result){
                        0 -> {
                            iv_one.setImageDrawable(grayDrawable)
                            Info.photos.removeAt(0)
                            isImageOneEmpty = true
                        }
                        1 -> {
                            iv_two.setImageDrawable(grayDrawable)
                            Info.photos.removeAt(1)
                            isImageTwoEmpty = true
                        }
                        2 -> {
                            iv_three.setImageDrawable(grayDrawable)
                            Info.photos.removeAt(2)
                            isImageThreeEmpty = true
                        }
                    }
                }
            }
        )
        editViewModel.drawables.observe(this,
            Observer<Result<Boolean>> {
                it.onSuccess {
                    setPhotoInImageView()
                }
            }
        )
    }

    private fun setPhotoInImageView() {
        for (i in 0 until Info.photos.size){
            var imageView = getImageView(Info.photos[i].path)
            Glide.with(applicationContext).load(Info.photos[i].url).into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(resource: Drawable,transition: Transition<in Drawable?>?) {
                    imageView.setImageDrawable(resource)
                }
            })
        }
    }

    private fun getImageView(path: String): ImageView{
        when (path.substring(path.length-5,path.length)){
            "1.jpg" -> {
                isImageOneEmpty = false
                return iv_one
            }
            "2.jpg" -> {
                isImageTwoEmpty = false
                return iv_two
            }
            "3.jpg" -> {
                isImageThreeEmpty = false
                return iv_three
            }
        }
        return iv_one
    }


    private fun saveData(){
        user?.description = et_description.text.toString()
        user?.gender = GENDER
        ORIENTATION = if (chipMen && chipWomen)
            3
        else if (chipWomen)
            2
        else if (chipMen)
            1
        else
            0
        user?.orientation = ORIENTATION
        user?.instagram = et_instagram.text.toString()
        user?.facebook = et_facebook.text.toString()
        user?.phone = et_phone.text.toString()
        editViewModel.saveChanges(user!!)
    }

    // Buttons -------------------------------------------------------------------------------------

    private fun setGenderMan(){
        GENDER = 1
        btn_man.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
        btn_woman.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_other.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
    }

    private fun setGenderWoman(){
        GENDER = 2
        btn_man.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_woman.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
        btn_other.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
    }

    private fun setGenderOther(gender: Int){
        GENDER = gender
        btn_man.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_woman.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_other.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
    }

    private fun addPhoto(i: Int){
        newPhotoPosition = i
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
    }

    // Dialogs -------------------------------------------------------------------------------------

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
                setGenderOther(3)
            }
            else if (rbTranswoman.isChecked){
                setGenderOther(4)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deletePhotoDialog(i: Int){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("Do you want to delete this picture?")
        alertDialog.setNegativeButton("No"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.setPositiveButton("Yes"
        ) { _, _ -> editViewModel.deletePhoto(i) }
        alertDialog.show()
    }


    // Overrides -----------------------------------------------------------------------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                val imageUri: Uri? = data?.data
                val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                var imageView: ImageView? = null
                when(newPhotoPosition){
                    0 -> imageView = iv_one
                    1 -> imageView = iv_two
                    2 -> imageView = iv_three
                }
                imageView?.setImageBitmap(selectedImage)
                editViewModel.uploadPhoto(selectedImage, newPhotoPosition+1)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }
}