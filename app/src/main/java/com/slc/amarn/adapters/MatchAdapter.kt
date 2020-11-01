package com.slc.amarn.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.slc.amarn.R
import com.slc.amarn.models.Match
import com.slc.amarn.models.User
import com.slc.amarn.models.UserMatch

class MatchAdapter(private val matches: ArrayList<UserMatch>, private val onMatchClickListener: OnMatchClickListener): RecyclerView.Adapter<MatchAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.icon?.setImageResource(R.color.black)
        holder.name?.text = matches[position].name
        holder.name?.text = matches[position].name
        holder.group?.text = matches[position].group
        holder.root?.setOnClickListener {
            onMatchClickListener.onMatchClick(matches[position])
        }
    }

    class UserViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_match, parent, false)) {

        var root: ConstraintLayout? = null
        var icon: ImageView? = null
        var name: TextView? = null
        var group: Button? = null

        init {
            root = itemView.findViewById(R.id.root)
            icon = itemView.findViewById(R.id.iv_icon)
            name = itemView.findViewById(R.id.tv_name)
            group = itemView.findViewById(R.id.chip_group)
        }
    }

    interface OnMatchClickListener {
        fun onMatchClick(user: UserMatch)
    }
}