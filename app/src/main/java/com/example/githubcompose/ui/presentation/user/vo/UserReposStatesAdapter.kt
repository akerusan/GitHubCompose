package com.example.githubcompose.ui.presentation.user.vo

import com.example.githubcompose.data.models.UserReposResponse

sealed class UserReposStatesAdapter {
    data object Initial : UserReposStatesAdapter()
    data object Loading : UserReposStatesAdapter()
    class Success(val repos: List<UserReposResponse.Repository>): UserReposStatesAdapter()
    class Error(val message: String?): UserReposStatesAdapter()
}