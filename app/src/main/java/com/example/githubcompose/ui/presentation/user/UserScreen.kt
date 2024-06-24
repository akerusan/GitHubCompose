package com.example.githubcompose.ui.presentation.user

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.githubcompose.R
import com.example.githubcompose.ui.presentation.home.component.RetryButton
import com.example.githubcompose.ui.presentation.user.vo.UserReposStatesAdapter
import com.example.githubcompose.ui.presentation.user.vo.UserStateAdapter
import com.example.githubcompose.ui.theme.GithubComposeTheme
import com.example.githubcompose.utils.createUserDetailsResponse
import com.example.githubcompose.utils.createUserReposResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    uiState: UserUiState,
    action: (UserAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.user_profile))
                },
                navigationIcon = {
                    IconButton(
                        onClick = { action(UserAction.OnClickBack) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back Icon"
                        )
                    }
                }
            )
        },
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            // ユーザー詳細
            when (val adapter = uiState.user) {
                UserStateAdapter.Initial,
                UserStateAdapter.Loading -> {
                    Box(
                        modifier = Modifier.height(96.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UserStateAdapter.Success -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            model = adapter.user.avatarUrl ?: "",
                            error = painterResource(id = R.drawable.ic_launcher_foreground),
                            fallback = painterResource(id = R.drawable.ic_launcher_foreground),
                            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text(
                                text = adapter.user.name ?: "Anonymous",
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Text(
                                text = adapter.user.login,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Row(
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(22.dp),
                                    imageVector = Icons.Default.People,
                                    contentDescription = "People Icon"
                                )
                                Text(
                                    modifier = Modifier.padding(start = 8.dp),
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    text = stringResource(R.string.followers_number).format(adapter.user.followers),
                                    maxLines = 1
                                )
                                Text(
                                    modifier = Modifier.padding(start = 8.dp),
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    text = stringResource(R.string.following_number).format(adapter.user.following),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                is UserStateAdapter.Error -> {
                    Box(
                        modifier = Modifier.height(96.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val errorMessage = adapter.message
                            Text(
                                text = errorMessage ?: stringResource(id = R.string.load_error),
                                color = MaterialTheme.colorScheme.error,
                                style = TextStyle(fontWeight = FontWeight.Bold)
                            )
                            RetryButton(
                                modifier = Modifier.padding(top = 16.dp),
                                onClick = {
                                    action(UserAction.OnRetryFetchUser)
                                }
                            )
                        }
                    }
                }
            }

            // ユーザーレポジトリ一覧
            when (val adapter = uiState.repos) {
                UserReposStatesAdapter.Initial,
                UserReposStatesAdapter.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UserReposStatesAdapter.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        itemsIndexed(adapter.repos, key = { index, _ ->
                            index
                        }, itemContent = { _, item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(8))
                                    .background(MaterialTheme.colorScheme.secondary)
                                    .clickable {
                                        item.htmlUrl?.let {
                                            action(UserAction.OnClickRepo(it))
                                        }
                                    }
                                    .padding(16.dp)
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.LibraryBooks,
                                        contentDescription = "Repo Icon",
                                    )
                                    Text(
                                        text = "${item.name}",
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                                    )
                                    Text(
                                        text = "${item.stargazersCount}",
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Icon(
                                        modifier = Modifier.size(22.dp),
                                        imageVector = Icons.Default.Star,
                                        tint = Color.Yellow,
                                        contentDescription = "Star Icon"
                                    )
                                }
                                item.language?.let {
                                    Text(
                                        text = stringResource(R.string.written_with_language).format(it),
                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                        color = MaterialTheme.colorScheme.tertiary,
                                    )
                                }
                                item.description?.let { description ->
                                    Spacer(
                                        modifier = Modifier
                                            .padding(vertical = 16.dp)
                                            .background(MaterialTheme.colorScheme.onBackground)
                                            .fillMaxWidth()
                                            .height(1.dp)
                                    )
                                    Text(
                                        text = description,
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        })
                    }
                }

                is UserReposStatesAdapter.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val errorMessage = adapter.message
                            Text(
                                text = errorMessage ?: stringResource(id = R.string.load_error),
                                color = MaterialTheme.colorScheme.error,
                                style = TextStyle(fontWeight = FontWeight.Bold)
                            )
                            RetryButton(
                                modifier = Modifier.padding(top = 16.dp),
                                onClick = {
                                    action(UserAction.OnRetryFetchRepos)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserScreenPreview() {
    GithubComposeTheme {
        UserScreen(
            uiState = UserUiState(
                user = UserStateAdapter.Success(
                    createUserDetailsResponse()
                ),
                repos = UserReposStatesAdapter.Success(
                    listOf(
                        createUserReposResponse(),
                        createUserReposResponse(),
                        createUserReposResponse()
                    )
                )
            ),
            action = {}
        )
    }
}