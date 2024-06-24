package com.example.githubcompose.ui.presentation.user

sealed class UserAction {
    data object OnClickBack : UserAction()
    data object OnRetryFetchUser : UserAction()
    data object OnRetryFetchRepos : UserAction()
    data class OnClickRepo(val url: String) : UserAction()
}