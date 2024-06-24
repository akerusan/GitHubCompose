package com.example.githubcompose.ui.presentation.home

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.githubcompose.navigation.ScreenRoute

fun NavGraphBuilder.homeRoute(
    onClickUser: (String) -> Unit
) {
    composable<ScreenRoute.Home> {
        val viewModel: HomeViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()

        HomeScreen(
            uiState = uiState,
            action = { action ->
                when (action) {
                    HomeAction.InitializeUser -> {
                        viewModel.initializeUser()
                    }

                    is HomeAction.SearchUser -> {
                        viewModel.searchUser(action.username)
                    }

                    is HomeAction.OnClickUser -> {
                        onClickUser(action.username)
                    }
                }
            }
        )
    }
}