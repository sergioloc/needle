package com.slc.needle.components

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.tabs.TabLayout
import com.slc.needle.R

class NavBar : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    companion object {
        const val PROFILE = 0
        const val SWIPE = 1
        const val MATCH = 2
    }

    private var fromLeft = true
    private val circleMove: View
    private val profileButton: ImageView
    private val swipeButton: ImageView
    private val matchButton: ImageView

    private val root: ConstraintLayout
    private val transition = AutoTransition()
    private var buttonPulsed = 0
    private var transitionDuration: Long = 200

    private val enableColor = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.white))
    private val disableColor = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.gray))
    private val paddingSelected = (13 * Resources.getSystem().displayMetrics.density).toInt()
    private val paddingNoSelected = (16 * Resources.getSystem().displayMetrics.density).toInt()

    init {
        inflate(this.context, R.layout.navbar, this) as ConstraintLayout
        root = findViewById(R.id.root_NavBar)
        circleMove = findViewById(R.id.circle_move_NavBar)
        profileButton = findViewById(R.id.profile_NavBar)
        swipeButton = findViewById(R.id.swipe_NavBar)
        matchButton = findViewById(R.id.match_NavBar)
        transition.duration = transitionDuration
    }

    fun setUpNavBarFrom(position: Int){
        when(position){
            PROFILE -> {
                fromLeft = true
                buttonPulsed = R.id.profile_NavBar
                addTransitionListener()
                updateStatusOval(buttonPulsed, false)
                setUpButtonStyle(profileButton, true)
                setUpButtonStyle(swipeButton, false)
                setUpButtonStyle(matchButton, false)
            }
            SWIPE -> {
                buttonPulsed = R.id.swipe_NavBar
                addTransitionListener()
                updateStatusOval(buttonPulsed, fromLeft)
                fromLeft = !fromLeft
                setUpButtonStyle(profileButton, false)
                setUpButtonStyle(swipeButton, true)
                setUpButtonStyle(matchButton, false)
            }
            MATCH -> {
                fromLeft = false
                buttonPulsed = R.id.match_NavBar
                addTransitionListener()
                updateStatusOval(buttonPulsed, true)
                setUpButtonStyle(profileButton, false)
                setUpButtonStyle(swipeButton, false)
                setUpButtonStyle(matchButton, true)
            }
        }
    }

    private fun setUpButtonStyle(button: ImageView, enabled: Boolean) {
        button.apply {
            if (enabled) {
                imageTintList = enableColor
                setPadding(paddingSelected, paddingSelected, paddingSelected, paddingSelected)
            } else {
                imageTintList = disableColor
                setPadding(paddingNoSelected, paddingNoSelected, paddingNoSelected, paddingNoSelected)
            }
        }
    }

    private fun addTransitionListener(){
        transition.addListener(object : Transition.TransitionListener{
            override fun onTransitionEnd(transition: Transition) {
                updateStatusOval(buttonPulsed, fromLeft)
                transition.removeListener(this)
            }
            override fun onTransitionResume(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionStart(transition: Transition) {}
        })
    }

    private fun updateStatusOval(toConnectId: Int, fromLeftButton: Boolean){
        when(fromLeftButton){
            true -> changeOvalConstraints(toConnectId, ConstraintSet.END)
            false -> changeOvalConstraints(toConnectId, ConstraintSet.START)
        }
    }

    private fun changeOvalConstraints(toConnectId: Int, side: Int){
        val constraintSet = ConstraintSet().apply {
            clone(root)
            clear(R.id.circle_move_NavBar, side)
            connect(R.id.circle_move_NavBar, side, toConnectId, side)
        }
        TransitionManager.beginDelayedTransition(root, transition)
        constraintSet.applyTo(root)
    }
}