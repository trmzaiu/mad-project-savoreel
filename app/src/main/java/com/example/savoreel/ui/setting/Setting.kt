package com.example.savoreel.ui.setting

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savoreel.R
import com.example.savoreel.model.ThemeViewModel
import com.example.savoreel.model.UserViewModel
import com.example.savoreel.ui.component.BackArrow
import com.example.savoreel.ui.component.ConfirmDialog
import com.example.savoreel.ui.component.CustomSwitch
import com.example.savoreel.ui.component.ForwardArrow
import com.example.savoreel.ui.component.IconTheme
import com.example.savoreel.ui.theme.SavoreelTheme
import com.example.savoreel.ui.theme.nunitoFontFamily
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTheme(navController: NavController, themeViewModel: ThemeViewModel, userViewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    var showModal by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isSignOut by remember { mutableStateOf(false) }
    val isDarkModeEnabled by themeViewModel.isDarkModeEnabled.observeAsState(initial = false)

    LaunchedEffect(Unit) {
        userViewModel.getUser(onSuccess = { user ->
            if (user != null) {
                name = user.name.toString()
            } else {
                Log.e("Setting", "User data not found")
            }
        }, onFailure = { error ->
            Log.e("NameTheme", "Error retrieving user: $error")
        })
    }

    fun deleteAccount() {
        userViewModel.deleteUserAndData(
            onSuccess = {
                themeViewModel.resetDarkMode()
                navController.navigate("onboarding")
                Log.d("FirebaseAuth", "User account deleted successfully.")
            },
            onFailure = { error ->
                Log.e("FirebaseAuth", error)
            }
        )
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        themeViewModel.resetDarkMode()
        navController.navigate("onboarding")
    }

    SavoreelTheme(darkTheme = isDarkModeEnabled) {
         Box(
             modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
         ) {
             Column(
                 modifier = Modifier.fillMaxWidth()
             ) {
                 Box(
                     modifier = Modifier
                         .fillMaxWidth()
                         .background(color = MaterialTheme.colorScheme.background)
                 ) {
                     BackArrow(
                         modifier = Modifier.align(Alignment.TopStart).padding(start = 20.dp, top = 40.dp)
                     )

                     Text(
                         text = "Setting",
                         fontFamily = nunitoFontFamily,
                         textAlign = TextAlign.Center,
                         fontWeight = FontWeight.SemiBold,
                         fontSize = 32.sp,
                         color = MaterialTheme.colorScheme.onBackground,
                         modifier = Modifier
                             .align(Alignment.Center)
                             .padding(top = 40.dp, bottom = 10.dp)
                     )
                 }

                 LazyColumn(
                     modifier = Modifier.padding(horizontal = 35.dp)
                 ) {
                     item {
                         Spacer(modifier = Modifier.height(20.dp))

                         Box(
                             contentAlignment = Alignment.Center,
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .align(Alignment.CenterHorizontally)
                         ) {
                             Image(
                                 painter = painterResource(R.drawable.default_avatar),
                                 contentDescription = "User Avatar",
                                 modifier = Modifier
                                     .clip(CircleShape)
                                     .clickable { showModal = true }
                                     .size(150.dp),
                                 contentScale = ContentScale.Crop
                             )
                         }

                         if (showModal) {
                             Box(
                                 modifier = Modifier
                                 .fillMaxSize()
                                 .background(MaterialTheme.colorScheme.scrim)
                             ){
                                 ModalBottomSheet(
                                     onDismissRequest = { showModal = false },
                                     sheetState = rememberModalBottomSheetState(
                                         skipPartiallyExpanded = true
                                     ),
                                     containerColor = MaterialTheme.colorScheme.secondary
                                 )
                                 {
                                     SheetContent(
                                         onOptionClick = { option ->
                                             showModal = false
                                             handleAvatarOption(option)
                                         }
                                     )
                                 }
                             }
                         }

                         Spacer(modifier = Modifier.height(15.dp))

                         Text(
                             text = name,
                             fontFamily = nunitoFontFamily,
                             textAlign = TextAlign.Center,
                             fontWeight = FontWeight.Bold,
                             fontSize = 24.sp,
                             color = MaterialTheme.colorScheme.onBackground,
                             modifier = Modifier.fillMaxWidth()
                         )

                         // General Section
                         Spacer(modifier = Modifier.height(20.dp))

                         SettingsSection(title = "General", imageVector = ImageVector.vectorResource(R.drawable.ic_general)) {
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_name),
                                 text = "Edit name",
                                 navController = navController,
                                 destination = "name_screen?isChangeName=true",
                             )
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_mail),
                                 text = "Change email",
                                 navController = navController,
                                 destination = "password_screen?isChangePassword=false",
                             )
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_key),
                                 text = "Change password",
                                 navController = navController,
                                 destination = "password_screen?isChangePassword=true",
                             )
                         }

                         // Support Section
                         SettingsSection(title = "Support", imageVector = ImageVector.vectorResource(R.drawable.ic_support)) {
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_darkmode),
                                 text = "Dark mode",
                                 changeMode = true,
                                 isChecked = isDarkModeEnabled,
                                 onCheckedChange = { themeViewModel.toggleDarkMode() }
                             )

                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_language),
                                 text = "Language",
                                 navController = navController,
                                 destination = "language",
                             )
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_noti),
                                 text = "Notifications",
                                 navController = navController,
                                 destination = "notification_setting",
                             )
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_report),
                                 text = "Report a problem",
                                 navController = navController,
                                 destination = "report_a_problem",
                             )
                         }

                         // About Section
                         SettingsSection(title = "About", imageVector = ImageVector.vectorResource(R.drawable.ic_about)) {
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_share),
                                 text = "Share account",
                                 navController = navController,
                                 destination = "share_account",
                             )
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_term),
                                 text = "Terms of service",
                                 navController = navController,
                                 destination = "terms_of_service",
                             )
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_privacy),
                                 text = "Privacy",
                                 navController = navController,
                                 destination = "privacy",
                             )
                         }

                         SettingsSection(title = "Danger Zone", imageVector = ImageVector.vectorResource(R.drawable.ic_danger)) {
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_delete),
                                 text = "Delete account",
                                 onClick = {
                                     errorMessage = "Are you want to delete your account? This action cannot be undone."
                                     showErrorDialog = true
                                     isSignOut = false
                                 }
                             )
                             SettingItemWithNavigation(
                                 icon = ImageVector.vectorResource(R.drawable.ic_logout),
                                 text = "Sign out",
                                 onClick = {
                                     errorMessage = "Are you sure you want to sign out?"
                                     showErrorDialog = true
                                     isSignOut = true
                                 }
                             )
                         }
                     }
                 }
             }
         }
         if (showErrorDialog) {
             ConfirmDialog(
                 title = if (isSignOut) "Confirm" else "We're sorry to see you go!",
                 message = errorMessage,
                 onDismiss = {
                     showErrorDialog = false
                 },
                 onConfirm = {
                     if (isSignOut) {
                         showErrorDialog = false
                         signOut()
                     } else {
                         showErrorDialog = false
                         deleteAccount()
                     }
                 }
             )
         }
    }
}

@Composable
fun SettingItemWithNavigation(
    icon: ImageVector? = null,
    text: String,
    navController: NavController? = null,
    destination: String? = null,
    changeMode: Boolean = false,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    val modifier = if (!changeMode) {
        Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                destination?.let {
                    navController?.navigate(it)
                }
                onClick()
            }
    } else {
        Modifier
            .fillMaxWidth()
            .height(55.dp)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        if (icon != null) {
            IconTheme(
                imageVector = icon,
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 20.sp,
            fontFamily = nunitoFontFamily,
            fontWeight = FontWeight.Bold,
            color = if (text == "Delete account") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        )

        if (changeMode) {
            CustomSwitch(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        } else {
            if (navController != null && destination != null) {
                ForwardArrow(navController = navController, destination = destination)
            }
        }
    }
}



