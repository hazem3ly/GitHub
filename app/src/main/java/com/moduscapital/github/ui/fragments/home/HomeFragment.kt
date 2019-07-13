package com.moduscapital.github.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moduscapital.github.R
import com.moduscapital.github.data.network.response.Owner
import com.moduscapital.github.ui.adapters.PaginationScrollListener
import com.moduscapital.github.ui.adapters.UsersAdapter
import com.moduscapital.github.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class HomeFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private lateinit var viewModel: HomeViewModel
    private val viewModelFactory: HomeViewModelFactory by instance()

    lateinit var usersAdapter: UsersAdapter
    var isLoading: Boolean = false

    private var searchView: SearchView? = null

    private var isSearching: Boolean = false
    private var searchQuery: String = ""
    private var page = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.home_fragment, container, false)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)

        val searchViewItem = menu?.findItem(R.id.search)

        searchView = searchViewItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView?.clearFocus()
                usersAdapter.removeItems()
                searchUsers(query)
                isSearching = true
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchView?.setOnCloseListener {
            progress.visibility = View.VISIBLE
            usersAdapter.removeItems()
            getMoreUsers(0)
            isSearching = false
            return@setOnCloseListener false
        }
        super.onCreateOptionsMenu(menu, inflater)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        initRecycler()

        getUsers()

    }

    private fun initRecycler() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        usersAdapter = UsersAdapter {
            openUserDetails(it)
        }
        users_recycler?.setHasFixedSize(true)
        users_recycler?.layoutManager = layoutManager
        users_recycler?.adapter = usersAdapter
        users_recycler?.addOnScrollListener(object :
            PaginationScrollListener(layoutManager) {
            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                Log.e("loading more ->", "getting more users")
                isLoading = true
                load_more_progress.visibility = View.VISIBLE
                if (!isSearching)
                    getMoreUsers(usersAdapter.getLastItemId())
                else {
                    getMoreSearching()
                }
            }
        })
    }

    private fun openUserDetails(user: Owner) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToUserDetailsFragment()
        action.userName = user.login ?: ""
        view?.findNavController()?.navigate(action)
    }


    private fun getUsers() = launch {
        progress.visibility = View.VISIBLE
        val users = viewModel.getUsers().await()
        users.observe(this@HomeFragment.viewLifecycleOwner, Observer {
            load_more_progress.visibility = View.GONE
            progress.visibility = View.GONE
            isLoading = false
            isSearching = false
            if (it != null && it.isSuccessful) {
                updateRecycler(it.body() ?: emptyList())
            } else
                Toast.makeText(
                    this@HomeFragment.requireContext(),
                    "Error Loading Users",
                    Toast.LENGTH_SHORT
                ).show()
        })

    }

    private fun searchUsers(query: String) = launch {
        searchQuery = query
        progress.visibility = View.VISIBLE
        usersAdapter.removeItems()
        val repos = viewModel.searchUsers(query).await()
        repos.observe(this@HomeFragment.viewLifecycleOwner, Observer {
            load_more_progress.visibility = View.GONE
            progress.visibility = View.GONE
            isLoading = false

            if (it != null && it.isSuccessful) {
                updateRecycler(it.body()?.items ?: emptyList())
            } else
                Toast.makeText(
                    this@HomeFragment.requireContext(),
                    "Error Loading Repos",
                    Toast.LENGTH_SHORT
                ).show()
        })
    }


    private fun getMoreSearching() = launch {
        viewModel.searchUsers(searchQuery, ++page).await()
    }

    private fun getMoreUsers(since: Int = 0) = launch {
        viewModel.getUsers(since).await()
    }

    private fun updateRecycler(list: List<Owner>) {
        usersAdapter.addList(list)
    }


}
