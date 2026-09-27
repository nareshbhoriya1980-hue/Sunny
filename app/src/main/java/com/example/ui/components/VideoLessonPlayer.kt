package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnimationSceneType
import com.example.data.model.ChapterVideoLesson
import com.example.data.model.KeyframeMilestone
import com.example.data.model.VideoScene
import com.example.ui.theme.AcademicElectricViolet
import com.example.ui.theme.AcademicPrimary
import com.example.ui.theme.CriticalAlertCrimson
import com.example.ui.theme.HighYieldGold
import com.example.ui.theme.MasteryEmerald
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoLessonPlayer(
    videoLesson: ChapterVideoLesson,
    onVideoCompleted: () -> Unit = {},
    onPromptVeoStudio: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var currentSeconds by remember { mutableFloatStateOf(0f) }
    var playbackSpeed by remember { mutableFloatStateOf(1f) }
    var showCaptions by remember { mutableStateOf(true) }
    var selectedSceneIndex by remember { mutableIntStateOf(0) }

    val totalDuration = videoLesson.totalDurationSeconds.toFloat()

    // Real-time playback timer loop
    LaunchedEffect(isPlaying, playbackSpeed, videoLesson.id) {
        while (isPlaying) {
            delay(100)
            if (isPlaying) {
                currentSeconds += 0.1f * playbackSpeed
                if (currentSeconds >= totalDuration) {
                    currentSeconds = totalDuration
                    isPlaying = false
                    onVideoCompleted()
                }

                // Determine active scene based on current time
                var accumulated = 0
                for ((idx, scene) in videoLesson.scenes.withIndex()) {
                    accumulated += scene.durationSeconds
                    if (currentSeconds <= accumulated) {
                        selectedSceneIndex = idx
                        break
                    }
                }
            }
        }
    }

    val activeScene = videoLesson.scenes.getOrNull(selectedSceneIndex) ?: videoLesson.scenes.first()

    CardContainer(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(AcademicPrimary, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "AI Animated Video",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = videoLesson.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "AI 3D Concept Animation • 60 FPS",
                            style = MaterialTheme.typography.labelSmall.copy(color = AcademicElectricViolet)
                        )
                    }
                }

                // AI Prompt Badge
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = AcademicElectricViolet.copy(alpha = 0.12f),
                    modifier = Modifier.clickable { onPromptVeoStudio(videoLesson.aiPromptUsed) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Veo AI Prompt",
                            tint = AcademicElectricViolet,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Veo AI Sim",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AcademicElectricViolet
                            )
                        )
                    }
                }
            }

            // 60 FPS Interactive Canvas Video Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(Color(0xFF090722))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                // Animated scientific simulation canvas
                InteractiveScientificCanvas(
                    animationType = activeScene.animationType,
                    isPlaying = isPlaying,
                    progress = if (totalDuration > 0) currentSeconds / totalDuration else 0f
                )

                // Live Captions & Narration Overlay
                if (showCaptions) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.78f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp, start = 12.dp, end = 12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = activeScene.narration,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White,
                                lineHeight = 16.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Scene Title Watermark
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Scene ${selectedSceneIndex + 1}/${videoLesson.scenes.size}: ${activeScene.title}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            // Timeline Scrubber & Progress
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Slider(
                    value = currentSeconds,
                    onValueChange = { currentSeconds = it },
                    valueRange = 0f..totalDuration,
                    colors = SliderDefaults.colors(
                        thumbColor = AcademicElectricViolet,
                        activeTrackColor = AcademicElectricViolet,
                        inactiveTrackColor = Color(0xFFDAE2FD)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .testTag("video_timeline_scrubber")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatTime(currentSeconds.toInt()),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = formatTime(videoLesson.totalDurationSeconds),
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                }
            }

            // Playback Controls Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Speed Selector
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = Color(0xFFF2F3FF),
                    modifier = Modifier.clickable {
                        playbackSpeed = when (playbackSpeed) {
                            1f -> 1.25f
                            1.25f -> 1.5f
                            1.5f -> 2f
                            else -> 1f
                        }
                    }
                ) {
                    Text(
                        text = "${playbackSpeed}x",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = AcademicPrimary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                // Center: Rewind, Play/Pause, Fast Forward
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { currentSeconds = (currentSeconds - 5f).coerceAtLeast(0f) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastRewind,
                            contentDescription = "Rewind 5s",
                            tint = AcademicPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(2.dp, CircleShape)
                            .background(AcademicPrimary, CircleShape)
                            .clickable {
                                if (currentSeconds >= totalDuration) {
                                    currentSeconds = 0f
                                    isPlaying = true
                                } else {
                                    isPlaying = !isPlaying
                                }
                            }
                            .testTag("video_play_pause_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    IconButton(
                        onClick = { currentSeconds = (currentSeconds + 5f).coerceAtMost(totalDuration) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Forward 5s",
                            tint = AcademicPrimary
                        )
                    }
                }

                // Right: Captions toggle
                IconButton(
                    onClick = { showCaptions = !showCaptions }
                ) {
                    Icon(
                        imageVector = Icons.Default.Subtitles,
                        contentDescription = "Toggle Captions",
                        tint = if (showCaptions) AcademicElectricViolet else Color.Gray
                    )
                }
            }

            // Keyframe Chapters & Milestones Row
            Text(
                text = "Key Milestones & Exam Caveats",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AcademicPrimary
                ),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                videoLesson.keyframes.forEach { milestone ->
                    val isPastOrCurrent = currentSeconds >= milestone.timestampSeconds
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = if (isPastOrCurrent) AcademicPrimary else Color(0xFFF2F3FF),
                        modifier = Modifier.clickable {
                            currentSeconds = milestone.timestampSeconds.toFloat()
                            isPlaying = true
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatTime(milestone.timestampSeconds),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPastOrCurrent) HighYieldGold else AcademicElectricViolet
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = milestone.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isPastOrCurrent) Color.White else AcademicPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun CardContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
            .shadow(2.dp, RoundedCornerShape(8.dp)),
        color = Color(0xFFFFFFFF),
        shape = RoundedCornerShape(8.dp)
    ) {
        content()
    }
}

// 60 FPS Custom Canvas Simulation Renderer
@Composable
private fun InteractiveScientificCanvas(
    animationType: AnimationSceneType,
    isPlaying: Boolean,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "anim_canvas")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        when (animationType) {
            AnimationSceneType.ELECTROSTATIC_FLUX -> {
                // Draw 3D Infinite Charged Sheet & Gaussian Cylinder
                val centerY = h / 2
                // Plane
                drawRect(
                    brush = Brush.horizontalGradient(listOf(Color(0xFF1E1B4B), Color(0xFF4648D4))),
                    topLeft = Offset(w * 0.1f, centerY - 6f),
                    size = Size(w * 0.8f, 12f)
                )

                // Surface charge markers (+)
                for (i in 0..10) {
                    val x = w * 0.15f + i * (w * 0.7f / 10)
                    drawCircle(
                        color = Color(0xFFF59E0B),
                        radius = 4f,
                        center = Offset(x, centerY)
                    )
                }

                // Electric field vectors (arrows up & down)
                val arrowOffset = (sin(Math.toRadians(phase.toDouble())).toFloat() * 6f)
                for (i in 0..8) {
                    val x = w * 0.18f + i * (w * 0.64f / 8)
                    // Upward
                    drawLine(
                        color = Color(0xFF6366F1),
                        start = Offset(x, centerY - 10f),
                        end = Offset(x, centerY - 50f - arrowOffset),
                        strokeWidth = 3f
                    )
                    // Downward
                    drawLine(
                        color = Color(0xFF6366F1),
                        start = Offset(x, centerY + 10f),
                        end = Offset(x, centerY + 50f + arrowOffset),
                        strokeWidth = 3f
                    )
                }

                // Gaussian Cylinder intersecting plane
                val cylX = w * 0.5f
                drawOval(
                    color = Color(0x660EA5E9),
                    topLeft = Offset(cylX - 45f, centerY - 70f),
                    size = Size(90f, 30f)
                )
                drawOval(
                    color = Color(0x660EA5E9),
                    topLeft = Offset(cylX - 45f, centerY + 40f),
                    size = Size(90f, 30f)
                )
                drawLine(
                    color = Color(0xFF0EA5E9),
                    start = Offset(cylX - 45f, centerY - 55f),
                    end = Offset(cylX - 45f, centerY + 55f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = Color(0xFF0EA5E9),
                    start = Offset(cylX + 45f, centerY - 55f),
                    end = Offset(cylX + 45f, centerY + 55f),
                    strokeWidth = 2f
                )
            }

            AnimationSceneType.DIELECTRIC_POLARIZATION -> {
                // Parallel Capacitor Plates
                drawLine(
                    color = Color(0xFFEF4444), // Positive Plate
                    start = Offset(w * 0.15f, h * 0.2f),
                    end = Offset(w * 0.85f, h * 0.2f),
                    strokeWidth = 10f
                )
                drawLine(
                    color = Color(0xFF4648D4), // Negative Plate
                    start = Offset(w * 0.15f, h * 0.8f),
                    end = Offset(w * 0.85f, h * 0.8f),
                    strokeWidth = 10f
                )

                // Dielectric Slab in center
                drawRect(
                    color = Color(0x3310B981),
                    topLeft = Offset(w * 0.2f, h * 0.3f),
                    size = Size(w * 0.6f, h * 0.4f)
                )

                // Polarized Dipoles (+ -)
                val wobble = sin(Math.toRadians((phase * 2).toDouble())).toFloat() * 4f
                for (row in 0..2) {
                    for (col in 0..5) {
                        val cx = w * 0.26f + col * 48f
                        val cy = h * 0.38f + row * 32f
                        drawCircle(color = Color(0xFFEF4444), radius = 6f, center = Offset(cx - 8f, cy + wobble))
                        drawCircle(color = Color(0xFF38BDF8), radius = 6f, center = Offset(cx + 8f, cy - wobble))
                        drawLine(color = Color.White, start = Offset(cx - 8f, cy), end = Offset(cx + 8f, cy), strokeWidth = 2f)
                    }
                }
            }

            AnimationSceneType.EQUILIBRIUM_SHIFT -> {
                // Chemical chamber with moving Piston
                val pistonY = h * 0.25f + sin(Math.toRadians(phase.toDouble())).toFloat() * 20f

                // Chamber walls
                drawLine(color = Color.White, start = Offset(w * 0.2f, h * 0.15f), end = Offset(w * 0.2f, h * 0.85f), strokeWidth = 5f)
                drawLine(color = Color.White, start = Offset(w * 0.8f, h * 0.15f), end = Offset(w * 0.8f, h * 0.85f), strokeWidth = 5f)
                drawLine(color = Color.White, start = Offset(w * 0.2f, h * 0.85f), end = Offset(w * 0.8f, h * 0.85f), strokeWidth = 5f)

                // Piston head
                drawRect(
                    color = Color(0xFF94A3B8),
                    topLeft = Offset(w * 0.21f, pistonY),
                    size = Size(w * 0.58f, 16f)
                )

                // Gas particles colliding
                for (i in 0..14) {
                    val angle = Math.toRadians((phase * (i + 1) * 23 % 360).toDouble())
                    val px = w * 0.5f + (cos(angle) * (w * 0.24f)).toFloat()
                    val py = (pistonY + 30f) + (sin(angle).toFloat().coerceAtLeast(0f) * (h * 0.45f))
                    val isDimer = i % 3 == 0
                    drawCircle(
                        color = if (isDimer) Color(0xFF10B981) else Color(0xFFF59E0B),
                        radius = if (isDimer) 8f else 5f,
                        center = Offset(px, py)
                    )
                }
            }

            AnimationSceneType.DNA_REPLICATION_FORK -> {
                // Replication Fork unwinding
                val forkPath = Path()
                forkPath.moveTo(w * 0.1f, h * 0.5f)
                forkPath.lineTo(w * 0.45f, h * 0.5f)
                forkPath.lineTo(w * 0.85f, h * 0.2f) // Leading
                drawPath(forkPath, color = Color(0xFF38BDF8), style = Stroke(width = 5f))

                val laggingPath = Path()
                laggingPath.moveTo(w * 0.1f, h * 0.5f + 14f)
                laggingPath.lineTo(w * 0.45f, h * 0.5f + 14f)
                laggingPath.lineTo(w * 0.85f, h * 0.8f) // Lagging
                drawPath(laggingPath, color = Color(0xFFF43F5E), style = Stroke(width = 5f))

                // Helicase hexamer ring
                drawCircle(
                    color = Color(0xFFF59E0B),
                    radius = 16f,
                    center = Offset(w * 0.45f, h * 0.57f)
                )

                // Synthesized fragments on lagging strand
                for (k in 0..2) {
                    val fx = w * 0.52f + k * 38f
                    val fy = h * 0.58f + k * 18f
                    drawLine(
                        color = Color(0xFF10B981),
                        start = Offset(fx, fy),
                        end = Offset(fx + 22f, fy + 12f),
                        strokeWidth = 6f
                    )
                }
            }

            AnimationSceneType.INTEGRAL_KINGS_PROPERTY, AnimationSceneType.CUSTOM_AI_SIMULATION -> {
                // Coordinate Axes
                drawLine(color = Color.Gray, start = Offset(w * 0.1f, h * 0.8f), end = Offset(w * 0.9f, h * 0.8f), strokeWidth = 3f)
                drawLine(color = Color.Gray, start = Offset(w * 0.1f, h * 0.8f), end = Offset(w * 0.1f, h * 0.15f), strokeWidth = 3f)

                // Original Function curve f(x)
                val curvePath = Path()
                curvePath.moveTo(w * 0.1f, h * 0.8f)
                for (step in 0..40) {
                    val x = w * 0.1f + step * (w * 0.7f / 40)
                    val rad = (step.toFloat() / 40f) * Math.PI
                    val y = (h * 0.8f) - (sin(rad).toFloat() * (h * 0.5f))
                    curvePath.lineTo(x, y)
                }
                drawPath(curvePath, color = Color(0xFF6366F1), style = Stroke(width = 4f))

                // Reflection f(a+b-x)
                val reflectPath = Path()
                reflectPath.moveTo(w * 0.1f, (h * 0.8f) - (sin(Math.PI).toFloat() * (h * 0.5f)))
                for (step in 0..40) {
                    val x = w * 0.1f + step * (w * 0.7f / 40)
                    val rad = ((40 - step).toFloat() / 40f) * Math.PI
                    val y = (h * 0.8f) - (sin(rad).toFloat() * (h * 0.5f))
                    reflectPath.lineTo(x, y)
                }
                drawPath(reflectPath, color = Color(0xFF10B981), style = Stroke(width = 3f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f))))

                // Midpoint line
                drawLine(
                    color = Color(0xFFF59E0B),
                    start = Offset(w * 0.45f, h * 0.15f),
                    end = Offset(w * 0.45f, h * 0.8f),
                    strokeWidth = 2f
                )
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
