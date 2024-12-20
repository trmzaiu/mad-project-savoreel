package com.example.savoreel.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.savoreel.ui.home.Notifications
import com.example.savoreel.ui.home.Searching
import com.example.savoreel.ui.home.SearchingResult
import com.example.savoreel.ui.onboarding.ChangePasswordTheme
import com.example.savoreel.ui.onboarding.EmailTheme
import com.example.savoreel.ui.onboarding.HashTagTheme
import com.example.savoreel.ui.onboarding.NameTheme
import com.example.savoreel.ui.onboarding.OnboardingTheme
import com.example.savoreel.ui.onboarding.SignInScreenTheme
import com.example.savoreel.ui.onboarding.SignUpScreenTheme
import com.example.savoreel.ui.onboarding.SuccessTheme
import com.example.savoreel.ui.onboarding.VerifyCodeTheme
import com.example.savoreel.ui.profile.FollowScreen
import com.example.savoreel.ui.setting.NotificationSetting
import com.example.savoreel.ui.setting.SettingsScreen
import com.example.savoreel.ui.setting.TermsOfServiceScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
//        startDestination = "sign_in_screen",
        startDestination = "onboarding"
    ) {
        composable("sign_in_screen") {
            SignInScreenTheme(navController = navController)
        }

        composable("sign_up_screen") {
            SignUpScreenTheme(navController = navController)
        }

        composable("email_screen?isChangeEmail={isChangeEmail}") { backStackEntry ->
            val isChangeEmail = backStackEntry.arguments?.getString("isChangeEmail")?.toBoolean() ?: false
            EmailTheme(navController = navController, isChangeEmail = isChangeEmail)
        }

        composable("verify_code_screen") {
            VerifyCodeTheme(navController = navController)
        }

        composable("change_password_screen") {
            ChangePasswordTheme(navController = navController)
        }

        composable("name_screen/{currentName}") { backStackEntry ->
            val currentName = backStackEntry.arguments?.getString("currentName")
            NameTheme(
                navController = navController,
                currentName = currentName.takeIf { it != "unknown" }
            ) { newName ->
                println("Submitted name: $newName")
            }
        }

        composable("success_screen") {
            SuccessTheme(navController = navController)
        }

        composable("hashtag_screen") {
            HashTagTheme(navController = navController)
        }

        composable("notification"){
            Notifications(navController = navController)
        }

        composable("searching"){
            Searching(navController = navController)
        }

        composable("searching_result/{query}", arguments = listOf(navArgument("query") { type = NavType.StringType })) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchingResult(navController = navController, searchQuery = query)
        }


        composable("onboarding") {
            OnboardingTheme(navController = navController)
        }

        composable("settings_screen") {
            SettingsScreen(navController = navController)
        }

        composable("notification_setting") {
            NotificationSetting(navController = navController)
        }

        composable("follow") {
            FollowScreen(navController = navController)
        }

        composable("terms_of_service") {
            TermsOfServiceScreen(navController = navController)
        }
    }
}


