package com.example.githubcompose.domain.useCase

import android.app.Application
import com.example.githubcompose.data.Repository
import com.example.githubcompose.data.models.UserReposResponse
import com.example.githubcompose.utils.NetworkResult
import com.example.githubcompose.utils.hasInternetConnection
import javax.inject.Inject

class GetUserReposUseCase @Inject constructor(
    private val repository: Repository,
    private val application: Application
) {
    suspend operator fun invoke(username: String) : NetworkResult<List<UserReposResponse.Repository>> {
        if (hasInternetConnection(application)) {
            try {
                val response = repository.remote.getUserRepos(username)
                return when {
                    response.isSuccessful -> {
                        NetworkResult.Success(response.body())
                    }
                    response.message().toString().contains("timeout") -> {
                        NetworkResult.Error(message = "Timeout")
                    }
                    response.body() == null -> {
                        NetworkResult.Error(message = "user not found.")
                    }
                    else -> {
                        NetworkResult.Error(message = response.message())
                    }
                }
            } catch (e: Exception) {
                return NetworkResult.Error(message = "user not found.")
            }
        } else {
            return NetworkResult.Error(message = "No internet connection.")
        }
    }
}