package com.example.githubcompose.ui.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubcompose.ui.presentation.home.component.ErrorScreen
import com.example.githubcompose.ui.presentation.home.component.FooterRetryButton
import com.example.githubcompose.ui.presentation.home.component.LoadingScreen
import com.example.githubcompose.ui.presentation.home.component.SearchComponent
import com.example.githubcompose.ui.presentation.home.component.UserItem
import com.example.githubcompose.ui.theme.GithubComposeTheme
import com.example.githubcompose.utils.createUserListResponseUser
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    action: (HomeAction) -> Unit
) {
    val users = uiState.userList.collectAsLazyPagingItems()
    val loadState = users.loadState
    val listState = rememberLazyGridState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchComponent(
                uiState = uiState,
                action = action
            )
        },
    ) { values ->
        when (loadState.refresh) {
            LoadState.Loading -> LoadingScreen()

            is LoadState.NotLoading -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(values),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    state = listState
                ) {
                    items(users.itemCount) { index ->
                        users[index]?.let { user ->
                            UserItem(
                                username = user.login,
                                profileUrl = user.avatarUrl ?: "",
                                onClickUser = {
                                    action(HomeAction.OnClickUser(user.login))
                                }
                            )
                        }
                    }

                    when (loadState.append) {
                        LoadState.Loading -> {
                            item(
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                LoadingScreen(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp)
                                )
                            }
                        }

                        is LoadState.Error -> {
                            item(
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                val errorMessage = (loadState.append as LoadState.Error).error.message
                                FooterRetryButton(
                                    errorMessage = (errorMessage),
                                    onClickRetry = {
                                        users.retry()
                                    }
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }

            is LoadState.Error -> {
                val errorMessage = (loadState.refresh as LoadState.Error).error.message
                ErrorScreen(
                    errorMessage = errorMessage,
                    onClickRetry = { users.retry() }
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    GithubComposeTheme {
        HomeScreen(
            uiState = HomeUiState(
                userList = flowOf(
                    PagingData.from(
                        listOf(
                            createUserListResponseUser("User1"),
                            createUserListResponseUser("User2"),
                            createUserListResponseUser("User3"),
                            createUserListResponseUser("User4"),
                            createUserListResponseUser("User5"),
                            createUserListResponseUser("User6"),
                        )
                    )
                ),
                searchedUser = flowOf(
                    PagingData.from(emptyList())
                )
            ),
            action = {}
        )
    }
}