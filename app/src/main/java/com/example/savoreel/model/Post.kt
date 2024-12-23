package com.example.savoreel.model

import android.annotation.SuppressLint
import com.example.savoreel.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Post(
    val postid: String,
    val userid: String,
    val title: String,
    val imageRes: Int,
    val datetime: Date,
)

@SuppressLint("NewApi")
val postss = List(200) { i ->
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2023)
        set(Calendar.MONTH, i % 12)
        set(Calendar.DAY_OF_MONTH, i % 28 + 1)
        set(Calendar.HOUR_OF_DAY, 10)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    Post(
        postid = "${i + 1}",
        userid = "${i + 1}",
        title = "Post ${i + 1}",
        imageRes = R.drawable.food,
        datetime = calendar.time
    )
}

val posts = groupPostsByMonth(postss)

@SuppressLint("NewApi")
fun groupPostsByMonth(posts: List<Post>): Map<Pair<Int, Int>, List<Post>> {
    val comparator = compareByDescending<Pair<Int, Int>> { it.first }
        .thenByDescending { it.second }

    return posts.groupBy { post ->
        val calendar = Calendar.getInstance()
        calendar.time = post.datetime
        Pair(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
    }.mapValues { entry ->
        entry.value.sortedByDescending { it.datetime }
    }.toSortedMap(comparator)
}

fun getMonthName(month: Int): String {
    val dateFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
    val calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, month - 1)
    }
    return dateFormat.format(calendar.time)
}