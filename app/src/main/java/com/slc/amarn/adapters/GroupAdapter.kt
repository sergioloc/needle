package com.slc.amarn.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.slc.amarn.R
import com.slc.amarn.models.GroupId

class GroupAdapter(private val groups: ArrayList<GroupId>): RecyclerView.Adapter<GroupAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int = groups.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val group = groups[position]
        holder.name?.text = group.name
        holder.id?.text = group.id
    }

    class UserViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.cell_group, parent, false)) {

        var name: TextView? = null
        var id: TextView? = null

        init {
            name = itemView.findViewById(R.id.tv_name)
            id = itemView.findViewById(R.id.tv_id)
        }
    }
}