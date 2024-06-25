package com.example.githubcompose.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubcompose.BuildConfig.BEARER_TOKEN
import com.example.githubcompose.data.models.SearchResponse
import com.example.githubcompose.data.network.GithubApi
import com.example.githubcompose.utils.Constants.Companion.ACCEPT
import retrofit2.HttpException
import java.io.IOException

class SearchUserPagingSource(
    private val service: GithubApi,
    private val query: String
) : PagingSource<Int, SearchResponse.User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResponse.User> {
        val page = params.key ?: 1
        return try {
            val response = service.searchUser(ACCEPT, BEARER_TOKEN, query, page)
            if (!response.isSuccessful || response.body() == null) {
                return LoadResult.Error(HttpException(response))
            }
            val result = response.body()?.users ?: emptyList()

            LoadResult.Page(
                data = result,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (result.isEmpty()) null else page + 1,
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchResponse.User>): Int? {
        return state.anchorPosition
    }
}