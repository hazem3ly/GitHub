package com.moduscapital.github.data.repository

import androidx.lifecycle.LiveData
import com.moduscapital.github.data.network.response.*
import retrofit2.Response

interface RepositoryApi {
    suspend fun getUsers(since: Int): LiveData<Response<List<Owner>>>
    suspend fun searchUsers(query: String, page: Int, perPage: Int): LiveData<Response<SearchReaslt>>
    suspend fun getUserRepos(userName: String, page: Int): LiveData<List<RepoDetails>>
}