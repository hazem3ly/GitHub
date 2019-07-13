package com.moduscapital.github.data.network

import androidx.lifecycle.LiveData
import com.moduscapital.github.data.network.response.Owner
import com.moduscapital.github.data.network.response.RepoDetails
import com.moduscapital.github.data.network.response.SearchResult
import retrofit2.Response

interface NetworkDataSource {
    val users: LiveData<Response<List<Owner>>>
    suspend fun getUsers(since: Int)

    val searchUsers: LiveData<Response<SearchResult>>
    suspend fun searchUsers(query: String, page: Int, perPage: Int)

    val userRepos: LiveData<Response<List<RepoDetails>>>
    suspend fun userRepos(userName: String, page: Int)
}