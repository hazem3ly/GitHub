package com.moduscapital.github.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moduscapital.github.data.network.response.Owner
import com.moduscapital.github.data.network.response.RepoDetails
import com.moduscapital.github.data.network.response.SearchResult
import retrofit2.Response
import java.io.IOException

class NetworkDataSourceImpl(
    private val apiService: ApiService
) : NetworkDataSource {
    private val _userRepos = MutableLiveData<Response<List<RepoDetails>>>()
    override val userRepos: LiveData<Response<List<RepoDetails>>>
        get() = _userRepos

    override suspend fun userRepos(userName: String, page: Int) {
        try {
            val repo = apiService.userRepos(userName,page).await()
            _userRepos.postValue(repo)
        } catch (e: IOException) {
            Log.e("Connectivity", "No internet connection.", e)
            _userRepos.postValue(null)
        }
    }

    private val _searchUsersResponse = MutableLiveData<Response<SearchResult>>()
    override val searchUsers: LiveData<Response<SearchResult>>
        get() = _searchUsersResponse

    override suspend fun searchUsers(query: String,page:Int,perPage:Int) {
        try {
            val repositories = apiService.searchUsers(query,page, perPage).await()
            _searchUsersResponse.postValue(repositories)
        } catch (e: IOException) {
            Log.e("Connectivity", "No internet connection.", e)
            _searchUsersResponse.postValue(null)
        }
    }

    private val _usersResponse = MutableLiveData<Response<List<Owner>>>()
    override val users: LiveData<Response<List<Owner>>>
        get() = _usersResponse

    override suspend fun getUsers(since: Int) {
        try {
            val repositories = apiService.users(since).await()
            _usersResponse.postValue(repositories)
        } catch (e: IOException) {
            Log.e("Connectivity", "No internet connection.", e)
            _usersResponse.postValue(null)
        }
    }

}