package com.moduscapital.github.data.repository

import androidx.lifecycle.LiveData
import com.moduscapital.github.data.network.response.Owner
import com.moduscapital.github.data.network.response.RepoDetails
import com.moduscapital.github.data.network.response.SearchResult
import retrofit2.Response

interface RepositoryApi {
    suspend fun deleteOldUserRepos()
    suspend fun getUsers(since: Int): LiveData<Response<List<Owner>>>
    suspend fun searchUsers(
        query: String,
        page: Int,
        perPage: Int
    ): LiveData<Response<SearchResult>>
    suspend fun getUserRepos(userName: String, page: Int): LiveData<List<RepoDetails>>
}