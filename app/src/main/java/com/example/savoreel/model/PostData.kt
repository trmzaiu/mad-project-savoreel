package com.example.savoreel.model

data class PostData(
    val postId: String = "",
    val userId: String = "",
    val name: String = "",
    val title: String = "",
    val hashtag: String = "",
    val location: String = "",
    val date: String = "",
    val photoUri: String = "",
    val reactions: Map<String, Int> = emptyMap()
)

