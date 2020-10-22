package com.slc.amarn.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.slc.amarn.R
import com.slc.amarn.models.User

class CardStackAdapter(
    private var users: List<User> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.name.text = "${user.name}, ${user.age}"
        holder.lbg.text = user.lbg
        /*Glide.with(holder.image)
            .load(spot.url)
            .into(holder.image)*/
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
        val name: TextView = view.findViewById(R.id.item_name)
        var lbg: TextView = view.findViewById(R.id.item_lbg)
        //var image: ImageView = view.findViewById(R.id.item_image)
    }

}