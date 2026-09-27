package com.example.data.adaptive

import com.example.data.datasource.CuratedAcademicContent
import com.example.data.local.entities.QuestionAttemptEntity
import com.example.data.model.ExamType
import com.example.data.model.PracticeQuestion
import com.example.data.model.Subject
import kotlin.math.roundToInt

data class TopicMastery(
    val chapterId: String,
    val chapterName: String,
    val subject: Subject,
    val totalAttempts: Int,
    val correctCount: Int,
    val accuracyPercentage: Float,
    val status: MasteryStatus
)

enum class MasteryStatus(val label: String, val colorHex: Long) {
    WEAK_NEEDS_FOCUS("Needs Urgent Focus (<50%)", 0xFFEF4444),
    DEVELOPING("Developing (50-75%)", 0xFFF59E0B),
    MASTERED("Mastered (>75%)", 0xFF10B981),
    UNTESTED("Untested Topic", 0xFF6366F1)
}

data class AdaptiveQuizConfig(
    val title: String = "Adaptive Diagnostic Challenge",
    val examType: ExamType = ExamType.ALL,
    val subject: Subject? = null,
    val classGrade: Int? = null,
    val questionCount: Int = 8,
    val timeLimitMinutes: Int = 10, // 0 = untimed
    val targetScore: Int = 24, // e.g. 6 correct (+24 marks)
    val focusOnWeakAreasOnly: Boolean = true
)

data class AdaptiveQuizSummary(
    val targetScore: Int,
    val achievedScore: Int,
    val totalQuestions: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val unattemptedCount: Int,
    val accuracy: Float,
    val timeSpentSeconds: Int,
    val timeLimitSeconds: Int,
    val weakTopicsIdentified: List<TopicMastery>,
    val recommendedActions: List<String>
)

object AdaptiveQuizEngine {

    fun computeTopicMasteries(
        attempts: List<QuestionAttemptEntity>,
        grade: Int? = null
    ): List<TopicMastery> {
        val attemptsByQuestionId = attempts.groupBy { it.questionId }
        val targetChapters = CuratedAcademicContent.chapters.filter {
            grade == null || it.classGrade == grade
        }

        return targetChapters.map { chapter ->
            val chapterQuestions = CuratedAcademicContent.practiceQuestions.filter { it.chapterId == chapter.id }
            val chapterAttempts = chapterQuestions.flatMap { q -> attemptsByQuestionId[q.id] ?: emptyList() }

            val total = chapterAttempts.size
            val correct = chapterAttempts.count { it.isCorrect }
            val accuracy = if (total > 0) (correct.toFloat() / total) * 100f else 0f

            val status = when {
                total == 0 -> MasteryStatus.UNTESTED
                accuracy < 50f -> MasteryStatus.WEAK_NEEDS_FOCUS
                accuracy <= 75f -> MasteryStatus.DEVELOPING
                else -> MasteryStatus.MASTERED
            }

            TopicMastery(
                chapterId = chapter.id,
                chapterName = chapter.name,
                subject = chapter.subject,
                totalAttempts = total,
                correctCount = correct,
                accuracyPercentage = accuracy,
                status = status
            )
        }
    }

    fun generateAdaptiveQuiz(
        config: AdaptiveQuizConfig,
        attempts: List<QuestionAttemptEntity>
    ): List<PracticeQuestion> {
        val masteries = computeTopicMasteries(attempts, config.classGrade)
        val allQuestions = CuratedAcademicContent.practiceQuestions.filter { q ->
            val gradeMatches = config.classGrade == null || q.classGrade == config.classGrade
            val examMatches = when (config.examType) {
                ExamType.ALL -> true
                ExamType.NEET -> q.examType == ExamType.NEET
                ExamType.JEE_MAIN, ExamType.JEE_ADVANCED -> q.examType != ExamType.NEET
            }
            val subjectMatches = config.subject == null || q.subject == config.subject
            gradeMatches && examMatches && subjectMatches
        }.ifEmpty {
            // If grade-specific pool is empty or small, fall back to matching exam or all
            CuratedAcademicContent.practiceQuestions.filter { q ->
                config.subject == null || q.subject == config.subject
            }
        }

        if (allQuestions.isEmpty()) {
            return CuratedAcademicContent.practiceQuestions.take(config.questionCount)
        }

        // Rank chapters by weakness
        val weakChapterIds = masteries.filter { it.status == MasteryStatus.WEAK_NEEDS_FOCUS || it.status == MasteryStatus.UNTESTED }
            .map { it.chapterId }
            .toSet()

        val developingChapterIds = masteries.filter { it.status == MasteryStatus.DEVELOPING }
            .map { it.chapterId }
            .toSet()

        // Partition questions
        val weakPool = allQuestions.filter { it.chapterId in weakChapterIds }.shuffled()
        val developingPool = allQuestions.filter { it.chapterId in developingChapterIds }.shuffled()
        val otherPool = allQuestions.filter { it.chapterId !in weakChapterIds && it.chapterId !in developingChapterIds }.shuffled()

        val selected = mutableListOf<PracticeQuestion>()

        // 60% from weak pool if available
        val weakTarget = (config.questionCount * 0.6f).roundToInt()
        selected.addAll(weakPool.take(weakTarget))

        // 25% from developing pool
        val devTarget = (config.questionCount * 0.25f).roundToInt()
        selected.addAll(developingPool.take(devTarget))

        // Fill remaining with other questions
        val remaining = config.questionCount - selected.size
        if (remaining > 0) {
            val remainingCandidates = (weakPool.drop(weakTarget) + developingPool.drop(devTarget) + otherPool)
                .distinctBy { it.id }
                .filter { it !in selected }
            selected.addAll(remainingCandidates.take(remaining))
        }

        // If still under question count, backfill from all available
        if (selected.size < config.questionCount) {
            val backfill = allQuestions.filter { it !in selected }
            selected.addAll(backfill.take(config.questionCount - selected.size))
        }

        return selected
    }

    fun evaluateQuiz(
        config: AdaptiveQuizConfig,
        userAnswers: Map<String, Int>, // questionId -> selectedOption
        questions: List<PracticeQuestion>,
        timeSpentSeconds: Int
    ): AdaptiveQuizSummary {
        var correct = 0
        var incorrect = 0
        var unattempted = 0
        var score = 0

        val weakChapterMistakes = mutableMapOf<String, Int>()

        questions.forEach { q ->
            val answer = userAnswers[q.id]
            if (answer == null || answer == -1) {
                unattempted++
            } else if (answer == q.correctIndex) {
                correct++
                score += 4
            } else {
                incorrect++
                score -= 1
                weakChapterMistakes[q.chapterId] = (weakChapterMistakes[q.chapterId] ?: 0) + 1
            }
        }

        val totalAnswered = correct + incorrect
        val accuracy = if (totalAnswered > 0) (correct.toFloat() / totalAnswered) * 100f else 0f

        val identifiedWeak = CuratedAcademicContent.chapters
            .filter { weakChapterMistakes.containsKey(it.id) }
            .map { chapter ->
                val mistakes = weakChapterMistakes[chapter.id] ?: 0
                TopicMastery(
                    chapterId = chapter.id,
                    chapterName = chapter.name,
                    subject = chapter.subject,
                    totalAttempts = mistakes,
                    correctCount = 0,
                    accuracyPercentage = 0f,
                    status = MasteryStatus.WEAK_NEEDS_FOCUS
                )
            }

        val actions = mutableListOf<String>()
        if (score >= config.targetScore) {
            actions.add("Target Score Achieved! You exceeded your goal of ${config.targetScore} marks.")
            actions.add("Ready for High-Yield JEE/NEET Advanced Diagnostic Sprint.")
        } else {
            actions.add("Target Score Missed by ${config.targetScore - score} marks. Focus on negative marking avoidance.")
        }

        identifiedWeak.forEach { weak ->
            actions.add("Review Video Lesson & NCERT Notes for: ${weak.chapterName}")
        }

        if (timeSpentSeconds > (config.timeLimitMinutes * 60) && config.timeLimitMinutes > 0) {
            actions.add("Pacing Alert: You exceeded the allocated time limit. Practice rapid elimination of obvious trap distractors.")
        }

        return AdaptiveQuizSummary(
            targetScore = config.targetScore,
            achievedScore = score,
            totalQuestions = questions.size,
            correctCount = correct,
            incorrectCount = incorrect,
            unattemptedCount = unattempted,
            accuracy = accuracy,
            timeSpentSeconds = timeSpentSeconds,
            timeLimitSeconds = config.timeLimitMinutes * 60,
            weakTopicsIdentified = identifiedWeak,
            recommendedActions = actions
        )
    }
}
