package com.moduscapital.github.ui.fragments.userdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.moduscapital.github.R
import com.moduscapital.github.data.network.response.RepoDetails
import com.moduscapital.github.extensions.loadSavedUser
import com.moduscapital.github.extensions.saveToSP
import com.moduscapital.github.ui.activities.MainActivity
import com.moduscapital.github.ui.adapters.PaginationScrollListener
import com.moduscapital.github.ui.adapters.UserDetailsAdapter
import com.moduscapital.github.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_details_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class UserDetailsFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: UserDetailsViewModelFactory by instance()
    private lateinit var viewModel: UserDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_details_fragment, container, false)
    }

    var userName: String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UserDetailsViewModel::class.java)

        val safeArgs = arguments?.let { UserDetailsFragmentArgs.fromBundle(it) }

        userName = safeArgs?.userName ?: ""

        if (userName != "") {
            val user = requireContext().loadSavedUser()
            if (user != userName) {
                deleteOldRepos()
            }
            (activity as? MainActivity)?.toolbar?.title = userName
            userName.saveToSP(requireContext())
        }

        initRecycler()
        getUserRepos(userName)


    }

    private fun deleteOldRepos() = launch {
        viewModel.deleteOldData().await()
    }


    private fun getUserRepos(userName: String) = launch {
        progress.visibility = View.VISIBLE
        val userRepos = viewModel.getUserRepos(userName).await()
        userRepos.observe(this@UserDetailsFragment.viewLifecycleOwner, Observer {
            progress.visibility = View.GONE
            if (it != null) {
                it.let { it1 ->
                    updateList(it1)
                    isLoading = false
                }
            } else {
                Toast.makeText(
                    this@UserDetailsFragment.requireContext(),
                    "Error Loading Repos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateList(list: List<RepoDetails>) {
        adapter?.submitList(list)
    }

    var adapter: UserDetailsAdapter? = null
    var isLoading: Boolean = false

    private fun initRecycler() {

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = UserDetailsAdapter {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.html_url))
            startActivity(browserIntent)
        }
        user_recycler?.setHasFixedSize(true)
        user_recycler?.layoutManager = layoutManager
        user_recycler?.adapter = adapter
        user_recycler?.addOnScrollListener(object :
            PaginationScrollListener(layoutManager) {
            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }
        })

    }

    private var page = 1

    private fun getMoreItems() = launch {
        viewModel.getUserRepos(userName, ++page).await()
    }

}
