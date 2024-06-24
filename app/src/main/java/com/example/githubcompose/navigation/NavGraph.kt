package com.example.githubcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.githubcompose.ui.presentation.home.homeRoute

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
                // TODO: ユーザー詳細画面へ遷移
            }
        )
    }
}