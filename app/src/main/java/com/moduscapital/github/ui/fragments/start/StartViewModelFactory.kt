package com.moduscapital.github.ui.fragments.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moduscapital.github.data.repository.RepositoryApi

class StartViewModelFactory(
    private val repository: RepositoryApi
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StartViewModel() as T
    }
}