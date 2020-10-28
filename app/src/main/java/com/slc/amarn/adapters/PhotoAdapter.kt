package com.slc.amarn.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.slc.amarn.R
import com.slc.amarn.utils.Info
import kotlinx.android.synthetic.main.activity_edit.*

class PhotoAdapter(var context: Context, var photos: List<String>): PagerAdapter() {

    lateinit var layoutInflater: LayoutInflater

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object` as LinearLayout

    override fun getCount(): Int = photos.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var imageView: ImageView
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = layoutInflater.inflate(R.layout.photo, container, false)
        imageView = view.findViewById(R.id.imageView)
        Glide.with(context).load(photos[position]).into(object : SimpleTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                imageView.setImageDrawable(resource)
            }
        })
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}