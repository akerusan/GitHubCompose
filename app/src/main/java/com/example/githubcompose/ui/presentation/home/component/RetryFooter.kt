package com.example.githubcompose.ui.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.githubcompose.R

@Composable
fun FooterRetryButton(
    errorMessage: String?,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.error)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = errorMessage ?: stringResource(id = R.string.load_error),
            color = MaterialTheme.colorScheme.onError,
            style = TextStyle(fontWeight = FontWeight.Bold),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
                .weight(1f)
        )
        RetryButton(
            modifier = Modifier.padding(vertical = 16.dp),
            onClick = { onClickRetry() }
        )
    }
}