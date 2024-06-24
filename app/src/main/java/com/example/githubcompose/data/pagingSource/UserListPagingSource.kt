package com.example.githubcompose.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubcompose.data.models.UserListResponse
import com.example.githubcompose.data.network.GithubApi
import com.example.githubcompose.utils.Constants.Companion.ACCEPT
import com.example.githubcompose.utils.Constants.Companion.BEARER_TOKEN
import retrofit2.HttpException
import java.io.IOException

class UserListPagingSource(
    private val service: GithubApi,
) : PagingSource<Int, UserListResponse.User>()  {

    private var prevKey = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserListResponse.User> {
        val page = params.key ?: 1
        return try {
            val response = service.getUserList(ACCEPT, BEARER_TOKEN, page)
            if (!response.isSuccessful || response.body() == null) {
                return LoadResult.Error(HttpException(response))
            }
            val result = response.body()?.map { it } ?: emptyList()

            LoadResult.Page(
                data = result,
                prevKey = if (page == 1) null else prevKey,
                nextKey = if (result.isEmpty()) null else result.last().id,
            ).also {
                prevKey = result.last().id
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserListResponse.User>): Int? {
        return state.anchorPosition
    }
}