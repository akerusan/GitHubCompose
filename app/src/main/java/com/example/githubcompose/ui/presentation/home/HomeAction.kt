package com.example.githubcompose.ui.presentation.home

sealed class HomeAction {
    data object InitializeUser: HomeAction()
    data class SearchUser(val username: String): HomeAction()
    data class OnClickUser(val username: String): HomeAction()
}