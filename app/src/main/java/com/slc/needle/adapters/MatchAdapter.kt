package com.slc.needle.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.slc.needle.R
import com.slc.needle.models.Match

class MatchAdapter(private val matches: ArrayList<Match>, private val onMatchClickListener: OnMatchClickListener): RecyclerView.Adapter<MatchAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.name?.text = matches[position].name
        holder.name?.text = matches[position].name
        holder.group?.text = matches[position].group
        if (matches[position].images.isNotEmpty()){
            Glide.with(holder.root?.context!!).load(matches[position].images[0]).into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    holder.icon?.setImageDrawable(resource)
                }
            })
        }
        holder.root?.setOnClickListener {
            onMatchClickListener.onMatchClick(matches[position])
        }
    }

    class UserViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.cell_match, parent, false)) {

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
        fun onMatchClick(m: Match)
    }
}