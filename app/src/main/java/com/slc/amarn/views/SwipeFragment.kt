package com.slc.amarn.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.slc.amarn.R
import com.slc.amarn.adapters.CardStackAdapter
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_swipe.*
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.lifecycle.Observer
import com.slc.amarn.models.User
import com.slc.amarn.models.UserPreview
import com.slc.amarn.utils.Info
import com.slc.amarn.viewmodels.SwipeViewModel

class SwipeFragment : Fragment(), CardStackListener {

    lateinit var manager: CardStackLayoutManager
    lateinit var adapter: CardStackAdapter
    lateinit var cardStackView: CardStackView
    lateinit var swipeViewModel: SwipeViewModel
    lateinit var emailList: ArrayList<String>
    private var position = 0

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

    private fun initButtons() {
        fab_dislike.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Slow.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
            adapter
            swipeViewModel.swipeUser(emailList[position], true)
        }

        fab_like.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Slow.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
            swipeViewModel.swipeUser(emailList[position], true)
        }
    }

    private fun initObservers(){
        swipeViewModel.user.observe(this,
            Observer<Result<User>> {
                it.onSuccess {
                    swipeViewModel.getUsers(Info.user.groups)
                }
                it.onFailure { result ->
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
        swipeViewModel.userList.observe(this,
            Observer<Result<ArrayList<UserPreview>>> {
                it.onSuccess {list ->
                    adapter = CardStackAdapter(list)
                    position = 0
                    emailList = adapter.getEmailList()
                    cardStackView.adapter = adapter
                    loader.visibility = View.GONE
                }
                it.onFailure { result ->
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun initVariables() {
        manager = CardStackLayoutManager(context, this)
        manager.setStackFrom(StackFrom.None)
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

    //Overrides -----------------------------------------------------------------------------------

    override fun onCardSwiped(direction: Direction) {
        if (direction == Direction.Right)
            swipeViewModel.swipeUser(emailList[position], true)
        else
            swipeViewModel.swipeUser(emailList[position], false)
        position++
    }

    override fun onCardDisappeared(view: View?, position: Int) { }

    override fun onCardDragging(direction: Direction?, ratio: Float) { }

    override fun onCardCanceled() { }

    override fun onCardAppeared(view: View?, position: Int) { }

    override fun onCardRewound() { }

}