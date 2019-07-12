package com.moduscapital.github.ui.fragments.userdetails

import androidx.lifecycle.ViewModel
import com.moduscapital.github.data.repository.RepositoryApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class UserDetailsViewModel(val repository: RepositoryApi) : ViewModel() {
    internal suspend fun getUserRepos(userName: String, page: Int = 1) = GlobalScope.async {
        return@async repository.getUserRepos(userName, page)
    }

    internal suspend fun deleteOldData() = GlobalScope.async {
        return@async repository.deleteOldUserRepos()
    }
}
