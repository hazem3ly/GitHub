package com.moduscapital.github.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.moduscapital.github.data.repository.RepositoryApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class HomeViewModel(val repository: RepositoryApi) : ViewModel() {

    internal suspend fun getUsers(since: Int = 0) = GlobalScope.async {
        return@async repository.getUsers(since)
    }


    internal suspend fun searchUsers(query: String, page: Int = 1, perPage: Int = 20) =
        GlobalScope.async {
        return@async repository.searchUsers(query, page, perPage)
    }
}
