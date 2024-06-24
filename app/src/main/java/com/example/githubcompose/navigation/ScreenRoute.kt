package com.example.githubcompose.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoute {

    @Serializable
    data object Home: ScreenRoute()

    @Serializable
    data class User(val username: String): ScreenRoute()

    @Serializable
    data class WebView(val url: String): ScreenRoute()
}