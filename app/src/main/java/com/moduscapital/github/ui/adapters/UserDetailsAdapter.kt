package com.moduscapital.github.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moduscapital.github.R
import com.moduscapital.github.data.network.response.RepoDetails
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_layout.view.*

class UserDetailsAdapter(
    val onRepoClicked: (repo: RepoDetails) -> Unit
) : ListAdapter<RepoDetails, UserDetailsAdapter.ItemViewHolder>(TaskDiffCallback())
/* RecyclerView.Adapter<UserDetailsAdapter.ItemViewHolder>() */ {
//    val userRepos: ArrayList<RepoDetails> = arrayListOf()

    /* override fun getItemCount(): Int {
         return userRepos.size
     }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout,
                parent,
                false
            )
        )
    }
/*
    fun addList(list: List<RepoDetails>) {
        this.userRepos.clear()
        this.userRepos.addAll(list)
        notifyDataSetChanged()
    }*/

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position)) {
            onRepoClicked(it)
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ownerImage = itemView.owner_image
        private val repoDescription = itemView.repo_description
        private val forksCount = itemView.forks_count
        private val language = itemView.language
        private val creationDate = itemView.creation_date
        private val repoName = itemView.repo_name
        private val imageLoader = itemView.image_loader

        fun bind(item: RepoDetails, onItemClicked: (repo: RepoDetails) -> Unit) {
            itemView.setOnClickListener {
                onItemClicked(item)
            }

            Picasso.get().load(item.owner?.avatar_url).error(R.drawable.github_repo).fit()
                .into(ownerImage, object : Callback {
                    override fun onError(e: Exception?) {
                        imageLoader.visibility = View.GONE
                    }

                    override fun onSuccess() {
                        imageLoader.visibility = View.GONE
                    }
                })

            repoName.text = item.name
            repoDescription.text = item.description
            language.text = item.language
            creationDate.text = item.created_at
            forksCount.text = "${item.forks_count}"
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<RepoDetails>() {
        override fun areItemsTheSame(oldItem: RepoDetails, newItem: RepoDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RepoDetails, newItem: RepoDetails): Boolean {
            return oldItem == newItem
        }

    }

}