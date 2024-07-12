package com.example.saalobjectsmanager.di

import android.content.Context
import com.example.saalobjectsmanager.data.AppDatabase
import com.example.saalobjectsmanager.data.SaalObjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideSaalObjectDao(appDatabase: AppDatabase): SaalObjectDao {
        return appDatabase.saalObjectDao()
    }

}