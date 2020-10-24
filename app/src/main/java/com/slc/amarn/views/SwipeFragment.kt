package com.slc.amarn.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.slc.amarn.R
import com.slc.amarn.models.UserCallback
import com.slc.amarn.adapters.CardStackAdapter
import com.slc.amarn.models.User
import com.yuyakaido.android.cardstackview.*

class SwipeFragment : Fragment(), CardStackListener {

    lateinit var manager: CardStackLayoutManager
    lateinit var adapter: CardStackAdapter
    lateinit var cardStackView: CardStackView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_swipe, container, false)
        cardStackView = view.findViewById(R.id.card_stack_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager = CardStackLayoutManager(context, this)
        adapter = CardStackAdapter(createSpots())
        setupCardStackView()
        setupButton()
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun setupButton() {
        /*
        val skip = findViewById<View>(R.id.skip_button)
        skip.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        val rewind = findViewById<View>(R.id.rewind_button)
        rewind.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            cardStackView.rewind()
        }

        val like = findViewById<View>(R.id.like_button)
        like.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
        */
    }

    private fun initialize() {
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
        cardStackView.adapter = adapter

    }

    private fun paginate() {
        val old = adapter.getUsers()
        val new = old.plus(createSpots())
        val callback = UserCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setUsers(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun reload() {
        val old = adapter.getUsers()
        val new = createSpots()
        val callback = UserCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setUsers(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addFirst(size: Int) {
        val old = adapter.getUsers()
        val new = mutableListOf<User>().apply {
            addAll(old)
            for (i in 0 until size) {
                add(manager.topPosition, createSpot())
            }
        }
        val callback = UserCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setUsers(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addLast(size: Int) {
        val old = adapter.getUsers()
        val new = mutableListOf<User>().apply {
            addAll(old)
            addAll(List(size) { createSpot() })
        }
        val callback = UserCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setUsers(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeFirst(size: Int) {
        if (adapter.getUsers().isEmpty()) {
            return
        }

        val old = adapter.getUsers()
        val new = mutableListOf<User>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(manager.topPosition)
            }
        }
        val callback = UserCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setUsers(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeLast(size: Int) {
        if (adapter.getUsers().isEmpty()) {
            return
        }

        val old = adapter.getUsers()
        val new = mutableListOf<User>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(this.size - 1)
            }
        }
        val callback = UserCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setUsers(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun replace() {
        val old = adapter.getUsers()
        val new = mutableListOf<User>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, createSpot())
        }
        adapter.setUsers(new)
        adapter.notifyItemChanged(manager.topPosition)
    }

    private fun swap() {
        val old = adapter.getUsers()
        val new = mutableListOf<User>().apply {
            addAll(old)
            val first = removeAt(manager.topPosition)
            val last = removeAt(this.size - 1)
            add(manager.topPosition, last)
            add(first)
        }
        val callback = UserCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setUsers(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createSpot(): User {
        return User(1,"Sergio",24,"LBG Madrid", 3, 1, 2,"Hola", arrayListOf(R.drawable.bear, R.drawable.clouds), "sergioloc", "Sergio López", "696752807")
    }

    private fun createSpots(): List<User> {
        val spots = ArrayList<User>()
        spots.add(User(1,"Sergio",24,"LBG Madrid", 3, 1, 2,"Hola", arrayListOf(R.drawable.bear, R.drawable.clouds), "sergioloc", "Sergio López", "696752807"))
        spots.add(User(1,"Sergio",24,"LBG Madrid", 3, 1, 2,"Hola", arrayListOf(R.drawable.bear, R.drawable.clouds), "sergioloc", "Sergio López", "696752807"))
        return spots
    }

}