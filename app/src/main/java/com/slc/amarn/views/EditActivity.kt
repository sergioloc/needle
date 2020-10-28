package com.slc.amarn.views

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
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
import com.slc.amarn.models.User
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.EditViewModel
import kotlinx.android.synthetic.main.activity_edit.*
import java.io.FileNotFoundException
import java.io.InputStream

class EditActivity: AppCompatActivity() {

    lateinit var editViewModel: EditViewModel
    //Data
    private var chipMen = false
    private var chipWomen = false
    private val RESULT_LOAD_IMG = 1
    private var user: User? = null
    private var initUser: User? = null
    //Photos
    lateinit var grayDrawable: Drawable
    private var newPhotoPosition = 0
    private var isImageOneEmpty = true
    private var isImageTwoEmpty = true
    private var isImageThreeEmpty = true
    private var loaders: ArrayList<ProgressBar>? = null

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
        user = Info.user
        initUser = user?.copy()

        //Photos
        loaders = arrayListOf(loader1, loader2, loader3)
        grayDrawable = ContextCompat.getDrawable(applicationContext, R.color.gray)!!
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
            if (chipMen){
                btn_men.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
                if (chipWomen)
                    user?.orientation = 3   //men yes, women yes
                else
                    user?.orientation = 1   //men yes, women no
            }
            else{
                btn_men.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
                if (chipWomen)
                    user?.orientation = 2   //men no, women yes
                else
                    user?.orientation = 0   //men no, women no
            }
            chipMen = !chipMen
        }
        btn_women.setOnClickListener {
            if (chipWomen){
                btn_women.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
                if (chipMen)
                    user?.orientation = 3   //women yes, man yes
                else
                    user?.orientation = 2   //women yes, man no
            }
            else{
                btn_women.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
                if (chipMen)
                    user?.orientation = 1   //women no, man yes
                else
                    user?.orientation = 0   //women no, man no
            }
            chipWomen = !chipWomen
        }

        //Fields
        et_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                user?.description = et_description.text.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
        et_instagram.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                user?.instagram = et_instagram.text.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
        et_facebook.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                user?.facebook = et_facebook.text.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
        et_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                user?.phone = et_phone.text.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        //Photos
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
        editViewModel.uploadPhoto.observe(this,
            Observer<Result<Boolean>> {
                loaders?.get(newPhotoPosition)?.visibility = View.GONE
                it.onSuccess {
                    Info.reloadPhotos = true
                }
                it.onFailure {
                    Toast.makeText(applicationContext, "No se ha podido guardar la foto en nuestros servidores", Toast.LENGTH_SHORT).show()
                    clearImageView(newPhotoPosition)
                }
            }
        )
        editViewModel.deletePhoto.observe(this,
            Observer<Result<Int>> {
                it.onSuccess {result ->
                    loaders?.get(result)?.visibility = View.GONE
                    Info.reloadPhotos = true
                    clearImageView(result)
                }
            }
        )
    }

    private fun clearImageView(position: Int){
        when (position){
            0 -> {
                iv_one.setImageDrawable(grayDrawable)
                isImageOneEmpty = true

            }
            1 -> {
                iv_two.setImageDrawable(grayDrawable)
                isImageTwoEmpty = true
            }
            2 -> {
                iv_three.setImageDrawable(grayDrawable)
                isImageThreeEmpty = true
            }
        }
    }

    private fun setPhotoInImageView() {
        for (i in 0 until Info.photos.size){
            val imageView = getImageView(Info.photos[i])
            Glide.with(applicationContext).load(Info.photos[i]).into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(resource: Drawable,transition: Transition<in Drawable?>?) {
                    imageView.setImageDrawable(resource)
                    when (imageView) {
                        iv_one -> loaders?.get(0)?.visibility = View.GONE
                        iv_two -> loaders?.get(1)?.visibility = View.GONE
                        iv_three -> loaders?.get(2)?.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun getImageView(url: String): ImageView{
        return when {
            "1.jpg" in url -> {
                loaders?.get(0)?.visibility = View.VISIBLE
                isImageOneEmpty = false
                iv_one
            }
            "2.jpg" in url -> {
                loaders?.get(1)?.visibility = View.VISIBLE
                isImageTwoEmpty = false
                iv_two
            }
            else -> {
                loaders?.get(2)?.visibility = View.VISIBLE
                isImageThreeEmpty = false
                iv_three
            }
        }
    }


    private fun saveData(){
        user?.description = et_description.text.toString()
        user?.instagram = et_instagram.text.toString()
        user?.facebook = et_facebook.text.toString()
        user?.phone = et_phone.text.toString()
        editViewModel.saveChanges(user!!)
    }

    // Buttons -------------------------------------------------------------------------------------

    private fun setGenderMan(){
        user?.gender = 1
        btn_man.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
        btn_woman.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_other.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
    }

    private fun setGenderWoman(){
        user?.gender = 2
        btn_man.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
        btn_woman.background = ContextCompat.getDrawable(this, R.drawable.chip_accent)
        btn_other.background = ContextCompat.getDrawable(this, R.drawable.chip_white)
    }

    private fun setGenderOther(gender: Int){
        user?.gender = gender
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
        alertDialog.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        alertDialog.setPositiveButton("Yes"){ _, _ ->
            loaders?.get(i)?.visibility = View.VISIBLE
            editViewModel.deletePhoto(i)
        }
        alertDialog.show()
    }


    // Overrides -----------------------------------------------------------------------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                loaders?.get(newPhotoPosition)?.visibility = View.VISIBLE
                val imageUri: Uri? = data?.data
                val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                var imageView: ImageView? = null
                when(newPhotoPosition){
                    0 -> {
                        imageView = iv_one
                        isImageOneEmpty = false
                    }
                    1 -> {
                        imageView = iv_two
                        isImageTwoEmpty = false
                    }
                    2 -> {
                        imageView = iv_three
                        isImageThreeEmpty = false
                    }
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (initUser != user)
            saveData()
    }
}