package com.example.githubcompose.ui.presentation.user

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.githubcompose.navigation.ScreenRoute

fun NavGraphBuilder.userRoute(
    onClickRepo: (String) -> Unit,
    onClickBack: () -> Unit,
) {
    composable<ScreenRoute.User> {
        val args = it.toRoute<ScreenRoute.User>()

        val viewModel: UserViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.fetchUser(args.username)
            viewModel.fetchUserRepos(args.username)
        }

        UserScreen(
            uiState = uiState,
            action = { action ->
                when (action) {
                    is UserAction.OnClickBack -> onClickBack()
                    is UserAction.OnClickRepo -> onClickRepo(action.url)
                    UserAction.OnRetryFetchUser -> viewModel.fetchUser(args.username)
                    UserAction.OnRetryFetchRepos -> viewModel.fetchUserRepos(args.username)
                }
            }
        )
    }
}