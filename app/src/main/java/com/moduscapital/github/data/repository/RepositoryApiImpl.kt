package com.moduscapital.github.data.repository

import androidx.lifecycle.LiveData
import com.moduscapital.github.data.db.RepoDetailsDao
import com.moduscapital.github.data.network.NetworkDataSource
import com.moduscapital.github.data.network.response.Owner
import com.moduscapital.github.data.network.response.RepoDetails
import com.moduscapital.github.data.network.response.SearchReaslt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RepositoryApiImpl(
    private val networkDataSource: NetworkDataSource,
    private val repoDetailsDao: RepoDetailsDao
) : RepositoryApi {



    init {
        networkDataSource.apply {
            userRepos.observeForever {
                // persist
                it?.let {
                    persistFetchedData(it)
                }
            }
        }
    }

    override suspend fun deleteOldUserRepos() {
        repoDetailsDao.deleteOldEntries()
    }

    private fun persistFetchedData(response: Response<List<RepoDetails>>) {
        GlobalScope.launch(Dispatchers.IO) {
            response.body()?.let { repoDetailsDao.insert(it) }
        }
    }


    override suspend fun getUserRepos(userName: String, page: Int): LiveData<List<RepoDetails>> {
        return withContext(Dispatchers.IO) {
            networkDataSource.userRepos(userName, page)
            return@withContext repoDetailsDao.getSavedRepos()
        }
    }


    override suspend fun searchUsers(
        query: String,
        page: Int,
        perPage: Int
    ): LiveData<Response<SearchReaslt>> {
        return withContext(Dispatchers.IO) {
            networkDataSource.searchUsers(query, page, perPage)
            return@withContext networkDataSource.searchUsers
        }
    }

    override suspend fun getUsers(since: Int): LiveData<Response<List<Owner>>> {
        return withContext(Dispatchers.IO) {
            networkDataSource.getUsers(since)
            return@withContext networkDataSource.users
        }
    }
}