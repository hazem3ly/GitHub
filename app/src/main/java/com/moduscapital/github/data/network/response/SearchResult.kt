package com.moduscapital.github.data.network.response

import com.google.gson.annotations.SerializedName


data class SearchResult(
    @SerializedName("total_count") val total_count: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<Owner>
)