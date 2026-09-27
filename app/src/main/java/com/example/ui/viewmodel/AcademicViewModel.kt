package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiExplanationService
import com.example.data.adaptive.AdaptiveQuizSummary
import com.example.data.local.AppDatabase
import com.example.data.local.entities.AdaptiveQuizResultEntity
import com.example.data.local.entities.BookmarkEntity
import com.example.data.local.entities.FlashcardProgressEntity
import com.example.data.local.entities.HighlightEntity
import com.example.data.local.entities.QuestionAttemptEntity
import com.example.data.model.Chapter
import com.example.data.model.ExamType
import com.example.data.model.FlashcardItem
import com.example.data.model.FormulaCard
import com.example.data.model.PracticeQuestion
import com.example.data.model.Subject
import com.example.data.repository.AcademicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AcademicViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = AcademicRepository(database.academicDao())
    val explanationService = AiExplanationService()

    // Filters & Navigation State
    private val _selectedGrade = MutableStateFlow(12) // Default Class 12th (NEET/JEE Senior)
    val selectedGrade: StateFlow<Int> = _selectedGrade.asStateFlow()

    private val _selectedExam = MutableStateFlow(ExamType.ALL)
    val selectedExam: StateFlow<ExamType> = _selectedExam.asStateFlow()

    private val _selectedSubject = MutableStateFlow<Subject?>(null)
    val selectedSubject: StateFlow<Subject?> = _selectedSubject.asStateFlow()

    // Active Tab: 0 = Textbook Reader, 1 = Adaptive Quiz, 2 = Formula Vault, 3 = Spaced Flashcards
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    // Formula / Text Explainer Modal state
    private val _isExplainerOpen = MutableStateFlow(false)
    val isExplainerOpen: StateFlow<Boolean> = _isExplainerOpen.asStateFlow()

    private val _explainerQuery = MutableStateFlow("")
    val explainerQuery: StateFlow<String> = _explainerQuery.asStateFlow()

    // Reactive DB Observations
    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val highlights: StateFlow<List<HighlightEntity>> = repository.allHighlights
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attempts: StateFlow<List<QuestionAttemptEntity>> = repository.allAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flashcardProgress: StateFlow<List<FlashcardProgressEntity>> = repository.allFlashcardProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adaptiveQuizHistory: StateFlow<List<AdaptiveQuizResultEntity>> = repository.allAdaptiveQuizResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Content
    fun getFilteredChapters(): List<Chapter> {
        return repository.getChapters(
            selectedExam = _selectedExam.value,
            selectedSubject = _selectedSubject.value,
            selectedGrade = _selectedGrade.value
        )
    }

    fun getFilteredFormulas(query: String = ""): List<FormulaCard> {
        return repository.getFormulas(
            query = query,
            subject = _selectedSubject.value,
            selectedGrade = _selectedGrade.value
        )
    }

    fun getFilteredPracticeQuestions(): List<PracticeQuestion> {
        return repository.getPracticeQuestions(
            selectedExam = _selectedExam.value,
            selectedSubject = _selectedSubject.value,
            selectedGrade = _selectedGrade.value
        )
    }

    fun getFilteredFlashcards(): List<FlashcardItem> {
        return repository.getFlashcards(_selectedGrade.value)
    }

    // Actions
    fun setSelectedGrade(grade: Int) {
        _selectedGrade.value = grade
    }

    fun setSelectedExam(exam: ExamType) {
        _selectedExam.value = exam
    }

    fun setSelectedSubject(subject: Subject?) {
        _selectedSubject.value = subject
    }

    fun setCurrentTab(tab: Int) {
        _currentTab.value = tab
    }

    fun openExplainer(query: String) {
        _explainerQuery.value = query
        _isExplainerOpen.value = true
    }

    fun closeExplainer() {
        _isExplainerOpen.value = false
    }

    fun toggleBookmark(referenceId: String, type: String, title: String, subtitle: String, subject: String) {
        viewModelScope.launch {
            repository.toggleBookmark(referenceId, type, title, subtitle, subject)
        }
    }

    fun addHighlight(chapterId: String, sectionId: String, text: String, colorHex: Long) {
        viewModelScope.launch {
            repository.addHighlight(chapterId, sectionId, text, colorHex)
        }
    }

    fun recordQuestionAttempt(questionId: String, selectedOption: Int, isCorrect: Boolean, examType: String, subject: String) {
        viewModelScope.launch {
            repository.recordAttempt(questionId, selectedOption, isCorrect, examType, subject)
        }
    }

    fun reviewFlashcard(cardId: String, wasCorrect: Boolean) {
        viewModelScope.launch {
            repository.updateFlashcardReview(cardId, wasCorrect)
        }
    }

    fun saveAdaptiveQuizResult(summary: AdaptiveQuizSummary) {
        viewModelScope.launch {
            val weakTopics = summary.weakTopicsIdentified.joinToString(", ") { it.chapterName }
            repository.saveAdaptiveQuizResult(
                AdaptiveQuizResultEntity(
                    title = "Adaptive Quiz (Class ${_selectedGrade.value})",
                    examType = _selectedExam.value.name,
                    targetScore = summary.targetScore,
                    achievedScore = summary.achievedScore,
                    totalQuestions = summary.totalQuestions,
                    correctCount = summary.correctCount,
                    incorrectCount = summary.incorrectCount,
                    timeLimitSeconds = summary.timeLimitSeconds,
                    timeSpentSeconds = summary.timeSpentSeconds,
                    identifiedWeakTopics = weakTopics,
                    difficultyDistribution = "Adaptive Multi-Tier",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
