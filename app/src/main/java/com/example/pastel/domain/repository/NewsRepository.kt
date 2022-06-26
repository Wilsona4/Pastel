package com.example.pastel.domain.repository

import com.example.pastel.data.remote.model.Article
import com.example.pastel.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(): Flow<Resource<List<Article>>>
}