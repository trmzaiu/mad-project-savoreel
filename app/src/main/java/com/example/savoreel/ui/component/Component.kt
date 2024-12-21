package com.example.savoreel.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savoreel.R
import com.example.savoreel.ui.home.SearchItem
import com.example.savoreel.ui.theme.nunitoFontFamily

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
            .background(color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(size = 15.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        singleLine = true,
        visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = nunitoFontFamily,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        ),
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
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.outline
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
        modifier = modifier
            .width(360.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold,
                color = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onTertiary
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
    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.scrim)){
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title, fontFamily = nunitoFontFamily, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground) },
            text = {
                Text(
                    message,
                    style = TextStyle(
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                )},
            containerColor = MaterialTheme.colorScheme.secondary,

            confirmButton = {
                Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)) {
                    Text("Ok", fontFamily = nunitoFontFamily, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        )
    }
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
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun BackArrow(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = {
            navController.popBackStack()
        },
        modifier = modifier
            .padding(start = 20.dp, top = 40.dp)
            .size(48.dp),
        content = {
            Icon(
                painter = painterResource(id = R.drawable.chevron_left),
                contentDescription = "Back arrow",
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
    )
}

@Composable
fun IconTheme(
    imageVector: ImageVector,
    modifier: Modifier = Modifier
) {
    Image(
        imageVector = imageVector,
        contentDescription = "Icon",
        modifier = modifier.size(30.dp),
        colorFilter = tint(MaterialTheme.colorScheme.tertiary)
    )
}

@Composable
fun ImageCustom(
    painter: Painter,
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick },
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            alignment = Alignment.Center,

            )
    }
}

@Composable
fun GridImage(posts: List<SearchItem>, onClick: (SearchItem) -> Unit){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        contentPadding = PaddingValues(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
    ) {
        items(posts) { post ->
            ImageCustom(
                painter = painterResource(post.imageRes),
                onClick = {}
            )
        }
    }
}