package com.example.pastel.data.local

import androidx.room.*
import com.example.pastel.data.remote.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    /*Add News to Database*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewsArticle(article: Article)

    /*Get News in the Database*/
    @Transaction
    @Query("SELECT * FROM news_article_table")
    fun readNewsArticle(): Flow<List<Article>>

    /*Delete news in the Database*/
    @Query("DELETE FROM news_article_table")
    suspend fun deleteNewsArticles()
}