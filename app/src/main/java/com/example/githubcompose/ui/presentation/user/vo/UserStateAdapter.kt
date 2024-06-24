package com.example.githubcompose.ui.presentation.user.vo

import com.example.githubcompose.data.models.UserDetailsResponse

sealed class UserStateAdapter {
    data object Initial : UserStateAdapter()
    data object Loading : UserStateAdapter()
    class Success(val user: UserDetailsResponse): UserStateAdapter()
    class Error(val message: String?): UserStateAdapter()
}