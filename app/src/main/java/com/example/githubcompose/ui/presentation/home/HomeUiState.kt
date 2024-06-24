package com.example.githubcompose.ui.presentation.home

import androidx.paging.PagingData
import com.example.githubcompose.data.models.SearchResponse
import com.example.githubcompose.data.models.UserListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeUiState(
    val userList: Flow<PagingData<UserListResponse.User>>,
    val searchedUser: Flow<PagingData<SearchResponse.User>>
) {
    companion object {
        val Empty = HomeUiState(
            userList = emptyFlow(),
            searchedUser = emptyFlow()
        )
    }
}