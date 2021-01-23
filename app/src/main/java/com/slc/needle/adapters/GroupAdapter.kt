package com.slc.needle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.slc.needle.R
import com.slc.needle.models.GroupId
import com.slc.needle.utils.Info

class GroupAdapter(private val groups: ArrayList<GroupId>, private val onGroupClickListener: OnGroupClickListener): RecyclerView.Adapter<GroupAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int = groups.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val group = groups[position]
        holder.name?.text = group.name
        holder.id?.text = "ID: " + group.id
        //if (group.owner == Info.email)
        //    holder?.delete?.visibility = View.VISIBLE
        holder.copy?.setOnClickListener { onGroupClickListener.onCopyClick(group.id) }
        holder.leave?.setOnClickListener { onGroupClickListener.onLeaveClick(group.id) }
        holder.delete?.setOnClickListener { onGroupClickListener.onDeleteClick(group.id) }
    }

    class UserViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.cell_group, parent, false)) {

        var name: TextView? = null
        var id: TextView? = null
        var copy: ImageView? = null
        var leave: ImageView? = null
        var delete: ImageView? = null

        init {
            name = itemView.findViewById(R.id.tv_name)
            id = itemView.findViewById(R.id.tv_id)
            copy = itemView.findViewById(R.id.iv_copy)
            leave = itemView.findViewById(R.id.iv_leave)
            delete = itemView.findViewById(R.id.iv_delete)
        }
    }

    interface OnGroupClickListener {
        fun onCopyClick(id: String)
        fun onLeaveClick(id: String)
        fun onDeleteClick(id: String)
    }
}