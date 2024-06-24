package com.example.githubcompose.utils

import com.example.githubcompose.data.models.SearchResponse
import com.example.githubcompose.data.models.UserListResponse

fun createUserListResponseUser(username: String): UserListResponse.User {
    return UserListResponse.User(
        avatarUrl = "https://example.com/avatar",
        eventsUrl = "https://example.com/events",
        followersUrl = "https://example.com/followers",
        followingUrl = "https://example.com/following",
        gistsUrl = "https://example.com/gists",
        gravatarId = "gravatarId",
        htmlUrl = "https://example.com/html",
        id = 1,
        login = username,
        nodeId = "nodeId",
        organizationsUrl = "https://example.com/organizations",
        receivedEventsUrl = "https://example.com/received_events",
        reposUrl = "https://example.com/repos",
        siteAdmin = false,
        starredUrl = "https://example.com/starred",
        subscriptionsUrl = "https://example.com/subscriptions",
        type = "User",
        url = "https://example.com/url"
    )
}

fun createSearchUserResponse(): SearchResponse.User {
    return SearchResponse.User(
        avatarUrl = "https://example.com/avatar",
        eventsUrl = "https://example.com/events",
        followersUrl = "https://example.com/followers",
        followingUrl = "https://example.com/following",
        gistsUrl = "https://example.com/gists",
        gravatarId = "",
        htmlUrl = "https://example.com/html",
        id = 1,
        login = "username",
        nodeId = "node1",
        organizationsUrl = "https://example.com/orgs",
        receivedEventsUrl = "https://example.com/received_events",
        reposUrl = "https://example.com/repos",
        siteAdmin = false,
        starredUrl = "https://example.com/starred",
        subscriptionsUrl = "https://example.com/subscriptions",
        type = "User",
        score = 0.0,
        url = "",
    )
}