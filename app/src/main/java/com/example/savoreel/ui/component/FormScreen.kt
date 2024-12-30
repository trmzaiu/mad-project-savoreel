package com.example.savoreel.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CommonForm(
    title: String,
    placeholder: String,
    buttonText: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordField: Boolean = false,
    isButtonEnabled: Boolean = true,
    onClickButton: () -> Unit,
    additionalContent: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        BackArrow(
            modifier = Modifier.align(Alignment.TopStart).padding(start = 20.dp, top = 40.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            CustomTitle(
                text = title
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Input Field
            CustomInputField(
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder,
                isPasswordField = isPasswordField
            )

            Spacer(modifier = Modifier.height(159.dp))

            // Continue Button
            CustomButton(
                text = buttonText,
                enabled = isButtonEnabled,
                onClick = onClickButton
            )

            Spacer(modifier = Modifier.height(78.dp))
        }
    }

    additionalContent?.invoke()
}

