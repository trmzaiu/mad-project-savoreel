package com.example.savoreel.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.savoreel.R
import com.example.savoreel.ui.theme.disableButtonColor
import com.example.savoreel.ui.theme.fontDarkColor
import com.example.savoreel.ui.theme.fontLightColor
import com.example.savoreel.ui.theme.lineColor
import com.example.savoreel.ui.theme.nunitoFontFamily
import com.example.savoreel.ui.theme.primaryButtonColor
import com.example.savoreel.ui.theme.secondaryLightColor

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPasswordField: Boolean = false,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(50.dp)
            .width(340.dp)
            .background(color = secondaryLightColor, shape = RoundedCornerShape(size = 15.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        singleLine = true,
        visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None,
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxSize()
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = nunitoFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = lineColor
                        )
                    )
                }
                innerTextField()
            }
        }
    )
}


@Composable
fun CustomButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .width(360.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) primaryButtonColor else disableButtonColor
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold,
                color = fontDarkColor
            )
        )
    }
}

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        containerColor = secondaryLightColor,

        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = lineColor)) {
                Text("Ok", fontFamily = nunitoFontFamily, fontWeight = FontWeight.Normal)
            }
        }
    )
}

@Composable
fun CustomTitle(
    text: String,
    modifier: Modifier = Modifier
){
    Text(
        text = text,
        fontSize = 28.sp,
        lineHeight = 45.sp,
        fontFamily = nunitoFontFamily,
        fontWeight = FontWeight.Bold,
        color = fontLightColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun BackArrow(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.chevron_left),
        contentDescription = "Back arrow",
        modifier = modifier
            .padding(start = 20.dp, top = 40.dp)
            .size(30.dp)
            .clickable { onClick() }
    )
}