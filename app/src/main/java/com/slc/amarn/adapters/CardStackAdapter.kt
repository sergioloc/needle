package com.slc.amarn.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.slc.amarn.R
import com.slc.amarn.models.User
import com.slc.amarn.models.UserPreview
import com.slc.amarn.utils.Age
import kotlinx.android.synthetic.main.activity_user.*

class CardStackAdapter(
    private var users: List<UserPreview> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        val adapter = PhotoAdapter(holder.name.context, user.photos)
        holder.viewPager.adapter = adapter
        holder.indicator.setupWithViewPager(holder.viewPager)
        holder.name.text = "${user.name}, ${Age().getAge(user.dateOfBirth)}"
        holder.city.text = user.city
        holder.back.setOnClickListener {
            if (holder.viewPager.currentItem != 0) holder.viewPager.currentItem = holder.viewPager.currentItem - 1
        }

        holder.front.setOnClickListener {
            if (holder.viewPager.currentItem != users[position].photos.size) holder.viewPager.currentItem = holder.viewPager.currentItem + 1
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun getEmailList(): ArrayList<String>{
        var result = ArrayList<String>()
        for (u in users)
            result.add(u.email)
        return result
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewPager: ViewPager = view.findViewById(R.id.vp_photos)
        val indicator: TabLayout = view.findViewById(R.id.tab_indicator)
        val name: TextView = view.findViewById(R.id.item_name)
        var city: TextView = view.findViewById(R.id.item_city)
        var front: View = view.findViewById(R.id.btn_front)
        var back: View = view.findViewById(R.id.btn_back)
    }

}