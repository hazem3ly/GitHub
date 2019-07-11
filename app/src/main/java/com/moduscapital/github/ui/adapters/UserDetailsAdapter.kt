package com.moduscapital.github.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moduscapital.github.R
import com.moduscapital.github.data.network.response.RepoDetails
import com.moduscapital.github.data.network.response.UserDetails
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.header_layout.view.*
import kotlinx.android.synthetic.main.item_layout.view.*

class UserDetailsAdapter(
        val userRepos: ArrayList<RepoDetails>,
        val onRepoClicked: (repo: RepoDetails) -> Unit
) :
        RecyclerView.Adapter<UserDetailsAdapter.ItemViewHolder>() {

    override fun getItemCount(): Int {
        return userRepos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            return ItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_layout,
                            parent,
                            false
                    )
            )
    }

    fun addList(list: List<RepoDetails>) {
        this.userRepos.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(userRepos[position]) {
            onRepoClicked(it)
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val owner_image = itemView.owner_image
        val repo_description = itemView.repo_description
        val forks_count = itemView.forks_count
        val language = itemView.language
        val creation_date = itemView.creation_date
        val repo_name = itemView.repo_name
        val image_loader = itemView.image_loader

        fun bind(item: RepoDetails, onItemClicked: (repo: RepoDetails) -> Unit) {
            itemView.setOnClickListener {
                onItemClicked(item)
            }

            Picasso.get().load(item.owner?.avatar_url).error(R.drawable.github_repo).fit()
                    .into(owner_image, object : Callback {
                        override fun onError(e: Exception?) {
                            image_loader.visibility = View.GONE
                        }

                        override fun onSuccess() {
                            image_loader.visibility = View.GONE
                        }
                    })

            repo_name.text = item.name
            repo_description.text = item.description
            language.text = item.language
            creation_date.text = item.created_at
            forks_count.text = "${item.forks_count}"
        }
    }


}