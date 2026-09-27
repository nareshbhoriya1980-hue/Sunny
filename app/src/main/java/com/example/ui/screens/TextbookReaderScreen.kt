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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.BookmarkEntity
import com.example.data.local.entities.HighlightEntity
import com.example.data.model.Chapter
import com.example.data.model.ExamType
import com.example.data.model.Subject
import com.example.data.model.TextbookSection
import com.example.ui.components.VideoLessonPlayer
import com.example.ui.theme.AcademicBackground
import com.example.ui.theme.AcademicElectricViolet
import com.example.ui.theme.AcademicPrimary
import com.example.ui.theme.CriticalAlertCrimson
import com.example.ui.theme.HighYieldGold
import com.example.ui.theme.MasteryEmerald

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TextbookReaderScreen(
    chapters: List<Chapter>,
    selectedGrade: Int,
    onGradeSelected: (Int) -> Unit,
    selectedSubject: Subject?,
    onSubjectSelected: (Subject?) -> Unit,
    selectedExam: ExamType,
    onExamSelected: (ExamType) -> Unit,
    bookmarks: List<BookmarkEntity>,
    highlights: List<HighlightEntity>,
    onToggleBookmark: (String, String, String, String, String) -> Unit,
    onAddHighlight: (String, String, String, Long) -> Unit,
    onExplainContent: (String) -> Unit,
    onOpenPromptStudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedChapterIndex by remember { mutableIntStateOf(0) }
    var isVideoPlayerExpanded by remember { mutableStateOf(false) }

    val activeChapter = chapters.getOrNull(selectedChapterIndex) ?: chapters.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AcademicBackground)
    ) {
        // Class 5th to 12th Grade Switcher Bar
        GradeSelectorBar(
            selectedGrade = selectedGrade,
            onGradeSelected = {
                onGradeSelected(it)
                selectedChapterIndex = 0
            }
        )

        // Subject Filter Bar
        SubjectFilterBar(
            selectedSubject = selectedSubject,
            onSubjectSelected = {
                onSubjectSelected(it)
                selectedChapterIndex = 0
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Chapter Horizontal Carousel
            item {
                Text(
                    text = "Select Chapter (Class $selectedGrade)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AcademicPrimary
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chapters.forEachIndexed { idx, ch ->
                        val isSelected = selectedChapterIndex == idx
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) AcademicPrimary else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) AcademicPrimary else Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier
                                .clickable {
                                    selectedChapterIndex = idx
                                    isVideoPlayerExpanded = false
                                }
                                .testTag("chapter_tab_$idx")
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text(
                                    text = ch.name,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else AcademicPrimary
                                    ),
                                    maxLines = 1
                                )
                                Text(
                                    text = "${ch.subject.shortName} • ${ch.weightage}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        color = if (isSelected) HighYieldGold else Color.Gray
                                    )
                                )
                            }
                        }
                    }
                }
            }

            if (activeChapter != null) {
                // 3D Animated Video Lesson Card / Player
                item {
                    val video = activeChapter.videoLesson
                    if (video != null) {
                        Surface(
                            color = Color(0xFF1E1B4B),
                            shape = RoundedCornerShape(8.dp),
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
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = "Animated Lesson",
                                            tint = HighYieldGold,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = video.title,
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                            Text(
                                                text = "3D Animated Physics/Chemistry/Bio Simulation",
                                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFC4C1FB))
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(percent = 50),
                                        color = AcademicElectricViolet,
                                        modifier = Modifier.clickable {
                                            isVideoPlayerExpanded = !isVideoPlayerExpanded
                                        }
                                    ) {
                                        Text(
                                            text = if (isVideoPlayerExpanded) "Minimize Video" else "Watch in 3D",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            ),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }

                                AnimatedVisibility(visible = isVideoPlayerExpanded) {
                                    Column(modifier = Modifier.padding(top = 12.dp)) {
                                        VideoLessonPlayer(
                                            videoLesson = video,
                                            onPromptVeoStudio = onOpenPromptStudio
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Chapter Summary & NCERT Weightage
                item {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Class $selectedGrade Foundation • ${activeChapter.subject.displayName}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = AcademicElectricViolet,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "${activeChapter.pyqCount} Solved Questions",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MasteryEmerald,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = activeChapter.summary,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.DarkGray,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }

                // Textbook Sections with PYQ Sticky Margin Markers & Traps
                items(activeChapter.sections) { section ->
                    TextbookSectionCard(
                        section = section,
                        isBookmarked = bookmarks.any { it.referenceId == section.id },
                        onToggleBookmark = {
                            onToggleBookmark(
                                section.id,
                                "SECTION",
                                section.title,
                                activeChapter.name,
                                activeChapter.subject.name
                            )
                        },
                        onExplainText = { onExplainContent(it) },
                        onHighlightText = { snippet ->
                            onAddHighlight(activeChapter.id, section.id, snippet, 0xFFF59E0B)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
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
            text = "Class:",
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

@Composable
private fun SubjectFilterBar(
    selectedSubject: Subject?,
    onSubjectSelected: (Subject?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAF8FF))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // "All Subjects" chip
        Surface(
            shape = RoundedCornerShape(percent = 50),
            color = if (selectedSubject == null) AcademicElectricViolet else Color(0xFFF1F5F9),
            modifier = Modifier.clickable { onSubjectSelected(null) }
        ) {
            Text(
                text = "All Subjects",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (selectedSubject == null) Color.White else AcademicPrimary
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }

        Subject.entries.forEach { subj ->
            val isSelected = selectedSubject == subj
            Surface(
                shape = RoundedCornerShape(percent = 50),
                color = if (isSelected) AcademicElectricViolet else Color(0xFFF1F5F9),
                modifier = Modifier.clickable { onSubjectSelected(subj) }
            ) {
                Text(
                    text = subj.displayName,
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

@Composable
private fun TextbookSectionCard(
    section: TextbookSection,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onExplainText: (String) -> Unit,
    onHighlightText: (String) -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Section Header & Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AcademicPrimary
                        )
                    )
                    Text(
                        text = section.subheader,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                }

                IconButton(onClick = onToggleBookmark) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) AcademicElectricViolet else Color.Gray
                    )
                }
            }

            // Core Concept & PYQ Marker Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                section.pyqMarker?.let { pyq ->
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = CriticalAlertCrimson
                    ) {
                        Text(
                            text = pyq.examText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                section.coreConceptPill?.let { pill ->
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = Color(0xFFFEF3C7),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Text(
                            text = pill,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFB45309),
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Body Content (Engineered 1.75x line height for formulas)
            Text(
                text = section.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 28.sp,
                    color = AcademicPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
            )

            // Trap Alert Card if present
            section.trapAlert?.let { trap ->
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = "Trap Alert",
                                tint = CriticalAlertCrimson,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "⚠️ Common Exam Trap: ${trap.title}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB91C1C)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = trap.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF7F1D1D),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Tool Bar on Section ("Explain with AI", "Highlight")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // AI Explain Button
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = AcademicElectricViolet,
                    modifier = Modifier.clickable {
                        onExplainText(section.title + ": " + section.content.take(160))
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Explain",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Explain Section with AI",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                // Highlight Action
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    modifier = Modifier.clickable {
                        onHighlightText(section.title)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.BorderColor,
                            contentDescription = "Highlight",
                            tint = HighYieldGold,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Save Highlight",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = AcademicPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}
