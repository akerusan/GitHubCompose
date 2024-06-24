package com.example.githubcompose.ui.presentation.webview

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.githubcompose.navigation.ScreenRoute

fun NavGraphBuilder.webViewRoute(
    onClickBack: () -> Unit
) {
    composable<ScreenRoute.WebView> {
        val args = it.toRoute<ScreenRoute.WebView>()

        WebViewScreen(
            url = args.url,
            onClickBack = onClickBack
        )
    }
}