package com.moduscapital.github.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moduscapital.github.R
import com.moduscapital.github.data.network.response.Owner

class UsersAdapter(
        val onItemClicked: (repo: Owner) -> Unit
) :
        RecyclerView.Adapter<UsersViewHolder>() {

    private var list: ArrayList<Owner> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_layout_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(list[position]) {
            onItemClicked(it)
        }
    }

    fun getLastItemId(): Int {
        return list?.last().id ?: 0
    }

    fun addList(list: List<Owner>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun removeItems() {
        this.list.clear()
        notifyDataSetChanged()
    }
}