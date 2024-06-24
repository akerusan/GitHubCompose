package com.example.githubcompose.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(remoteDataSource: RemoteDataSource) {
    val remote = remoteDataSource
}