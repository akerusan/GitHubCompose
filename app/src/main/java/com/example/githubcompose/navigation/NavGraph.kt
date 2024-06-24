package com.example.githubcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.githubcompose.ui.presentation.home.homeRoute
import com.example.githubcompose.ui.presentation.user.userRoute

@Composable
fun SetupNavGraph(
    startDestination: ScreenRoute,
    navController: NavHostController
) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        // ホーム画面
        homeRoute(
            onClickUser = { username ->
                navController.navigate(ScreenRoute.User(username))
            }
        )

        // ユーザー詳細画面
        userRoute(
            onClickRepo = { url ->
                // TODO: WebView画面に遷移
            },
            onClickBack = {
                navController.popBackStack()
            }
        )
    }
}