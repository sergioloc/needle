package com.slc.amarn.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.slc.amarn.R
import com.slc.amarn.models.User

class MatchAdapter(private val matches: ArrayList<User>, private val onMatchClickListener: OnMatchClickListener): RecyclerView.Adapter<MatchAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = matches[position]
        holder.icon?.setImageResource(R.color.black)
        holder.name?.text = user.name
        holder.root?.setOnClickListener {
            onMatchClickListener.onMatchClick(user)
        }
    }

    class UserViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_match, parent, false)) {

        var root: ConstraintLayout? = null
        var icon: ImageView? = null
        var name: TextView? = null

        init {
            root = itemView.findViewById(R.id.root)
            icon = itemView.findViewById(R.id.iv_icon)
            name = itemView.findViewById(R.id.tv_name)
        }
    }

    interface OnMatchClickListener {
        fun onMatchClick(user: User)
    }
}