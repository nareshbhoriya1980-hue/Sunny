package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.analytics.DashboardAnalyticsEngine
import com.example.data.analytics.DashboardState
import com.example.data.analytics.SubjectMasteryData
import com.example.data.local.entities.AdaptiveQuizResultEntity
import com.example.data.local.entities.QuestionAttemptEntity
import com.example.data.model.Subject
import com.example.ui.components.RechartsAreaTrendChart
import com.example.ui.components.RechartsBarChart
import com.example.ui.components.RechartsRadarChart
import com.example.ui.theme.AcademicBackground
import com.example.ui.theme.AcademicElectricViolet
import com.example.ui.theme.AcademicPrimary
import com.example.ui.theme.HighYieldGold
import com.example.ui.theme.MasteryEmerald

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualDashboardScreen(
    userAttempts: List<QuestionAttemptEntity>,
    quizResults: List<AdaptiveQuizResultEntity>,
    selectedGrade: Int,
    onGradeSelected: (Int) -> Unit,
    onStartSubjectQuiz: (Subject) -> Unit,
    onOpenFormulaExplainer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dashboardState = remember(selectedGrade, userAttempts, quizResults) {
        DashboardAnalyticsEngine.computeDashboard(selectedGrade, userAttempts, quizResults)
    }

    var selectedSubjectDetail by remember { mutableStateOf<SubjectMasteryData?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AcademicBackground)
    ) {
        // Grade Level Selector Switcher Bar
        GradeSelectorBar(
            selectedGrade = selectedGrade,
            onGradeSelected = onGradeSelected
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Executive Summary & Improvement Trend Banner
            item {
                ExecutiveSummaryCard(dashboard = dashboardState)
            }

            // 2. Recharts Multi-Subject Mastery Bar Chart Card
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
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Subject Mastery Levels",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AcademicPrimary
                                    )
                                )
                                Text(
                                    text = "Class ${dashboardState.classGrade}th • Current vs Benchmark Target",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(percent = 50),
                                color = AcademicElectricViolet.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "Recharts Engine",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = AcademicElectricViolet,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        RechartsBarChart(
                            subjects = dashboardState.subjectsMastery,
                            onSubjectSelected = { selectedSubjectDetail = it }
                        )
                    }
                }
            }

            // 3. Recharts Improvement Trend Area Chart Card
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
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = "Improvement Trend",
                                    tint = AcademicElectricViolet,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Improvement Velocity Trend",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = AcademicPrimary
                                        )
                                    )
                                    Text(
                                        text = "Trajectory across diagnostic test evaluations",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(percent = 50),
                                color = MasteryEmerald
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "+${dashboardState.overallVelocityDelta.toInt()}% Trend",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        RechartsAreaTrendChart(trendPoints = dashboardState.improvementTrend)
                    }
                }
            }

            // 4. Grade-Level Benchmark & Focus Guidance Card
            item {
                GradeBenchmarkCard(benchmark = dashboardState.gradeBenchmark)
            }

            // 5. Radar Competencies Matrix Card
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
                                imageVector = Icons.Default.Radar,
                                contentDescription = "Competency Radar",
                                tint = AcademicElectricViolet,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Cognitive Competencies Radar",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AcademicPrimary
                                    )
                                )
                                Text(
                                    text = "Multi-axial readiness balance for Class ${dashboardState.classGrade}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        RechartsRadarChart(competencies = dashboardState.radarCompetencies)

                        Spacer(modifier = Modifier.height(10.dp))

                        // Competency Chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            dashboardState.radarCompetencies.forEach { comp ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFF8FAFC),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = comp.axisName,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.DarkGray,
                                                fontSize = 11.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${comp.scorePercent.toInt()}%",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = AcademicPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 6. Subject Diagnostic Drilldowns with Adaptive Quiz Shortcut
            item {
                Text(
                    text = "Subject Deep-Dive & Adaptive Action",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AcademicPrimary
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(dashboardState.subjectsMastery.size) { idx ->
                val subjData = dashboardState.subjectsMastery[idx]
                SubjectDetailCard(
                    data = subjData,
                    onStartQuiz = { onStartSubjectQuiz(subjData.subject) },
                    onExplainConcepts = { onOpenFormulaExplainer("${subjData.subject.displayName} core high-yield formulas for Class $selectedGrade") }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ExecutiveSummaryCard(dashboard: DashboardState) {
    Surface(
        color = AcademicPrimary,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = HighYieldGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Overall Grade Mastery",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Class ${dashboard.classGrade}th Competitive Benchmark",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFC4C1FB))
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = MasteryEmerald
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "▲ +${dashboard.overallVelocityDelta.toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "${dashboard.overallAccuracy.toInt()}%",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Composite Accuracy Index",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFDAE2FD))
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${dashboard.totalQuestionsSolved} Questions",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = HighYieldGold
                        )
                    )
                    Text(
                        text = "Evaluated via Adaptive Engine",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFDAE2FD))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = dashboard.gradeBenchmark.trajectoryStatus,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun GradeBenchmarkCard(benchmark: com.example.data.analytics.GradeBenchmarkData) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = AcademicElectricViolet,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "${benchmark.gradeLabel} Performance Alignment",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "How you stack against national competitive benchmarks",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Comparison Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BenchmarkBarColumn(
                    label = "Cohort Average",
                    percentage = benchmark.cohortAverage,
                    barColor = Color(0xFF94A3B8)
                )
                BenchmarkBarColumn(
                    label = "Your Mastery",
                    percentage = benchmark.studentCurrentMastery,
                    barColor = AcademicElectricViolet
                )
                BenchmarkBarColumn(
                    label = "Top 5% Target",
                    percentage = benchmark.topTierTarget,
                    barColor = HighYieldGold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Actionable advice card
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = HighYieldGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = benchmark.keyImprovementFocus,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AcademicPrimary,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BenchmarkBarColumn(
    label: String,
    percentage: Float,
    barColor: Color
) {
    Column(
        modifier = Modifier.width(96.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${percentage.toInt()}%",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = barColor
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (percentage / 100f).coerceIn(0f, 1f) },
            color = barColor,
            trackColor = Color(0xFFF1F5F9),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Gray,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
private fun SubjectDetailCard(
    data: SubjectMasteryData,
    onStartQuiz: () -> Unit,
    onExplainConcepts: () -> Unit
) {
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
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(data.subject.accentColorHex), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = data.subject.displayName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AcademicPrimary
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = Color(0xFFECFDF5)
                ) {
                    Text(
                        text = "+${data.trendDeltaPercent.toInt()}% trend",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MasteryEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { (data.currentAccuracy / 100f).coerceIn(0f, 1f) },
                color = Color(data.subject.accentColorHex),
                trackColor = Color(0xFFF1F5F9),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Mastery: ${data.currentAccuracy.toInt()}% (Target: ${data.targetBenchmark.toInt()}%)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AcademicPrimary
                    )
                )
                Text(
                    text = "${data.masteredTopicsCount}/${data.totalTopicsCount} topics mastered",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartQuiz,
                    colors = ButtonDefaults.buttonColors(containerColor = AcademicPrimary),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("quiz_subject_${data.subject.name.lowercase()}")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Adaptive Quiz",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF1F5F9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    modifier = Modifier
                        .height(36.dp)
                        .clickable { onExplainConcepts() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AcademicElectricViolet,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI Review",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = AcademicPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GradeSelectorBar(
    selectedGrade: Int,
    onGradeSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Grade Level:",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = AcademicPrimary
            )
        )

        (5..12).forEach { grade ->
            val isSelected = selectedGrade == grade
            Surface(
                shape = RoundedCornerShape(percent = 50),
                color = if (isSelected) AcademicPrimary else Color(0xFFF2F3FF),
                modifier = Modifier.clickable { onGradeSelected(grade) }
            ) {
                Text(
                    text = "${grade}th",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else AcademicPrimary
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}
