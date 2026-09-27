package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.BookmarkEntity
import com.example.data.local.entities.FlashcardProgressEntity
import com.example.data.local.entities.HighlightEntity
import com.example.data.local.entities.MockTestResultEntity
import com.example.data.local.entities.QuestionAttemptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AcademicDao {

    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE referenceId = :referenceId LIMIT 1)")
    fun isBookmarked(referenceId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long

    @Query("DELETE FROM bookmarks WHERE referenceId = :referenceId")
    suspend fun deleteBookmarkByReference(referenceId: String)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: Long)

    // Highlights
    @Query("SELECT * FROM highlights WHERE sectionId = :sectionId ORDER BY createdAt ASC")
    fun getHighlightsForSection(sectionId: String): Flow<List<HighlightEntity>>

    @Query("SELECT * FROM highlights ORDER BY createdAt DESC")
    fun getAllHighlights(): Flow<List<HighlightEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: HighlightEntity): Long

    @Query("DELETE FROM highlights WHERE id = :id")
    suspend fun deleteHighlightById(id: Long)

    // Question Attempts
    @Query("SELECT * FROM question_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<QuestionAttemptEntity>>

    @Query("SELECT * FROM question_attempts WHERE questionId = :questionId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestAttemptForQuestion(questionId: String): Flow<QuestionAttemptEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuestionAttemptEntity): Long

    // Flashcard Progress
    @Query("SELECT * FROM flashcard_progress")
    fun getAllFlashcardProgress(): Flow<List<FlashcardProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFlashcardProgress(progress: FlashcardProgressEntity)

    // Mock Test Results
    @Query("SELECT * FROM mock_test_results ORDER BY timestamp DESC")
    fun getAllMockTestResults(): Flow<List<MockTestResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMockTestResult(result: MockTestResultEntity): Long

    // Video Progress
    @Query("SELECT * FROM video_progress")
    fun getAllVideoProgress(): Flow<List<com.example.data.local.entities.VideoProgressEntity>>

    @Query("SELECT * FROM video_progress WHERE chapterId = :chapterId LIMIT 1")
    fun getVideoProgressForChapter(chapterId: String): Flow<com.example.data.local.entities.VideoProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertVideoProgress(progress: com.example.data.local.entities.VideoProgressEntity)

    // Adaptive Quiz Results
    @Query("SELECT * FROM adaptive_quiz_results ORDER BY timestamp DESC")
    fun getAllAdaptiveQuizResults(): Flow<List<com.example.data.local.entities.AdaptiveQuizResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdaptiveQuizResult(result: com.example.data.local.entities.AdaptiveQuizResultEntity): Long
}
