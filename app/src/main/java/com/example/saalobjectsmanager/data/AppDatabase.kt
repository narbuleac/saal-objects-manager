package com.example.saalobjectsmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.saalobjectsmanager.utilities.DATABASE_NAME

@Database(entities = [SaalObject::class, SaalObjectRelation::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun saalObjectDao(): SaalObjectDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}