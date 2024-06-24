package com.example.githubcompose.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubcompose.data.models.SearchResponse
import com.example.githubcompose.data.models.UserListResponse
import com.example.githubcompose.data.network.GithubApi
import com.example.githubcompose.data.pagingSource.SearchUserPagingSource
import com.example.githubcompose.data.pagingSource.UserListPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val githubApi: GithubApi) {

    /** USERS LIST **/
    fun getUserListTest(): Flow<PagingData<UserListResponse.User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 40,
                prefetchDistance = 40
            ),
            pagingSourceFactory = { UserListPagingSource(githubApi) }
        ).flow
    }

    /** SEARCH USERS**/
    fun searchUser(query: String): Flow<PagingData<SearchResponse.User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 40,
                prefetchDistance = 40
            ),
            pagingSourceFactory = { SearchUserPagingSource(githubApi, query) }
        ).flow
    }
}