package com.example.githubcompose.ui.presentation.home.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.githubcompose.R
import com.example.githubcompose.data.models.SearchResponse
import com.example.githubcompose.ui.presentation.home.HomeAction
import com.example.githubcompose.ui.presentation.home.HomeUiState
import com.example.githubcompose.ui.theme.GithubComposeTheme
import com.example.githubcompose.utils.createSearchUserResponse
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComponent(
    uiState: HomeUiState,
    action: (HomeAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val history = remember { mutableStateListOf<String>() }

    val searchItems = uiState.searchedUser.collectAsLazyPagingItems()
    val loadState = searchItems.loadState

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = text,
        onQueryChange = {
            text = it
            action(HomeAction.SearchUser(text))
        },
        onSearch = {
            history.add(text)
            keyboardController?.hide()
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(stringResource(R.string.search))
        },
        leadingIcon = {
            if (active) {
                IconButton(
                    onClick = {
                        active = false
                        text = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back Icon"
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            }
        },
        trailingIcon = {
            if (active && text.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable {
                        text = ""
                        action(HomeAction.InitializeUser)
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon"
                )
            }
        }
    ) {
        if (text.isEmpty()) {
            if (history.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .size(100.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(R.string.search_github_user)
                    )
                }
            } else {
                history.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                text = it
                                action(HomeAction.SearchUser(it))
                            }
                            .padding(16.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(24.dp),
                            imageVector = Icons.Default.History,
                            contentDescription = "History Icon"
                        )
                        Text(it)
                    }
                }
            }
        } else {
            ActiveSearch(
                searchItems = searchItems,
                loadState = loadState,
                action = action
            )
        }
    }
}

@Composable
fun ActiveSearch(
    searchItems: LazyPagingItems<SearchResponse.User>,
    loadState: CombinedLoadStates,
    action: (HomeAction) -> Unit
) {

    when (loadState.refresh) {
        LoadState.Loading -> LoadingScreen()

        is LoadState.NotLoading -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(count = searchItems.itemCount) { index ->
                    searchItems[index]?.let { user ->
                        SearchItem(
                            user = user,
                            onClick = {
                                action(HomeAction.OnClickUser(it))
                            }
                        )
                    }
                }

                when (loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            LoadingScreen(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            )
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            val errorMessage = (loadState.append as LoadState.Error).error.message
                            FooterRetryButton(
                                errorMessage = (errorMessage),
                                onClickRetry = {
                                    searchItems.retry()
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
                onClickRetry = { searchItems.retry() }
            )
        }
    }
}

@Composable
fun SearchItem(
    user: SearchResponse.User,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable {
                user.login?.let {
                    onClick(it)
                }
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(50.dp)
                .clip(CircleShape),
            model = user.avatarUrl ?: "",
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            fallback = painterResource(id = R.drawable.ic_launcher_foreground),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = user.login,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ActiveSearchPreview() {
    GithubComposeTheme {
        ActiveSearch(
            searchItems = flowOf(
                PagingData.from(
                    listOf(
                        createSearchUserResponse(),
                        createSearchUserResponse(),
                        createSearchUserResponse(),
                        createSearchUserResponse(),
                        createSearchUserResponse(),
                    )
                )
            ).collectAsLazyPagingItems(),
            loadState = CombinedLoadStates(
                refresh = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(false),
                append = LoadState.Error(Exception("Error")),
                source = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false)
                ),
                mediator = null
            ),
            action = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SearchBarPreview() {
    GithubComposeTheme {
        SearchComponent(
            uiState = HomeUiState.Empty,
            action = {}
        )
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SearchItemPreview() {
    GithubComposeTheme {
        SearchItem(
            user = createSearchUserResponse(),
            onClick = {}
        )
    }
}