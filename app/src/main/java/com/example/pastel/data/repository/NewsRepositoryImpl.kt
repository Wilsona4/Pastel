package com.example.pastel.data.repository

import androidx.room.withTransaction
import com.example.pastel.data.local.NewsDatabase
import com.example.pastel.data.remote.ApiService
import com.example.pastel.data.remote.model.Article
import com.example.pastel.di.Dispatcher
import com.example.pastel.di.MyDispatchers
import com.example.pastel.domain.repository.NewsRepository
import com.example.pastel.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val newsDatabase: NewsDatabase,
    @Dispatcher(MyDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : NewsRepository {
    override suspend fun getNews(): Flow<Resource<List<Article>>> = flow {

        val newsDao = newsDatabase.newsDao()

        emit(Resource.Loading())

        val cachedData = newsDao.readNewsArticle().first()

        if (cachedData.isNotEmpty()) {
            emit(Resource.Success(cachedData))
        }

        val remoteData = try {
            apiService.getNews()
        } catch (e: Exception) {
            newsDao.readNewsArticle().collect { newsArticles ->
                emit(Resource.Error(e.message ?: "Something went wrong", newsArticles))
            }
            null
        }

        remoteData?.let { newsArticles ->
            newsDatabase.withTransaction {
                newsDao.deleteNewsArticles()
                newsArticles.articles.forEach { article ->
                    newsDao.addNewsArticle(article)
                }
            }
            newsDao.readNewsArticle().collect {
                emit(Resource.Success(it))
            }
        }

    }.flowOn(ioDispatcher)
}