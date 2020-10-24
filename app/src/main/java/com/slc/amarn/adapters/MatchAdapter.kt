package com.slc.amarn.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.slc.amarn.R
import com.slc.amarn.models.User

class MatchAdapter(private val matches: ArrayList<User>): RecyclerView.Adapter<MatchAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(matches[position])
    }

    class UserViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_match, parent, false)) {

        private var icon: ImageView? = null
        private var name: TextView? = null

        init {
            icon = itemView.findViewById(R.id.iv_icon)
            name = itemView.findViewById(R.id.tv_name)
        }

        fun bind(user: User) {
            icon?.setImageResource(R.drawable.ic_heart)
            name?.text = user.name
        }
    }
}