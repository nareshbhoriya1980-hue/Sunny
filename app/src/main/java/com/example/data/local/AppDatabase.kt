package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AcademicDao
import com.example.data.local.entities.BookmarkEntity
import com.example.data.local.entities.FlashcardProgressEntity
import com.example.data.local.entities.HighlightEntity
import com.example.data.local.entities.MockTestResultEntity
import com.example.data.local.entities.QuestionAttemptEntity

@Database(
    entities = [
        BookmarkEntity::class,
        HighlightEntity::class,
        QuestionAttemptEntity::class,
        FlashcardProgressEntity::class,
        MockTestResultEntity::class,
        com.example.data.local.entities.VideoProgressEntity::class,
        com.example.data.local.entities.AdaptiveQuizResultEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun academicDao(): AcademicDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cognitive_academic.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
