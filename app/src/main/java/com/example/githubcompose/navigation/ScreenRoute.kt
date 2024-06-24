package com.example.githubcompose.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoute {

    @Serializable
    data object Home: ScreenRoute()
}