package com.slc.amarn.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.slc.amarn.R

class CardGroup @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private val ivIcon: FloatingActionButton
    private val tvTitle: TextView

    init {
        View.inflate(this.context, R.layout.card_group, this) as ConstraintLayout

        ivIcon = findViewById(R.id.fab_icon)
        tvTitle = findViewById(R.id.tv_title)

        attributeSet?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CardGroup,0,0)
            val icon = typedArray.getDrawable(R.styleable.CardGroup_iconCardGroup)
            val title = typedArray.getString(R.styleable.CardGroup_titleCardGroup)

            ivIcon.setImageDrawable(icon)
            tvTitle.text = title
        }
    }
}