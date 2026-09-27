package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.adaptive.AdaptiveQuizConfig
import com.example.data.adaptive.AdaptiveQuizEngine
import com.example.data.adaptive.AdaptiveQuizSummary
import com.example.data.adaptive.MasteryStatus
import com.example.data.adaptive.TopicMastery
import com.example.data.local.entities.QuestionAttemptEntity
import com.example.data.model.ExamType
import com.example.data.model.PracticeQuestion
import com.example.data.model.Subject
import com.example.ui.theme.AcademicBackground
import com.example.ui.theme.AcademicElectricViolet
import com.example.ui.theme.AcademicPrimary
import com.example.ui.theme.CriticalAlertCrimson
import com.example.ui.theme.HighYieldGold
import com.example.ui.theme.MasteryEmerald
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdaptiveQuizScreen(
    userAttempts: List<QuestionAttemptEntity>,
    selectedGrade: Int,
    onRecordAttempt: (String, Int, Boolean, String, String) -> Unit,
    onSaveQuizSummary: (AdaptiveQuizSummary) -> Unit,
    onOpenVideoLesson: (String) -> Unit,
    onOpenFormulaExplainer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var quizState by remember { mutableStateOf(QuizState.CONFIG) } // CONFIG, RUNNING, SUMMARY

    // Configuration
    var targetScore by remember { mutableIntStateOf(20) }
    var timeLimitMinutes by remember { mutableIntStateOf(10) }
    var questionCount by remember { mutableIntStateOf(6) }
    var focusWeakOnly by remember { mutableStateOf(true) }

    // Active Quiz runtime
    var currentQuestions by remember { mutableStateOf<List<PracticeQuestion>>(emptyList()) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<String, Int>() }
    var secondsRemaining by remember { mutableIntStateOf(timeLimitMinutes * 60) }
    var timeElapsedSeconds by remember { mutableIntStateOf(0) }
    var quizSummaryResult by remember { mutableStateOf<AdaptiveQuizSummary?>(null) }

    // Real-time mastery analytics
    val masteries = remember(userAttempts, selectedGrade) {
        AdaptiveQuizEngine.computeTopicMasteries(userAttempts, selectedGrade)
    }

    // Timer countdown
    LaunchedEffect(quizState, secondsRemaining) {
        if (quizState == QuizState.RUNNING && timeLimitMinutes > 0 && secondsRemaining > 0) {
            delay(1000)
            secondsRemaining--
            timeElapsedSeconds++
            if (secondsRemaining <= 0) {
                // Auto submit on time expiry
                val summary = AdaptiveQuizEngine.evaluateQuiz(
                    config = AdaptiveQuizConfig(
                        targetScore = targetScore,
                        timeLimitMinutes = timeLimitMinutes,
                        questionCount = questionCount
                    ),
                    userAnswers = userAnswers,
                    questions = currentQuestions,
                    timeSpentSeconds = timeElapsedSeconds
                )
                quizSummaryResult = summary
                onSaveQuizSummary(summary)
                quizState = QuizState.SUMMARY
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AcademicBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        when (quizState) {
            QuizState.CONFIG -> {
                // Topic Mastery Overview Card
                item {
                    MasteryDiagnosticCard(masteries = masteries)
                }

                // Configuration Box
                item {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CrisisAlert,
                                    contentDescription = "Adaptive Engine",
                                    tint = AcademicElectricViolet,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Adaptive Practice Quiz Setup",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Calibrated for Class $selectedGrade • Auto-tunes to Weak Areas",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Target Score Slider
                            Text(
                                text = "Target Score Goal: $targetScore marks (+4 / -1 scheme)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Slider(
                                value = targetScore.toFloat(),
                                onValueChange = { targetScore = it.toInt() },
                                valueRange = 12f..36f,
                                steps = 5,
                                colors = SliderDefaults.colors(
                                    thumbColor = AcademicElectricViolet,
                                    activeTrackColor = AcademicElectricViolet
                                ),
                                modifier = Modifier.testTag("target_score_slider")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Time Limit Selector
                            Text(
                                text = "Time Limit:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(5 to "5 Min Blitz", 10 to "10 Min Standard", 15 to "15 Min Deep", 0 to "Untimed").forEach { (mins, label) ->
                                    val isSelected = timeLimitMinutes == mins
                                    Surface(
                                        shape = RoundedCornerShape(percent = 50),
                                        color = if (isSelected) AcademicPrimary else Color(0xFFF2F3FF),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) AcademicPrimary else Color(0xFFDAE2FD)
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { timeLimitMinutes = mins }
                                    ) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) Color.White else AcademicPrimary,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            ),
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Mode Selection
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Prioritize Identified Weak Topics",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Feeds 60% of questions from topics with low accuracy or past mistakes",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(percent = 50),
                                    color = if (focusWeakOnly) MasteryEmerald else Color.LightGray,
                                    modifier = Modifier.clickable { focusWeakOnly = !focusWeakOnly }
                                ) {
                                    Text(
                                        text = if (focusWeakOnly) "ENABLED" else "DISABLED",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Launch Button
                            Button(
                                onClick = {
                                    val config = AdaptiveQuizConfig(
                                        targetScore = targetScore,
                                        timeLimitMinutes = timeLimitMinutes,
                                        questionCount = questionCount,
                                        classGrade = selectedGrade,
                                        focusOnWeakAreasOnly = focusWeakOnly
                                    )
                                    val questions = AdaptiveQuizEngine.generateAdaptiveQuiz(config, userAttempts)
                                    currentQuestions = questions
                                    currentQuestionIndex = 0
                                    userAnswers.clear()
                                    secondsRemaining = timeLimitMinutes * 60
                                    timeElapsedSeconds = 0
                                    quizState = QuizState.RUNNING
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AcademicPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("start_adaptive_quiz_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Start Adaptive Diagnostic Quiz",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            QuizState.RUNNING -> {
                val question = currentQuestions.getOrNull(currentQuestionIndex)
                if (question != null) {
                    // Quiz Top Status Bar
                    item {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Question counter
                                Text(
                                    text = "Q ${currentQuestionIndex + 1} of ${currentQuestions.size}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )

                                // Adaptive difficulty pill
                                Surface(
                                    shape = RoundedCornerShape(percent = 50),
                                    color = when (question.difficulty) {
                                        "Hard" -> CriticalAlertCrimson.copy(alpha = 0.15f)
                                        "Medium" -> HighYieldGold.copy(alpha = 0.15f)
                                        else -> MasteryEmerald.copy(alpha = 0.15f)
                                    }
                                ) {
                                    Text(
                                        text = "Adaptive: ${question.difficulty}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = when (question.difficulty) {
                                                "Hard" -> CriticalAlertCrimson
                                                "Medium" -> Color(0xFFB45309)
                                                else -> Color(0xFF047857)
                                            }
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                // Countdown Timer
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = "Timer",
                                        tint = if (secondsRemaining < 60 && timeLimitMinutes > 0) CriticalAlertCrimson else AcademicPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (timeLimitMinutes > 0) formatMinutesSeconds(secondsRemaining) else "Untimed",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (secondsRemaining < 60 && timeLimitMinutes > 0) CriticalAlertCrimson else AcademicPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Question Card
                    item {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = question.yearTag,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AcademicElectricViolet,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = question.subject.displayName,
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = question.questionText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        lineHeight = 26.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Options
                                val selected = userAnswers[question.id]
                                question.options.forEachIndexed { optIndex, optText ->
                                    val isOptionSelected = selected == optIndex
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isOptionSelected) Color(0xFFF2F3FF) else Color(0xFFF8FAFC),
                                        border = androidx.compose.foundation.BorderStroke(
                                            if (isOptionSelected) 2.dp else 1.dp,
                                            if (isOptionSelected) AcademicPrimary else Color(0xFFE2E8F0)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable {
                                                userAnswers[question.id] = optIndex
                                                val isCorrect = optIndex == question.correctIndex
                                                onRecordAttempt(
                                                    question.id,
                                                    optIndex,
                                                    isCorrect,
                                                    question.examType.name,
                                                    question.subject.name
                                                )
                                            }
                                            .testTag("option_button_$optIndex")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // 20px Authentic Radio Indicator
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .border(2.dp, if (isOptionSelected) AcademicPrimary else Color(0xFFCBD5E1), CircleShape)
                                                    .background(if (isOptionSelected) AcademicPrimary else Color.Transparent, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isOptionSelected) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(8.dp)
                                                            .background(Color.White, CircleShape)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Text(
                                                text = "${('A' + optIndex)}.  $optText",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isOptionSelected) FontWeight.SemiBold else FontWeight.Normal,
                                                    color = AcademicPrimary
                                                )
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Navigation Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            if (currentQuestionIndex > 0) currentQuestionIndex--
                                        },
                                        enabled = currentQuestionIndex > 0,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Previous")
                                    }

                                    if (currentQuestionIndex < currentQuestions.size - 1) {
                                        Button(
                                            onClick = { currentQuestionIndex++ },
                                            colors = ButtonDefaults.buttonColors(containerColor = AcademicPrimary),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.testTag("next_question_button")
                                        ) {
                                            Text("Next Question")
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                val summary = AdaptiveQuizEngine.evaluateQuiz(
                                                    config = AdaptiveQuizConfig(
                                                        targetScore = targetScore,
                                                        timeLimitMinutes = timeLimitMinutes,
                                                        questionCount = questionCount
                                                    ),
                                                    userAnswers = userAnswers,
                                                    questions = currentQuestions,
                                                    timeSpentSeconds = timeElapsedSeconds
                                                )
                                                quizSummaryResult = summary
                                                onSaveQuizSummary(summary)
                                                quizState = QuizState.SUMMARY
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = MasteryEmerald),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.testTag("finish_quiz_button")
                                        ) {
                                            Text("Submit & Diagnose")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            QuizState.SUMMARY -> {
                val summary = quizSummaryResult
                if (summary != null) {
                    item {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (summary.achievedScore >= summary.targetScore) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (summary.achievedScore >= summary.targetScore) MasteryEmerald else CriticalAlertCrimson,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (summary.achievedScore >= summary.targetScore) "Target Score Achieved!" else "Target Goal Missed",
                                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "Adaptive Diagnostic Scorecard • Class $selectedGrade",
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Score & Accuracy Grid
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    ScoreStatItem(label = "Score", value = "${summary.achievedScore} / ${summary.totalQuestions * 4}")
                                    ScoreStatItem(label = "Target", value = "${summary.targetScore}")
                                    ScoreStatItem(label = "Accuracy", value = "${summary.accuracy.toInt()}%")
                                    ScoreStatItem(label = "Time", value = formatMinutesSeconds(summary.timeSpentSeconds))
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Weak Areas Identified
                                if (summary.weakTopicsIdentified.isNotEmpty()) {
                                    Text(
                                        text = "Identified Weak Areas Requiring Immediate Focus:",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = CriticalAlertCrimson
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    summary.weakTopicsIdentified.forEach { weak ->
                                        Surface(
                                            color = Color(0xFFFEE2E2),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 3.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column {
                                                    Text(
                                                        text = weak.chapterName,
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            color = CriticalAlertCrimson
                                                        )
                                                    )
                                                    Text(
                                                        text = "Mistakes recorded in recent diagnostic",
                                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.DarkGray)
                                                    )
                                                }
                                                Button(
                                                    onClick = { onOpenVideoLesson(weak.chapterId) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = AcademicPrimary),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text("Watch 3D Video", fontSize = 11.sp)
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Recommended Actions
                                Text(
                                    text = "AI Study Roadmap Recommendations:",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                summary.recommendedActions.forEach { act ->
                                    Text(
                                        text = "• $act",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = AcademicPrimary,
                                            lineHeight = 18.sp
                                        ),
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { quizState = QuizState.CONFIG },
                                        colors = ButtonDefaults.buttonColors(containerColor = AcademicPrimary),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.RestartAlt,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Re-test Adaptive Quiz")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
        Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AcademicPrimary))
    }
}

@Composable
private fun MasteryDiagnosticCard(masteries: List<TopicMastery>) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Diagnostic",
                        tint = AcademicPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Real-Time Topic Mastery Diagnostic",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            masteries.take(4).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.chapterName,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            maxLines = 1
                        )
                        LinearProgressIndicator(
                            progress = { item.accuracyPercentage / 100f },
                            color = Color(item.status.colorHex),
                            trackColor = Color(0xFFE2E8F0),
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }

                    Text(
                        text = "${item.accuracyPercentage.toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(item.status.colorHex)
                        )
                    )
                }
            }
        }
    }
}

private enum class QuizState {
    CONFIG,
    RUNNING,
    SUMMARY
}

private fun formatMinutesSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
