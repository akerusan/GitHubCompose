package com.example.githubcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.githubcompose.navigation.ScreenRoute
import com.example.githubcompose.navigation.SetupNavGraph
import com.example.githubcompose.ui.theme.GithubComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        setContent {
            GithubComposeTheme {
                navController = rememberNavController()
                SetupNavGraph(
                    startDestination = ScreenRoute.Home,
                    navController = navController
                )
            }
        }
    }
}