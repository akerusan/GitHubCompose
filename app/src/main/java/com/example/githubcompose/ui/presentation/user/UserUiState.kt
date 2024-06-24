package com.example.githubcompose.ui.presentation.user

import com.example.githubcompose.ui.presentation.user.vo.UserReposStatesAdapter
import com.example.githubcompose.ui.presentation.user.vo.UserStateAdapter

data class UserUiState(
    val user: UserStateAdapter,
    val repos: UserReposStatesAdapter
) {
    companion object {
        val Empty = UserUiState(
            user = UserStateAdapter.Initial,
            repos = UserReposStatesAdapter.Initial
        )
    }
}