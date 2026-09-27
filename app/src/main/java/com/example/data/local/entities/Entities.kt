package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "CHAPTER", "SECTION", "FORMULA", "QUESTION"
    val referenceId: String,
    val title: String,
    val subtitle: String,
    val subject: String,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "highlights")
data class HighlightEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chapterId: String,
    val sectionId: String,
    val textSnippet: String,
    val colorHex: Long, // 0xFFF59E0B (Gold), 0xFF6366F1 (Violet), 0xFF10B981 (Emerald), 0xFFEF4444 (Crimson)
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "question_attempts")
data class QuestionAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionId: String,
    val selectedOption: Int,
    val isCorrect: Boolean,
    val examType: String,
    val subject: String,
    val scoreDelta: Int, // +4 or -1
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "flashcard_progress")
data class FlashcardProgressEntity(
    @PrimaryKey val cardId: String,
    val boxStage: Int = 1, // Leitner 1..5
    val timesReviewed: Int = 0,
    val timesCorrect: Int = 0,
    val lastReviewedTimestamp: Long = 0L,
    val isMastered: Boolean = false
)

@Entity(tableName = "mock_test_results")
data class MockTestResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val examTitle: String,
    val examType: String,
    val score: Int,
    val maxScore: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val unattemptedCount: Int,
    val accuracy: Float,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "video_progress")
data class VideoProgressEntity(
    @PrimaryKey val videoId: String,
    val chapterId: String,
    val lastPositionSeconds: Int = 0,
    val totalDurationSeconds: Int = 0,
    val isCompleted: Boolean = false,
    val lastWatchedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "adaptive_quiz_results")
data class AdaptiveQuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val examType: String,
    val targetScore: Int,
    val achievedScore: Int,
    val totalQuestions: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val timeLimitSeconds: Int,
    val timeSpentSeconds: Int,
    val identifiedWeakTopics: String,
    val difficultyDistribution: String,
    val timestamp: Long = System.currentTimeMillis()
)


