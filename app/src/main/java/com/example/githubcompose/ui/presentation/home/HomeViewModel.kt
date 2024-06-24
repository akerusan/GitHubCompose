package com.example.githubcompose.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.githubcompose.data.Repository
import com.example.githubcompose.data.models.SearchResponse
import com.example.githubcompose.data.models.UserListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _getUserList: MutableStateFlow<Flow<PagingData<UserListResponse.User>>> = MutableStateFlow(emptyFlow())
    private var _searchUser: MutableStateFlow<Flow<PagingData<SearchResponse.User>>> = MutableStateFlow(emptyFlow())

    val uiState: StateFlow<HomeUiState> = combine(_getUserList, _searchUser) { userList, searchUser ->
        HomeUiState(userList, searchUser)
    }.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = HomeUiState.Empty)

    init {
        fetchUserList()
    }

    fun initializeUser() {
        _searchUser.value = emptyFlow()
    }

    private fun fetchUserList() = viewModelScope.launch(Dispatchers.IO) {
        _getUserList.value = repository.remote.getUserListTest()
    }

    fun searchUser(query: String) = viewModelScope.launch(Dispatchers.IO) {
        _searchUser.value = repository.remote.searchUser(query)
    }
}