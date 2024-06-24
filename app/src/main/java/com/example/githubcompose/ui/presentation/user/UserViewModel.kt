package com.example.githubcompose.ui.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubcompose.domain.useCase.GetUserReposUseCase
import com.example.githubcompose.domain.useCase.GetUserDetailsUseCase
import com.example.githubcompose.ui.presentation.user.vo.UserReposStatesAdapter
import com.example.githubcompose.ui.presentation.user.vo.UserStateAdapter
import com.example.githubcompose.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val getUserReposUseCase: GetUserReposUseCase
) : ViewModel() {

    private var _getUser = MutableStateFlow<UserStateAdapter>(UserStateAdapter.Initial)
    private var _getUserRepos = MutableStateFlow<UserReposStatesAdapter>(UserReposStatesAdapter.Initial)

    val uiState: StateFlow<UserUiState> = _getUser.combine(_getUserRepos) { user, repos ->
        UserUiState(user, repos)
    }.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = UserUiState.Empty)

    fun fetchUser(username: String) = viewModelScope.launch(Dispatchers.IO)  {
        _getUser.value = UserStateAdapter.Loading
        getUserDetailsUseCase(username).let { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let {
                        _getUser.value = UserStateAdapter.Success(it)
                    }
                }
                is NetworkResult.Error -> _getUser.value = UserStateAdapter.Error(message = result.message)
                is NetworkResult.Loading -> _getUser.value = UserStateAdapter.Loading
            }
        }
    }

    fun fetchUserRepos(username: String) = viewModelScope.launch(Dispatchers.IO)  {
        _getUserRepos.value = UserReposStatesAdapter.Loading
        getUserReposUseCase(username).let { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { repo ->
                        val filteredRepos = repo.filterNot { it.fork }
                        _getUserRepos.value = UserReposStatesAdapter.Success(filteredRepos)
                    }
                }
                is NetworkResult.Error -> _getUserRepos.value = UserReposStatesAdapter.Error(message = result.message)
                is NetworkResult.Loading -> _getUserRepos.value = UserReposStatesAdapter.Loading
            }
        }
    }
}
