package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CrisisAlert
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ExamType
import com.example.ui.components.FormulaExplainerModal
import com.example.ui.screens.AdaptiveQuizScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.FormulaVaultScreen
import com.example.ui.screens.TextbookReaderScreen
import com.example.ui.screens.VisualDashboardScreen
import com.example.ui.theme.AcademicBackground
import com.example.ui.theme.AcademicElectricViolet
import com.example.ui.theme.AcademicPrimary
import com.example.ui.theme.HighYieldGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AcademicViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: AcademicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: AcademicViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val selectedExam by viewModel.selectedExam.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()

    val isExplainerOpen by viewModel.isExplainerOpen.collectAsStateWithLifecycle()
    val explainerQuery by viewModel.explainerQuery.collectAsStateWithLifecycle()

    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val highlights by viewModel.highlights.collectAsStateWithLifecycle()
    val attempts by viewModel.attempts.collectAsStateWithLifecycle()
    val flashcardProgress by viewModel.flashcardProgress.collectAsStateWithLifecycle()
    val adaptiveQuizHistory by viewModel.adaptiveQuizHistory.collectAsStateWithLifecycle()

    var showExamDropdown by remember { mutableStateOf(false) }
    var veoPromptDialogText by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AcademicBackground,
        topBar = {
            TopAcademicHeader(
                selectedGrade = selectedGrade,
                selectedExam = selectedExam,
                onExamClick = { showExamDropdown = true },
                onOpenExplainer = { viewModel.openExplainer("") }
            )
            // Exam Dropdown
            DropdownMenu(
                expanded = showExamDropdown,
                onDismissRequest = { showExamDropdown = false }
            ) {
                ExamType.entries.forEach { exam ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = exam.displayName,
                                fontWeight = if (selectedExam == exam) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            viewModel.setSelectedExam(exam)
                            showExamDropdown = false
                        }
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { viewModel.setCurrentTab(0) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 0) Icons.AutoMirrored.Filled.MenuBook else Icons.AutoMirrored.Outlined.MenuBook,
                            contentDescription = "Reader"
                        )
                    },
                    label = { Text("Textbook", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AcademicPrimary,
                        indicatorColor = AcademicPrimary
                    ),
                    modifier = Modifier.testTag("nav_reader")
                )

                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { viewModel.setCurrentTab(1) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 1) Icons.Filled.CrisisAlert else Icons.Outlined.CrisisAlert,
                            contentDescription = "Adaptive Quiz"
                        )
                    },
                    label = { Text("Adaptive", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AcademicPrimary,
                        indicatorColor = AcademicPrimary
                    ),
                    modifier = Modifier.testTag("nav_quiz")
                )

                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { viewModel.setCurrentTab(2) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 2) Icons.Filled.Calculate else Icons.Outlined.Calculate,
                            contentDescription = "Formulas"
                        )
                    },
                    label = { Text("Formulas", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AcademicPrimary,
                        indicatorColor = AcademicPrimary
                    ),
                    modifier = Modifier.testTag("nav_formulas")
                )

                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { viewModel.setCurrentTab(3) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 3) Icons.Filled.Psychology else Icons.Outlined.Psychology,
                            contentDescription = "Flashcards"
                        )
                    },
                    label = { Text("Cards", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AcademicPrimary,
                        indicatorColor = AcademicPrimary
                    ),
                    modifier = Modifier.testTag("nav_flashcards")
                )

                NavigationBarItem(
                    selected = currentTab == 4,
                    onClick = { viewModel.setCurrentTab(4) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 4) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Outlined.TrendingUp,
                            contentDescription = "Analytics"
                        )
                    },
                    label = { Text("Analytics", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AcademicPrimary,
                        indicatorColor = AcademicPrimary
                    ),
                    modifier = Modifier.testTag("nav_analytics")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> TextbookReaderScreen(
                    chapters = viewModel.getFilteredChapters(),
                    selectedGrade = selectedGrade,
                    onGradeSelected = { viewModel.setSelectedGrade(it) },
                    selectedSubject = selectedSubject,
                    onSubjectSelected = { viewModel.setSelectedSubject(it) },
                    selectedExam = selectedExam,
                    onExamSelected = { viewModel.setSelectedExam(it) },
                    bookmarks = bookmarks,
                    highlights = highlights,
                    onToggleBookmark = { ref, type, title, sub, s ->
                        viewModel.toggleBookmark(ref, type, title, sub, s)
                    },
                    onAddHighlight = { cId, sId, text, col ->
                        viewModel.addHighlight(cId, sId, text, col)
                    },
                    onExplainContent = { query ->
                        viewModel.openExplainer(query)
                    },
                    onOpenPromptStudio = { prompt ->
                        veoPromptDialogText = prompt
                    }
                )

                1 -> AdaptiveQuizScreen(
                    userAttempts = attempts,
                    selectedGrade = selectedGrade,
                    onRecordAttempt = { qId, opt, isCor, ex, sub ->
                        viewModel.recordQuestionAttempt(qId, opt, isCor, ex, sub)
                    },
                    onSaveQuizSummary = { summary ->
                        viewModel.saveAdaptiveQuizResult(summary)
                    },
                    onOpenVideoLesson = { chapterId ->
                        viewModel.setCurrentTab(0)
                    },
                    onOpenFormulaExplainer = { query ->
                        viewModel.openExplainer(query)
                    }
                )

                2 -> FormulaVaultScreen(
                    formulas = viewModel.getFilteredFormulas(),
                    selectedGrade = selectedGrade,
                    onGradeSelected = { viewModel.setSelectedGrade(it) },
                    onExplainFormula = { query ->
                        viewModel.openExplainer(query)
                    }
                )

                3 -> FlashcardsScreen(
                    flashcards = viewModel.getFilteredFlashcards(),
                    progressList = flashcardProgress,
                    onCardReviewed = { cardId, wasCor ->
                        viewModel.reviewFlashcard(cardId, wasCor)
                    },
                    onExplainCard = { query ->
                        viewModel.openExplainer(query)
                    }
                )

                4 -> VisualDashboardScreen(
                    userAttempts = attempts,
                    quizResults = adaptiveQuizHistory,
                    selectedGrade = selectedGrade,
                    onGradeSelected = { viewModel.setSelectedGrade(it) },
                    onStartSubjectQuiz = { subject ->
                        viewModel.setSelectedSubject(subject)
                        viewModel.setCurrentTab(1)
                    },
                    onOpenFormulaExplainer = { query ->
                        viewModel.openExplainer(query)
                    }
                )
            }
        }

        // Formula / Concept AI Explainer Modal
        if (isExplainerOpen) {
            FormulaExplainerModal(
                initialQuery = explainerQuery,
                onDismiss = { viewModel.closeExplainer() },
                explanationService = viewModel.explanationService
            )
        }

        // Veo AI Simulation Prompt Studio Dialog
        veoPromptDialogText?.let { prompt ->
            AlertDialog(
                onDismissRequest = { veoPromptDialogText = null },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AcademicElectricViolet,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Veo AI 3D Simulation Studio",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "Active AI Video Generation Prompt:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.Gray)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = prompt,
                                style = MaterialTheme.typography.bodySmall.copy(color = AcademicPrimary, lineHeight = 18.sp),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Veo 3.1 synthesizes 60 FPS physics and molecular dynamics to accelerate intuition for Class $selectedGrade students.",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.DarkGray)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { veoPromptDialogText = null },
                        colors = ButtonDefaults.buttonColors(containerColor = AcademicPrimary),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Simulate Animation")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { veoPromptDialogText = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Close", color = Color.Black)
                    }
                }
            )
        }
    }
}

@Composable
private fun TopAcademicHeader(
    selectedGrade: Int,
    selectedExam: ExamType,
    onExamClick: () -> Unit,
    onOpenExplainer: () -> Unit
) {
    Surface(
        color = AcademicPrimary,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Branding & Grade Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Logo",
                        tint = HighYieldGold,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Cognitive Academic",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Class $selectedGrade • 5th to 12th Foundations",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFC4C1FB),
                            fontSize = 10.sp
                        )
                    )
                }
            }

            // Right Actions: Exam Switcher Pill + AI Concept Explainer Trigger
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Exam Filter Tag
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = Color.White.copy(alpha = 0.18f),
                    modifier = Modifier.clickable { onExamClick() }
                ) {
                    Text(
                        text = selectedExam.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // AI Explainer Floating Pill
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = AcademicElectricViolet,
                    modifier = Modifier.clickable { onOpenExplainer() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Explainer",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI Explainer",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}
