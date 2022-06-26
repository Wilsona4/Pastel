package com.example.pastel.data.remote.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_article_table")
data class Article(
    val author: String? = "",
    val content: String? = "",
    val description: String? = "",
    val publishedAt: String? = "",
    @Embedded(prefix = "news_article_source")
    val source: Source? = null,
    val title: String? = "",
    val url: String,
    val urlToImage: String? = ""
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}