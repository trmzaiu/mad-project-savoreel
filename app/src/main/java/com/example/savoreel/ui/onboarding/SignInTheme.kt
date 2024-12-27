@file:Suppress("DEPRECATION")

package com.example.savoreel.ui.onboarding

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.savoreel.R
import com.example.savoreel.model.ThemeViewModel
import com.example.savoreel.model.UserViewModel
import com.example.savoreel.ui.component.CustomButton
import com.example.savoreel.ui.component.CustomInputField
import com.example.savoreel.ui.component.ErrorDialog
import com.example.savoreel.ui.theme.domineFontFamily
import com.example.savoreel.ui.theme.nunitoFontFamily
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@Composable
fun SignInScreenTheme(navController: NavController, userViewModel: UserViewModel, themeViewModel: ThemeViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
//    val context = ApplicationProvider.getApplicationContext<Context>()
//
//    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken("98860832757-kr9irk7q2et536qccn4iqulr5th1bnih.apps.googleusercontent.com")
//        .requestEmail()
//        .build()
//
//    val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    val isFormValid = email.isNotEmpty() && password.isNotEmpty()

    fun signIn() {
        isLoading = true
        userViewModel.viewModelScope.launch {
            try {
                userViewModel.signIn(email, password, onSuccess = {
                    isLoading = false
                    themeViewModel.loadUserSettings()
                    navController.navigate("take_photo_screen")
                }, onFailure = {
                    isLoading = false
                    errorMessage = "Make sure you entered your email and password correctly and try again."
                    showErrorDialog = true
                    Log.e("SignInScreen", errorMessage)
                })
            } catch (e: Exception) {
                // Handle any other exceptions that might occur
                isLoading = false
                errorMessage = "An unexpected error occurred: ${e.message}"
                showErrorDialog = true
                Log.e("SignInScreen", "Error during sign-in", e)
            }
        }
    }
    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    navController.navigate("take_photo_screen")
                } else {
                    errorMessage = "Google login failed: ${task.exception?.message}"
                    showErrorDialog = true
                }
            }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("SignIn", "Google sign in failed", e)
            }
        }
    }

    fun signInWithGoogle() {
//        val signInIntent = mGoogleSignInClient.signInIntent
//        launcher.launch(signInIntent)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(R.drawable.rounded_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "Savoreel",
                fontSize = 48.sp,
                lineHeight = 20.sp,
                fontFamily = domineFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(100.dp))

            // Email and Password Section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Email Input
                CustomInputField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    isPasswordField = false
                )

                // Password Input
                CustomInputField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    isPasswordField = true
                )

                // Forgot Password
                Box(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "Forgot Password",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = nunitoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondary,
                        ),
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigate("email_screen/0?isChangeEmail=false")
                            }

                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Sign In Button
            CustomButton(
                text = if (isLoading) "Loading..." else "Sign in",
                enabled = isFormValid,
                onClick = {
                    signIn()
                }
            )

            Spacer(modifier = Modifier.height(70.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Or connect with",
                    fontSize = 16.sp,
                    fontFamily = nunitoFontFamily,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    // Google Icon
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                signInWithGoogle()
                            }
                    )

                    Spacer(modifier = Modifier.width(16.dp))
//
                    // Facebook Icon
                    Image(
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "Facebook Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                println("Sign in with facebook")
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(42.dp))

            Row {
                Text(
                    text = "Don't have an account? ",
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSecondary,

                        textAlign = TextAlign.Center,
                    )
                )

                Text(
                    text = "Sign Up",
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null) {
                            navController.navigate("sign_up_screen")
                        }
                )
            }
        }
    }
    if (showErrorDialog) {
        ErrorDialog(
            title = "Couldn't sign in",
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}

