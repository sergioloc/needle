package com.slc.needle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.airbnb.lottie.LottieAnimationView
import com.slc.needle.R
import com.slc.needle.models.Tip
import com.slc.needle.views.WalkthroughActivity

class WalkthroughAdapter(val context: Context, private val tips: List<Tip>, private val listener: WalkthroughActivity.OnTextClickListener): PagerAdapter() {

    lateinit var layoutInflater: LayoutInflater

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object` as ConstraintLayout

    override fun getCount(): Int = tips.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.item_walkthought, container, false)

        val icon: LottieAnimationView = view.findViewById(R.id.iv_icon)
        val title: TextView = view.findViewById(R.id.tv_title)
        val description: TextView = view.findViewById(R.id.tv_desciption)
        val skip: TextView = view.findViewById(R.id.tv_skip)

        icon.setAnimation(tips[position].photo)
        title.text = tips[position].title
        description.text = tips[position].description
        if (position == count-1)
            skip.text = "Done"
        else
            skip.text = "Skip"
        skip.setOnClickListener {
            listener.onSkipClick()
        }


        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}