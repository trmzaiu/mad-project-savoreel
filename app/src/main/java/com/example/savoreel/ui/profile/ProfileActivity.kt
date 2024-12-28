package com.example.savoreel.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.savoreel.R
import com.example.savoreel.model.Post
import com.example.savoreel.model.PostImage
import com.example.savoreel.model.ThemeViewModel
import com.example.savoreel.model.UserViewModel
import com.example.savoreel.model.getMonthName
import com.example.savoreel.model.posts
import com.example.savoreel.ui.component.BackArrow
import com.example.savoreel.ui.component.NavButton
import com.example.savoreel.ui.setting.SettingActivity
import com.example.savoreel.ui.theme.SavoreelTheme
import com.example.savoreel.ui.theme.nunitoFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeViewModel.loadUserSettings()

        setContent {
            val isDarkMode by themeViewModel.isDarkModeEnabled.observeAsState(initial = false)

            SavoreelTheme(darkTheme = isDarkMode) {
                ProfileScreen(
                    navigateToSetting = {
                        val intent = Intent(this, SettingActivity::class.java)
                        startActivity(intent)
                    },
                    navigateToFollow = { tab, userId ->
                        val intent = Intent(this, FollowActivity::class.java)
                        intent.putExtra("TAB", tab)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getUser(
            onSuccess = { user -> userViewModel.setUser(user) },
            onFailure = { error -> Log.e("ProfileActivity", "Error retrieving user: $error") }
        )
    }
}

@Composable
fun ProfileScreen(navigateToSetting: () -> Unit, navigateToFollow: (String, String) -> Unit) {
    val userViewModel: UserViewModel = viewModel()
    val listState = rememberLazyListState()
    var isRowVisible by remember { mutableStateOf(true) }
    var numberOfFolower by remember { mutableIntStateOf(0) }
    var numberOfFollowing by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }
    var imgUrl by remember { mutableStateOf("") }
    var uid by remember {mutableStateOf("")}
    var isLoading by remember { mutableStateOf(true) }

    val currentUser by userViewModel.user.collectAsState()

    LaunchedEffect(currentUser) {
        userViewModel.getUser(
            onSuccess = { currentUser ->
                if (currentUser != null) {
                    uid = currentUser.userId.toString()
                    name = currentUser.name.toString()
                    imgUrl = currentUser.avatarUrl.toString()
                    numberOfFolower = currentUser.followers.size
                    numberOfFollowing = currentUser.following.size
                } else {
                    Log.e("ProfileScreen", "User data not found")
                }
                isLoading = false
            },
            onFailure = { error ->
                Log.e("NameTheme", "Error retrieving user: $error")
                isLoading = false
            }
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { scrollOffset ->
                isRowVisible = scrollOffset < 200
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isRowVisible) {
                            NavButton(
                                painter = painterResource(R.drawable.default_avatar),
                                onClickAction = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        listState.scrollToItem(0)
                                    }},
                                isChecked = true
                            )
                            Text(
                                text = name,
                                fontFamily = nunitoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                    Row {
                        NavButton(
                            painter = painterResource(R.drawable.ic_setting),
                            onClickAction = { navigateToSetting() }
                        )
                        BackArrow(
                            modifier = Modifier
                                .padding(0.dp)
                                .size(48.dp)
                                .rotate(180f)
                        )
                    }
                }
                if (isRowVisible) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            UserAvatar(
                                userId = uid,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(50))
                            )
                            Text(
                                text = name,
                                fontFamily = nunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(top = 5.dp)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    navigateToFollow("following", uid)
                                }
                        ) {
                            Text(
                                text = numberOfFollowing.toString(),
                                fontFamily = nunitoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                text = "Following",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    navigateToFollow("follower", uid)
                                }
                        ) {
                            Text(
                                text = numberOfFolower.toString(),
                                fontFamily = nunitoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                text = "Followers",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    posts.forEach { (key, groupedPosts) ->
                        item {
                            CalendarWithImages(
                                posts = groupedPosts,
                                title = "${getMonthName(key.second)}, ${key.first}"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CalendarWithImages(
    posts: List<Post>,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
                .height(50.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 10.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        FlowRow(
            modifier = Modifier
                .padding(5.dp, 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            posts.forEach { post ->
                PostImage(
                    url = post.imageRes.toString(),
                    modifier = Modifier
                        .size(60.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(15))
                        .clickable {
                            println("Clicked on post ID: ${post.postId}")
                            //chanh à nếu m làm tới đây bạn sẽ thấy tôi đợi bạn
                        }
                )
            }
        }
    }
}

@Composable
fun UserAvatar(
    userId: String,
    modifier: Modifier = Modifier
) {
    var avatarUrl by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf<String?>(null) }
    val userViewModel: UserViewModel = viewModel()

    LaunchedEffect(userId) {
        if (userId.isBlank()) return@LaunchedEffect

        try {
            userViewModel.getUserById(
                userId = userId,
                onSuccess = { user ->
                    avatarUrl = user?.avatarUrl.toString()
                    name = user?.name.toString()
                    error = null
                },
                onFailure = { errorMsg ->
                    error = errorMsg
                    avatarUrl = null
                }
            )
        } catch (e: Exception) {
            error = e.message
            avatarUrl = null
        }
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        if (avatarUrl != null) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "User avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center,
            ) {
                name?.take(1)?.let {
                    Text(
                        text = it.uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}