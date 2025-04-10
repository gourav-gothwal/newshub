package com.example.testapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testapp.data.local.dao.BookmarkDao
import com.example.testapp.data.local.entities.BookmarkedArticle

@Database(entities = [BookmarkedArticle::class], version = 2, exportSchema = false) // Change from 1 to 2
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: BookmarkDatabase? = null

        fun getDatabase(context: Context): BookmarkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDatabase::class.java,
                    "bookmark_database"
                )
                    .fallbackToDestructiveMigration() // This will clear old database if schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

