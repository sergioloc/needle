package com.slc.amarn.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.slc.amarn.R
import com.slc.amarn.models.EmailGroup
import com.slc.amarn.models.UserPreview
import com.slc.amarn.utils.Age

class CardStackAdapter(
    private var users: List<UserPreview> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        val adapter = PhotoAdapter(holder.name.context, user.images)
        holder.viewPager.adapter = adapter
        holder.indicator.setupWithViewPager(holder.viewPager)
        holder.name.text = "${user.name}, ${Age().getAge(user.dateOfBirth)}"
        holder.city.text = user.city
        holder.group.text = user.group
        holder.back.setOnClickListener {
            if (holder.viewPager.currentItem != 0) holder.viewPager.currentItem = holder.viewPager.currentItem - 1
        }

        holder.front.setOnClickListener {
            if (holder.viewPager.currentItem != users[position].images.size) holder.viewPager.currentItem = holder.viewPager.currentItem + 1
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun getEmailList(): ArrayList<EmailGroup>{
        var result = ArrayList<EmailGroup>()
        for (u in users)
            result.add(EmailGroup(u.email, u.group))
        return result
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewPager: ViewPager = view.findViewById(R.id.vp_photos)
        val indicator: TabLayout = view.findViewById(R.id.tab_indicator)
        val name: TextView = view.findViewById(R.id.item_name)
        var city: TextView = view.findViewById(R.id.item_city)
        var group: Button = view.findViewById(R.id.chip_group)
        var front: View = view.findViewById(R.id.btn_front)
        var back: View = view.findViewById(R.id.btn_back)
    }

}