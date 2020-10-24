package com.slc.amarn.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.slc.amarn.R
import com.slc.amarn.models.User
import kotlinx.android.synthetic.main.activity_user.*

class CardStackAdapter(
    private var users: List<User> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    var images = arrayListOf(R.drawable.clouds, R.drawable.bear, R.drawable.clouds)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var adapter = PhotoAdapter(holder.name.context, images)
        holder.viewPager.adapter = adapter

        val user = users[position]
        holder.name.text = "${user.name}, ${user.age}"
        holder.lbg.text = user.lbg
        /*Glide.with(holder.image)
            .load(spot.url)
            .into(holder.image)*/
        holder.front.setOnClickListener {
            Toast.makeText(holder.front.context, "Front", Toast.LENGTH_SHORT).show()
        }
        holder.back.setOnClickListener {
            Toast.makeText(holder.front.context, "Back", Toast.LENGTH_SHORT).show()
        }

        holder.back.setOnClickListener {
            if (holder.viewPager.currentItem != 0) holder.viewPager.currentItem = holder.viewPager.currentItem - 1
        }

        holder.front.setOnClickListener {
            if (holder.viewPager.currentItem != images.size) holder.viewPager.currentItem = holder.viewPager.currentItem + 1
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun setUsers(spots: List<User>) {
        this.users = spots
    }

    fun getUsers(): List<User> {
        return users
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewPager: ViewPager = view.findViewById(R.id.vp_photos)
        val name: TextView = view.findViewById(R.id.item_name)
        var lbg: TextView = view.findViewById(R.id.item_lbg)
        var front: View = view.findViewById(R.id.btn_front)
        var back: View = view.findViewById(R.id.btn_back)
        //var image: ImageView = view.findViewById(R.id.item_image)
    }

}