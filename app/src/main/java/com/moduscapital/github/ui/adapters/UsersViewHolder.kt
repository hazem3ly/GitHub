package com.moduscapital.github.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moduscapital.github.R
import com.moduscapital.github.data.network.response.Owner
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_layout_item.view.*

class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ownerImage = itemView.owner_image
    private val repoOwner = itemView.repo_owner
    private val imageLoader = itemView.image_loader

    fun bind(item: Owner, onItemClicked: (repo: Owner) -> Unit) {
        itemView.setOnClickListener {
            onItemClicked(item)
        }

        Picasso.get().load(item.avatar_url).error(R.drawable.github_repo).fit()
            .into(ownerImage, object : Callback {
                    override fun onError(e: Exception?) {
                        imageLoader.visibility = View.GONE
                    }

                    override fun onSuccess() {
                        imageLoader.visibility = View.GONE
                    }
                })

        repoOwner.text = item.login
    }
}