package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.FlashcardProgressEntity
import com.example.data.model.FlashcardItem
import com.example.ui.theme.AcademicBackground
import com.example.ui.theme.AcademicElectricViolet
import com.example.ui.theme.AcademicPrimary
import com.example.ui.theme.CriticalAlertCrimson
import com.example.ui.theme.HighYieldGold
import com.example.ui.theme.MasteryEmerald

@Composable
fun FlashcardsScreen(
    flashcards: List<FlashcardItem>,
    progressList: List<FlashcardProgressEntity>,
    onCardReviewed: (String, Boolean) -> Unit,
    onExplainCard: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val activeCard = flashcards.getOrNull(currentIndex)

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "card_flip"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AcademicBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Spaced Repetition Flashcards",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Leitner Spacing System • Daily Active Recall",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                )
            }

            Surface(
                shape = RoundedCornerShape(percent = 50),
                color = AcademicElectricViolet.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "${currentIndex + 1} / ${flashcards.size}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = AcademicElectricViolet,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (activeCard != null) {
            // Flipped Card Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .clickable { isFlipped = !isFlipped }
                    .testTag("flashcard_container")
            ) {
                if (rotation <= 90f) {
                    // Front of card (Question)
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(percent = 50),
                                    color = Color(0xFFF2F3FF)
                                ) {
                                    Text(
                                        text = "${activeCard.subject.displayName} • ${activeCard.chapterName}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AcademicElectricViolet,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.Flip,
                                    contentDescription = "Tap to Flip",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Text(
                                text = activeCard.front,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 28.sp,
                                    color = AcademicPrimary
                                )
                            )

                            Text(
                                text = "Tap anywhere on card to flip",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                            )
                        }
                    }
                } else {
                    // Back of card (Answer & Derivation)
                    Surface(
                        color = Color(0xFFFAF8FF),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, AcademicElectricViolet),
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { rotationY = 180f }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Verified Answer",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MasteryEmerald,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Surface(
                                    shape = RoundedCornerShape(percent = 50),
                                    color = AcademicElectricViolet,
                                    modifier = Modifier.clickable {
                                        onExplainCard("${activeCard.front}\n${activeCard.back}")
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = "Explain",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "Deep AI Explain",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }
                            }

                            Text(
                                text = activeCard.back,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    lineHeight = 26.sp,
                                    color = AcademicPrimary
                                )
                            )

                            activeCard.formulaSnippet?.let { snippet ->
                                Surface(
                                    color = Color(0xFFF1F5F9),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = snippet,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = AcademicPrimary
                                        ),
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Leitner Grading Buttons (Got it wrong vs Nailed it!)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        onCardReviewed(activeCard.id, false)
                        isFlipped = false
                        if (currentIndex < flashcards.size - 1) currentIndex++ else currentIndex = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CriticalAlertCrimson),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("card_wrong_button")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Needs Review")
                }

                Button(
                    onClick = {
                        onCardReviewed(activeCard.id, true)
                        isFlipped = false
                        if (currentIndex < flashcards.size - 1) currentIndex++ else currentIndex = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MasteryEmerald),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("card_correct_button")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mastered!")
                }
            }
        }
    }
}
