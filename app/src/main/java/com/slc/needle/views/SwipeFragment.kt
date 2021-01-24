package com.slc.needle.views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.LinearInterpolator
import com.slc.needle.R
import com.slc.needle.adapters.CardStackAdapter
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_swipe.*
import android.view.animation.AccelerateInterpolator
import android.widget.*
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.slc.needle.models.EmailGroup
import com.slc.needle.models.UserPreview
import com.slc.needle.utils.Info
import com.slc.needle.viewmodels.SwipeViewModel

class SwipeFragment : Fragment(), CardStackListener {

    lateinit var manager: CardStackLayoutManager
    lateinit var adapter: CardStackAdapter
    lateinit var cardStackView: CardStackView
    lateinit var swipeViewModel: SwipeViewModel
    private var emailList: ArrayList<EmailGroup> = ArrayList()
    private var position = 0
    private var firstTime = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_swipe, container, false)
        cardStackView = view.findViewById(R.id.card_stack_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeViewModel = SwipeViewModel()
        initVariables()
        initButtons()
        initObservers()
        swipeViewModel.getMyUserInfo()
    }

    override fun onResume() {
        super.onResume()
        if (!firstTime)
            swipeViewModel.getMyUserInfo()
        else
            firstTime = false

    }

    private fun initButtons() {
        fab_dislike.setOnClickListener {
            if (emailList.isNotEmpty()){
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager.setSwipeAnimationSetting(setting)
                cardStackView.swipe()
                swipeViewModel.swipeUser(emailList[position].email, emailList[position].group,false)
            }
        }

        fab_like.setOnClickListener {
            if (emailList.isNotEmpty()){
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager.setSwipeAnimationSetting(setting)
                cardStackView.swipe()
                swipeViewModel.swipeUser(emailList[position].email, emailList[position].group,true)
            }
        }
    }

    private fun initObservers(){
        swipeViewModel.getUser.observe(this,
            Observer<Result<Boolean>> {
                it.onSuccess {
                    loader.visibility = View.GONE
                    lock.visibility = View.GONE
                    profile.visibility = View.GONE
                    alone.visibility = View.GONE
                    done.visibility = View.GONE

                    if (!Info.user.visible){ // Hidden
                        lock.visibility = View.VISIBLE
                        tv_message.text = resources.getString(R.string.profile_hidden)
                        tv_message.visibility = View.VISIBLE
                    }
                    else if (Info.user.orientation == 0 || Info.user.gender == 0 || Info.user.dateOfBirth.isNullOrEmpty()){ // Incomplete profile
                        profile.visibility = View.VISIBLE
                        tv_message.text = resources.getString(R.string.profile_incomplete)
                        tv_message.visibility = View.VISIBLE
                    }
                    else if (Info.user.groups.isEmpty()) { // No groups
                        alone.visibility = View.VISIBLE
                        tv_message.text = resources.getString(R.string.no_group)
                        tv_message.visibility = View.VISIBLE
                    }
                    else{
                        loader.visibility = View.GONE
                        swipeViewModel.getMembers()
                    }
                }
                it.onFailure { result ->
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
        swipeViewModel.swipeList.observe(this,
            Observer<Result<ArrayList<UserPreview>>> {
                loader.visibility = View.GONE
                it.onSuccess {list ->
                    done.visibility = View.GONE
                    tv_message.visibility = View.GONE

                    adapter = CardStackAdapter(list)
                    position = 0
                    emailList = adapter.getEmailList()
                    cardStackView.adapter = adapter
                }
                it.onFailure {
                    upToDate()
                }
            }
        )

        swipeViewModel.match.observe(this,
            Observer<Result<Boolean>> {
                it.onSuccess {
                    showMatchDialog()
                }
                it.onFailure {

                }
            }
        )
    }

    private fun initVariables() {
        manager = CardStackLayoutManager(context, this)
        manager.setStackFrom(StackFrom.Top)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
    }

    private fun upToDate(){
        tv_message.text = resources.getString(R.string.up_to_date)
        tv_message.visibility = View.VISIBLE
        done.visibility = View.VISIBLE
    }

    //Dialogs --------------------------------------------------------------------------------------

    private fun showMatchDialog(){
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_match)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnOk = dialog.findViewById(R.id.btn_ok) as Button
        val name = dialog.findViewById(R.id.tv_name) as TextView
        val image = dialog.findViewById(R.id.iv_icon) as ImageView
        name.text = adapter.getName(position)
        Glide.with(context!!).load(adapter.getImage(position)).into(object : CustomTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                image.setImageDrawable(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    //Overrides ------------------------------------------------------------------------------------

    override fun onCardSwiped(direction: Direction) {
        if (direction == Direction.Right)
            swipeViewModel.swipeUser(emailList[position].email, emailList[position].group,true)
        else
            swipeViewModel.swipeUser(emailList[position].email, emailList[position].group,false)
        position++
        if (emailList.size == position) //no more users
            upToDate()

    }

    override fun onCardDisappeared(view: View?, position: Int) { }

    override fun onCardDragging(direction: Direction?, ratio: Float) { }

    override fun onCardCanceled() { }

    override fun onCardAppeared(view: View?, position: Int) { }

    override fun onCardRewound() { }

}