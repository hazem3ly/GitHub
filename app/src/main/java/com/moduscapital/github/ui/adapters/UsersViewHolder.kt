package com.moduscapital.github.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moduscapital.github.R
import com.moduscapital.github.data.network.response.Owner
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_layout_item.view.*

class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val owner_image = itemView.owner_image
    val repo_owner = itemView.repo_owner
    val image_loader = itemView.image_loader

    fun bind(item: Owner, onItemClicked: (repo: Owner) -> Unit) {
        itemView.setOnClickListener {
            onItemClicked(item)
        }

        Picasso.get().load(item.avatar_url).error(R.drawable.github_repo).fit()
                .into(owner_image, object : Callback {
                    override fun onError(e: Exception?) {
                        image_loader.visibility = View.GONE
                    }

                    override fun onSuccess() {
                        image_loader.visibility = View.GONE
                    }
                })

        repo_owner.text = item.login
    }
}