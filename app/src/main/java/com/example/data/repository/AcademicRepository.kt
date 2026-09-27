package com.example.data.repository

import com.example.data.datasource.CuratedAcademicContent
import com.example.data.local.dao.AcademicDao
import com.example.data.local.entities.AdaptiveQuizResultEntity
import com.example.data.local.entities.BookmarkEntity
import com.example.data.local.entities.FlashcardProgressEntity
import com.example.data.local.entities.HighlightEntity
import com.example.data.local.entities.MockTestResultEntity
import com.example.data.local.entities.QuestionAttemptEntity
import com.example.data.local.entities.VideoProgressEntity
import com.example.data.model.Chapter
import com.example.data.model.ExamType
import com.example.data.model.FlashcardItem
import com.example.data.model.FormulaCard
import com.example.data.model.PracticeQuestion
import com.example.data.model.Subject
import kotlinx.coroutines.flow.Flow

class AcademicRepository(private val dao: AcademicDao) {

    // Content access filtered by Class Grade (5th to 12th) & Exam Type
    fun getChapters(
        selectedExam: ExamType = ExamType.ALL,
        selectedSubject: Subject? = null,
        selectedGrade: Int? = null
    ): List<Chapter> {
        return CuratedAcademicContent.chapters.filter { chapter ->
            val gradeMatches = selectedGrade == null || chapter.classGrade == selectedGrade
            val examMatches = when (selectedExam) {
                ExamType.ALL -> true
                ExamType.NEET -> chapter.targetExam == ExamType.NEET || chapter.targetExam == ExamType.ALL
                ExamType.JEE_MAIN, ExamType.JEE_ADVANCED -> chapter.targetExam != ExamType.NEET
            }
            val subjectMatches = selectedSubject == null || chapter.subject == selectedSubject
            gradeMatches && examMatches && subjectMatches
        }
    }

    fun getChapterById(chapterId: String): Chapter? {
        return CuratedAcademicContent.chapters.firstOrNull { it.id == chapterId }
    }

    fun getFormulas(
        query: String = "",
        subject: Subject? = null,
        selectedGrade: Int? = null
    ): List<FormulaCard> {
        return CuratedAcademicContent.formulas.filter { formula ->
            val gradeMatches = selectedGrade == null || formula.classGrade == selectedGrade
            val queryMatches = query.isBlank() ||
                    formula.name.contains(query, ignoreCase = true) ||
                    formula.crucialCaveats.contains(query, ignoreCase = true) ||
                    formula.chapterName.contains(query, ignoreCase = true)
            val subjectMatches = subject == null || formula.subject == subject
            gradeMatches && queryMatches && subjectMatches
        }
    }

    fun getPracticeQuestions(
        selectedExam: ExamType = ExamType.ALL,
        selectedSubject: Subject? = null,
        selectedGrade: Int? = null
    ): List<PracticeQuestion> {
        return CuratedAcademicContent.practiceQuestions.filter { q ->
            val gradeMatches = selectedGrade == null || q.classGrade == selectedGrade
            val examMatches = when (selectedExam) {
                ExamType.ALL -> true
                ExamType.NEET -> q.examType == ExamType.NEET
                ExamType.JEE_MAIN, ExamType.JEE_ADVANCED -> q.examType != ExamType.NEET
            }
            val subjectMatches = selectedSubject == null || q.subject == selectedSubject
            gradeMatches && examMatches && subjectMatches
        }
    }

    fun getFlashcards(selectedGrade: Int? = null): List<FlashcardItem> {
        return CuratedAcademicContent.flashcards.filter {
            selectedGrade == null || it.classGrade == selectedGrade
        }
    }

    // Reactive DB observations
    val allBookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    val allHighlights: Flow<List<HighlightEntity>> = dao.getAllHighlights()
    val allAttempts: Flow<List<QuestionAttemptEntity>> = dao.getAllAttempts()
    val allFlashcardProgress: Flow<List<FlashcardProgressEntity>> = dao.getAllFlashcardProgress()
    val allMockTestResults: Flow<List<MockTestResultEntity>> = dao.getAllMockTestResults()
    val allAdaptiveQuizResults: Flow<List<AdaptiveQuizResultEntity>> = dao.getAllAdaptiveQuizResults()

    fun isBookmarked(referenceId: String): Flow<Boolean> = dao.isBookmarked(referenceId)

    fun getHighlightsForSection(sectionId: String): Flow<List<HighlightEntity>> =
        dao.getHighlightsForSection(sectionId)

    fun getVideoProgressForChapter(chapterId: String): Flow<VideoProgressEntity?> =
        dao.getVideoProgressForChapter(chapterId)

    // Mutations
    suspend fun toggleBookmark(
        referenceId: String,
        type: String,
        title: String,
        subtitle: String,
        subject: String
    ) {
        dao.deleteBookmarkByReference(referenceId)
        dao.insertBookmark(
            BookmarkEntity(
                type = type,
                referenceId = referenceId,
                title = title,
                subtitle = subtitle,
                subject = subject
            )
        )
    }

    suspend fun removeBookmarkByReference(referenceId: String) {
        dao.deleteBookmarkByReference(referenceId)
    }

    suspend fun removeBookmarkById(id: Long) {
        dao.deleteBookmarkById(id)
    }

    suspend fun addHighlight(
        chapterId: String,
        sectionId: String,
        textSnippet: String,
        colorHex: Long,
        note: String = ""
    ): Long {
        return dao.insertHighlight(
            HighlightEntity(
                chapterId = chapterId,
                sectionId = sectionId,
                textSnippet = textSnippet,
                colorHex = colorHex,
                note = note
            )
        )
    }

    suspend fun deleteHighlight(id: Long) {
        dao.deleteHighlightById(id)
    }

    suspend fun recordAttempt(
        questionId: String,
        selectedOption: Int,
        isCorrect: Boolean,
        examType: String,
        subject: String
    ) {
        val scoreDelta = if (isCorrect) 4 else -1
        dao.insertAttempt(
            QuestionAttemptEntity(
                questionId = questionId,
                selectedOption = selectedOption,
                isCorrect = isCorrect,
                examType = examType,
                subject = subject,
                scoreDelta = scoreDelta
            )
        )
    }

    suspend fun updateVideoProgress(
        videoId: String,
        chapterId: String,
        currentPosSec: Int,
        totalSec: Int,
        isCompleted: Boolean
    ) {
        dao.upsertVideoProgress(
            VideoProgressEntity(
                videoId = videoId,
                chapterId = chapterId,
                lastPositionSeconds = currentPosSec,
                totalDurationSeconds = totalSec,
                isCompleted = isCompleted,
                lastWatchedTimestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateFlashcardReview(cardId: String, wasCorrect: Boolean) {
        val progress = FlashcardProgressEntity(
            cardId = cardId,
            boxStage = if (wasCorrect) 2 else 1,
            timesReviewed = 1,
            timesCorrect = if (wasCorrect) 1 else 0,
            lastReviewedTimestamp = System.currentTimeMillis(),
            isMastered = wasCorrect
        )
        dao.upsertFlashcardProgress(progress)
    }

    suspend fun saveAdaptiveQuizResult(result: AdaptiveQuizResultEntity) {
        dao.insertAdaptiveQuizResult(result)
    }

    suspend fun saveMockTestResult(
        title: String,
        examType: String,
        score: Int,
        maxScore: Int,
        correct: Int,
        incorrect: Int,
        unattempted: Int,
        durationSeconds: Int
    ) {
        val accuracy = if (correct + incorrect > 0) (correct.toFloat() / (correct + incorrect)) * 100f else 0f
        dao.insertMockTestResult(
            MockTestResultEntity(
                examTitle = title,
                examType = examType,
                score = score,
                maxScore = maxScore,
                correctCount = correct,
                incorrectCount = incorrect,
                unattemptedCount = unattempted,
                accuracy = accuracy,
                durationSeconds = durationSeconds
            )
        )
    }
}
