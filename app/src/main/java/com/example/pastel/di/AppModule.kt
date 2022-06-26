package com.example.pastel.di

import android.content.Context
import androidx.room.Room
import com.example.pastel.data.local.NewsDao
import com.example.pastel.data.local.NewsDatabase
import com.example.pastel.data.local.NewsDatabase.Companion.DATABASE_NAME
import com.example.pastel.data.remote.ApiService
import com.example.pastel.data.repository.NewsRepositoryImpl
import com.example.pastel.domain.repository.NewsRepository
import com.example.pastel.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesStudentDataBase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(context, NewsDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesNewsDAO(database: NewsDatabase): NewsDao = database.newsDao()

    @Singleton
    @Provides
    fun provideNewsRepository(
        apiService: ApiService,
        database: NewsDatabase,
        @Dispatcher(MyDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): NewsRepository {
        return NewsRepositoryImpl(apiService, database, ioDispatcher)
    }
}