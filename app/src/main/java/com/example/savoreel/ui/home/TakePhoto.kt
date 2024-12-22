package com.example.savoreel.ui.home



import CameraFrame
import RequestCameraPermission
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.savoreel.R
import com.example.savoreel.ui.theme.SavoreelTheme
import com.example.savoreel.ui.theme.secondaryDarkColor
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTopBar(onNavigateTo: NavHostController) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        title = {},
        modifier = Modifier
            .padding(10.dp, 5.dp, 10.dp, 0.dp),
        actions = {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xF0F0F0F0))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                        .clickable { }
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xF0F0F0F0))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_noti),
                    contentDescription = "Notifications",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                        .clickable { }

                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xF0F0F0F0))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_avar),
                    contentDescription = "User Icon",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                        .clickable { }
                )
            }
        }
    )
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostView(
    navController : NavHostController,
    postViewModel: PostViewModel = viewModel(),
    onNavigateTo: NavHostController = rememberNavController()
) {
    val photoUri by postViewModel.photoUri
    val isPhotoTaken by postViewModel.isPhotoTaken
    var isFrontCamera by remember { mutableStateOf(false) }
    var permissionGranted by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }
    val isCaptureLocked by postViewModel.isCaptureLocked
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var currentEditOption by remember { mutableStateOf("") }

    val title by postViewModel.title
    val location by postViewModel.location
    val hashtag by postViewModel.hashtag
    val editingField by postViewModel.editingField

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        postViewModel.setPhotoUri(uri)
    }

    if (!permissionGranted) {
        RequestCameraPermission(
            onPermissionGranted = { permissionGranted = true },
            onPermissionDenied = { permissionDenied = true }
        )
    } else {
        Scaffold(
            topBar = { PostTopBar(onNavigateTo) },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(10.dp, 0.dp, 10.dp, 15.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(top = 60.dp)
                    ) {
                        Text(
                            text = "Name",
                            fontSize = 20.sp,
                            lineHeight = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = secondaryDarkColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(30.dp))
                                .background(Color.Gray)
                        ) {
                            if (isPhotoTaken && photoUri != null) {
                                GlideImage(
                                    model = photoUri,
                                    contentDescription = "Captured Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                CameraFrame(
                                    modifier = Modifier.fillMaxSize(),
                                    isFrontCamera = isFrontCamera,
                                    onSwapCamera = { isFrontCamera = !isFrontCamera },
                                    onCapturePhoto = { uri ->
                                        postViewModel.setPhotoUri(uri)
                                    },
                                    isCaptureLocked = isCaptureLocked
                                )
                            }
                        }
                        if (isPhotoTaken) {
                            EditableField(
                                label = "Add Title",
                                value = title,
                                onStartEdit = { postViewModel.startEditingField("title") },
                            )
                            if (hashtag.isNotEmpty() || editingField == "Hashtag") {
                                EditableField(
                                    label = "Add Hashtag",
                                    value = hashtag,
                                    onStartEdit = { postViewModel.startEditingField("hashtag") },
                                    TextAlign.Justify,
                                    FontWeight.Normal,
                                    R.drawable.ic_avar
                                )
                            }
                            if (location.isNotEmpty() || editingField == "Location") {
                                EditableField(
                                    label = "Add Location",
                                    value = location,
                                    onStartEdit = { postViewModel.startEditingField("location") },
                                    TextAlign.Justify,
                                    FontWeight.Normal,
                                    R.drawable.ic_edit
                                )
                            }
                        }
                    }

                    // Bottom Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 20.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (isPhotoTaken) {
                            // State after photo taken
                            ImageButton(R.drawable.ic_discard, "Discard", 40.dp) {
                                postViewModel.resetPhoto()
                            }
                            ImageButton(R.drawable.ic_send, "Post", 75.dp) {

                            }
                            ImageButton(R.drawable.ic_edit, "Edit", 35.dp) {
                                scope.launch { sheetState.show() }
                            }
                        } else {
                            // State before photo taken
                            ImageButton(R.drawable.ic_upload, "Upload", 40.dp) {
                                galleryLauncher.launch("image/*")
                            }
                            ImageButton(R.drawable.ic_camera, "Take Photo", 75.dp) {
                                postViewModel.setisPhotoTaken(true)
                                postViewModel.setisCapturing(true)
                                 Log.d("PostView", "Take Photo button clicked")
                            }
                            ImageButton(R.drawable.ic_camflip, "Swap Camera", 35.dp) {
                                isFrontCamera = !isFrontCamera
                            }
                        }
                    }
                }
            }
        )
    }
    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { scope.launch { sheetState.hide() } },
            sheetState = sheetState
        ) {
            EditOptionsOverlay(
                onDismiss = { scope.launch { sheetState.hide() } },
                onSelect = { option ->
                    when (option.lowercase()) {
                        "hashtag" -> {
                            currentEditOption = "hashtag"
                            postViewModel.startEditingField("hashtag")
                        }
                        "location" -> {
                            currentEditOption = "location"
                            postViewModel.startEditingField("location")
                        }
                        "download" -> {
                            photoUri?.let {
                                downloadPhoto(context, it)
                            }
                        }
                        "share" -> {
                            photoUri?.let {
                                sharePhoto(context, it)
                            }
                        }
                    }
                    scope.launch { sheetState.hide() }
                }
            )
        }
    }

    editingField?.let { field ->
        BlurredInputOverlay(
            label = "Enter ${field.capitalize()}",
            initialValue = when (field) {
                "title" -> title
                "location" -> location
                "hashtag" -> hashtag
                else -> ""
            },
            onValueChange = { newValue ->
                if (field == "hashtag") {
                    val filteredValue = newValue
                        .split(" ")
                        .filter { it.startsWith("#") }
                        .joinToString(" ")
                    postViewModel.updateFieldValue(field, filteredValue)
                } else {
                    postViewModel.updateFieldValue(field, newValue)
                }
            },
            onDone = { postViewModel.stopEditingField() },
            maxCharacters = when (field) {
                "title" -> 28
                "location" -> 60
                "hashtag" -> 30
                else -> 0
            }
        )
    }
}



@Composable
fun ImageButton(
    @DrawableRes iconId: Int,
    description: String,
    size : Dp = 40.dp,
    onClick: () -> Unit,
) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = description,
        modifier = Modifier
            .size(size)
            .clickable { onClick() }
    )
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SavoreelTheme() {
        PostView(navController = rememberNavController())
    }
}