package com.example.pastel.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pastel.data.remote.model.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        var DATABASE_NAME: String = "news_db"
    }
}