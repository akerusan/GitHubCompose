package com.example.githubcompose.data.network

import com.example.githubcompose.data.models.SearchResponse
import com.example.githubcompose.data.models.UserDetailsResponse
import com.example.githubcompose.data.models.UserReposResponse
import com.example.githubcompose.data.models.UserListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    /** USERS LIST **/

    @GET("users")
    suspend fun getUserList(
        @Header("Accept") accept: String,
        @Header("Authorization") token: String,
        @Query("since") since: Int
    ): Response<List<UserListResponse.User>>

    /** SEARCH USERS **/

    @GET("search/users")
    suspend fun searchUser(
        @Header("Accept") accept: String,
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("page") page: Int,
    ): Response<SearchResponse>

    /** USER DETAILS **/

    @GET("users/{username}")
    suspend fun getUser(
        @Header("Accept") accept: String,
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Response<UserDetailsResponse>

    /** USER REPOSITORIES **/

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Header("Accept") accept: String,
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Response<List<UserReposResponse.Repository>>
}