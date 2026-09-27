package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.AiExplanationService
import com.example.ai.ChatMessage
import com.example.ai.ExplanationLevel
import com.example.ai.ExplanationResult
import com.example.ui.theme.AcademicBackground
import com.example.ui.theme.AcademicElectricViolet
import com.example.ui.theme.AcademicPrimary
import com.example.ui.theme.CriticalAlertCrimson
import com.example.ui.theme.HighYieldGold
import com.example.ui.theme.MasteryEmerald
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FormulaExplainerModal(
    initialQuery: String,
    onDismiss: () -> Unit,
    explanationService: AiExplanationService,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var selectedText by remember { mutableStateOf(initialQuery) }
    var selectedLevel by remember { mutableStateOf(ExplanationLevel.INTERMEDIATE) }
    var isGenerating by remember { mutableStateOf(false) }
    var explanationResult by remember { mutableStateOf<ExplanationResult?>(null) }

    // Follow-up conversation
    val conversationHistory = remember { mutableStateListOf<ChatMessage>() }
    var followUpInput by remember { mutableStateOf("") }
    var isAnsweringFollowUp by remember { mutableStateOf(false) }

    val presetFormulas = listOf(
        "Gauss Law: ∮E·dA = q/ε₀",
        "Capacitor Energy: U = ½CV²",
        "Gibbs Energy: ΔG° = -RT ln K",
        "Snell's Law: n₁ sin i = n₂ sin r",
        "Newton 2nd Law: F = dp/dt = ma",
        "King's Integral: ∫ f(x)dx = ∫ f(a+b-x)dx"
    )

    // Trigger initial explanation
    fun requestExplanation(query: String, level: ExplanationLevel) {
        if (query.isBlank()) return
        isGenerating = true
        coroutineScope.launch {
            val result = explanationService.explainSelection(query, level)
            explanationResult = result
            isGenerating = false
        }
    }

    LaunchedEffect(initialQuery, selectedLevel) {
        if (initialQuery.isNotBlank()) {
            requestExplanation(initialQuery, selectedLevel)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(AcademicElectricViolet, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Cognitive Explainer",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AI Cognitive Concept Explainer",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Tailored for Classes 5th - 12th & NEET/JEE",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_explainer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray
                    )
                }
            }

            // Formula / Concept Input Field
            OutlinedTextField(
                value = selectedText,
                onValueChange = { selectedText = it },
                label = { Text("Selected Formula or Concept Text") },
                placeholder = { Text("e.g. ∮E·dA = q/ε₀ or Le Chatelier Principle") },
                trailingIcon = {
                    IconButton(
                        onClick = { requestExplanation(selectedText, selectedLevel) },
                        modifier = Modifier.testTag("submit_formula_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Explain",
                            tint = AcademicElectricViolet
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AcademicElectricViolet,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("formula_input_field")
            )

            // Preset Quick Selection Chips
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                presetFormulas.forEach { chipText ->
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = Color(0xFFF2F3FF),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDAE2FD)),
                        modifier = Modifier.clickable {
                            selectedText = chipText
                            requestExplanation(chipText, selectedLevel)
                        }
                    ) {
                        Text(
                            text = chipText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AcademicPrimary,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Level Selector Tabs (Beginner / Intermediate / Advanced)
            Text(
                text = "Tailored Understanding Level:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AcademicPrimary
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ExplanationLevel.entries.forEach { level ->
                    val isSelected = selectedLevel == level
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) AcademicPrimary else Color.Transparent)
                            .clickable {
                                selectedLevel = level
                                requestExplanation(selectedText, level)
                            }
                            .padding(vertical = 8.dp)
                            .testTag("level_tab_${level.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = level.displayName,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else AcademicPrimary
                                )
                            )
                            Text(
                                text = when (level) {
                                    ExplanationLevel.BEGINNER -> "Classes 5-8"
                                    ExplanationLevel.INTERMEDIATE -> "Classes 9-10"
                                    ExplanationLevel.ADVANCED -> "Classes 11-12"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    color = if (isSelected) HighYieldGold else Color.Gray
                                )
                            )
                        }
                    }
                }
            }

            // Content Area / Explanation & Follow-Up thread
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Loading indicator
                if (isGenerating) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = AcademicElectricViolet,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "AI is synthesizing ${selectedLevel.displayName} explanation with analogies...",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                    }
                } else if (explanationResult != null) {
                    val exp = explanationResult!!

                    // 1. Core Meaning & Variable Breakdown
                    item {
                        ExplanationCard(
                            title = "Core Intuition & Symbols",
                            icon = Icons.Default.School,
                            iconColor = AcademicElectricViolet,
                            content = exp.coreMeaning,
                            containerColor = Color(0xFFFAF8FF)
                        )
                    }

                    // 2. Real-World Analogy
                    item {
                        ExplanationCard(
                            title = "Real-World Analogy",
                            icon = Icons.Default.Lightbulb,
                            iconColor = HighYieldGold,
                            content = exp.analogy,
                            containerColor = Color(0xFFFEF3C7).copy(alpha = 0.5f)
                        )
                    }

                    // 3. Concrete Worked Example
                    item {
                        ExplanationCard(
                            title = "Concrete Step-by-Step Example",
                            icon = Icons.Default.AutoAwesome,
                            iconColor = MasteryEmerald,
                            content = exp.workedExample,
                            containerColor = Color(0xFFD1FAE5).copy(alpha = 0.4f)
                        )
                    }

                    // 4. Critical Exam Trap
                    item {
                        ExplanationCard(
                            title = "Critical Exam Trap Alert",
                            icon = Icons.Default.WarningAmber,
                            iconColor = CriticalAlertCrimson,
                            content = exp.examTrap,
                            containerColor = Color(0xFFFEE2E2).copy(alpha = 0.6f)
                        )
                    }

                    // Conversation history
                    if (conversationHistory.isNotEmpty()) {
                        item {
                            Text(
                                text = "Follow-up Discussion:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        items(conversationHistory) { msg ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                            ) {
                                Surface(
                                    color = if (msg.isUser) AcademicPrimary else Color(0xFFF2F3FF),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth(0.85f)
                                ) {
                                    Text(
                                        text = msg.text,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = if (msg.isUser) Color.White else AcademicPrimary,
                                            lineHeight = 18.sp
                                        ),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (isAnsweringFollowUp) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    color = AcademicElectricViolet,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI tutor is answering...",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                )
                            }
                        }
                    }

                    // Follow-up suggestion prompts
                    item {
                        Text(
                            text = "Suggested Follow-ups:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        val suggestions = listOf(
                            "Give me an exam trick",
                            "What happens at the boundary limit?",
                            "Explain with another analogy",
                            "Test me with a quick question"
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            suggestions.forEach { prompt ->
                                Surface(
                                    shape = RoundedCornerShape(percent = 50),
                                    color = Color(0xFFEEF2FF),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7D2FE)),
                                    modifier = Modifier.clickable {
                                        followUpInput = prompt
                                    }
                                ) {
                                    Text(
                                        text = prompt,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AcademicElectricViolet,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Follow-up Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = followUpInput,
                    onValueChange = { followUpInput = it },
                    placeholder = { Text("Ask follow-up doubt...") },
                    shape = RoundedCornerShape(percent = 50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AcademicElectricViolet,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("follow_up_input_field")
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            if (followUpInput.isNotBlank()) AcademicElectricViolet else Color.LightGray,
                            CircleShape
                        )
                        .clickable(enabled = followUpInput.isNotBlank() && !isAnsweringFollowUp) {
                            val question = followUpInput.trim()
                            followUpInput = ""
                            conversationHistory.add(ChatMessage(isUser = true, text = question))
                            isAnsweringFollowUp = true

                            coroutineScope.launch {
                                val answer = explanationService.answerFollowUp(
                                    originalSelection = selectedText,
                                    initialExplanation = explanationResult?.fullMarkdown ?: "",
                                    history = conversationHistory,
                                    newQuestion = question,
                                    level = selectedLevel
                                )
                                conversationHistory.add(ChatMessage(isUser = false, text = answer))
                                isAnsweringFollowUp = false
                            }
                        }
                        .testTag("send_follow_up_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExplanationCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    content: String,
    containerColor: Color
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AcademicPrimary
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    color = AcademicPrimary
                )
            )
        }
    }
}
