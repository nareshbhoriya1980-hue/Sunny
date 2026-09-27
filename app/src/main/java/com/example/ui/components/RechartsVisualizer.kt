package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.analytics.RadarCompetency
import com.example.data.analytics.SubjectMasteryData
import com.example.data.analytics.TrendPoint
import com.example.ui.theme.AcademicElectricViolet
import com.example.ui.theme.AcademicPrimary
import com.example.ui.theme.HighYieldGold
import com.example.ui.theme.MasteryEmerald
import kotlin.math.cos
import kotlin.math.sin

/**
 * Recharts-style Interactive Multi-Subject Mastery Bar Chart.
 * Displays side-by-side grouped bars for each subject: Current Accuracy vs Target Benchmark.
 */
@Composable
fun RechartsBarChart(
    subjects: List<SubjectMasteryData>,
    modifier: Modifier = Modifier,
    onSubjectSelected: (SubjectMasteryData) -> Unit = {}
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(subjects) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    val selectedSubject = subjects.getOrNull(selectedIndex)

    Column(modifier = modifier.fillMaxWidth()) {
        // Legend & Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Current Student Mastery
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(AcademicElectricViolet, RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Current Mastery",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AcademicPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                // Target Benchmark
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFFCBD5E1), RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Grade Benchmark",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }

            Text(
                text = "Tap bar for details",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontSize = 10.sp)
            )
        }

        // Canvas Recharts Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("recharts_bar_chart_canvas")
                    .pointerInput(subjects) {
                        detectTapGestures { offset ->
                            val width = size.width
                            val step = width / subjects.size
                            val tappedIdx = (offset.x / step).toInt().coerceIn(0, subjects.size - 1)
                            selectedIndex = tappedIdx
                            onSubjectSelected(subjects[tappedIdx])
                        }
                    }
            ) {
                val chartWidth = size.width
                val chartHeight = size.height - 30f // space for X-axis labels
                val count = subjects.size
                if (count == 0) return@Canvas

                // 1. Draw horizontal grid lines (0%, 25%, 50%, 75%, 100%)
                val gridLevels = listOf(0.25f, 0.50f, 0.75f, 1.0f)
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

                gridLevels.forEach { level ->
                    val y = chartHeight * (1f - level)
                    drawLine(
                        color = Color(0xFFE2E8F0),
                        start = Offset(0f, y),
                        end = Offset(chartWidth, y),
                        strokeWidth = 1f,
                        pathEffect = dashEffect
                    )
                }

                val groupWidth = chartWidth / count
                val barWidth = groupWidth * 0.28f
                val spacingBetweenBars = 4f

                subjects.forEachIndexed { index, data ->
                    val groupCenterX = index * groupWidth + groupWidth / 2f
                    val currentVal = (data.currentAccuracy / 100f).coerceIn(0f, 1f) * animatedProgress.value
                    val targetVal = (data.targetBenchmark / 100f).coerceIn(0f, 1f) * animatedProgress.value

                    val isSelected = selectedIndex == index

                    // Highlight column if selected
                    if (isSelected) {
                        drawRoundRect(
                            color = AcademicElectricViolet.copy(alpha = 0.08f),
                            topLeft = Offset(index * groupWidth + 4f, 0f),
                            size = Size(groupWidth - 8f, chartHeight),
                            cornerRadius = CornerRadius(6f, 6f)
                        )
                    }

                    // Bar 1: Benchmark Target (Gray / Muted Blue)
                    val targetBarHeight = chartHeight * targetVal
                    val targetBarLeft = groupCenterX - barWidth - (spacingBetweenBars / 2f)
                    drawRoundRect(
                        color = if (isSelected) Color(0xFF94A3B8) else Color(0xFFCBD5E1),
                        topLeft = Offset(targetBarLeft, chartHeight - targetBarHeight),
                        size = Size(barWidth, targetBarHeight),
                        cornerRadius = CornerRadius(4f, 4f)
                    )

                    // Bar 2: Student Current Mastery (Vibrant Gradient / Primary Accent)
                    val currentBarHeight = chartHeight * currentVal
                    val currentBarLeft = groupCenterX + (spacingBetweenBars / 2f)
                    val barBrush = Brush.verticalGradient(
                        colors = if (isSelected) {
                            listOf(Color(0xFF818CF8), Color(0xFF4338CA))
                        } else {
                            listOf(Color(0xFF6366F1), Color(0xFF312E81))
                        }
                    )

                    drawRoundRect(
                        brush = barBrush,
                        topLeft = Offset(currentBarLeft, chartHeight - currentBarHeight),
                        size = Size(barWidth, currentBarHeight),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
            }

            // Interactive Tooltip Overlay when a bar is tapped
            selectedSubject?.let { subjData ->
                Surface(
                    color = AcademicPrimary,
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column {
                            Text(
                                text = subjData.subject.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Current: ${subjData.currentAccuracy.toInt()}%  •  Target: ${subjData.targetBenchmark.toInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFFC4C1FB),
                                    fontSize = 10.sp
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(percent = 50),
                            color = MasteryEmerald
                        ) {
                            Text(
                                text = "+${subjData.trendDeltaPercent.toInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // X-Axis Labels (Subject Short Names)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            subjects.forEachIndexed { idx, item ->
                val isSelected = selectedIndex == idx
                Text(
                    text = item.subject.shortName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) AcademicElectricViolet else AcademicPrimary,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

/**
 * Recharts-style Area & Trend Curve Chart.
 * Displays historical learning progress trajectory with smooth Bézier curve, gradient area fill, and active dots.
 */
@Composable
fun RechartsAreaTrendChart(
    trendPoints: List<TrendPoint>,
    modifier: Modifier = Modifier
) {
    var activePointIndex by remember { mutableIntStateOf(trendPoints.lastIndex) }
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(trendPoints) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }

    val activePoint = trendPoints.getOrNull(activePointIndex) ?: trendPoints.lastOrNull()

    Column(modifier = modifier.fillMaxWidth()) {
        // Active Point Tooltip Header
        activePoint?.let { pt ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(MasteryEmerald, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${pt.label} (${pt.dateLabel}): ",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                    Text(
                        text = "${pt.scorePercent.toInt()}% Accuracy",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AcademicPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = Color(0xFFF2F3FF)
                ) {
                    Text(
                        text = "${pt.attemptsCount} questions evaluated",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AcademicElectricViolet,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("recharts_area_trend_canvas")
                    .pointerInput(trendPoints) {
                        detectTapGestures { offset ->
                            val width = size.width
                            val step = width / (trendPoints.size - 1).coerceAtLeast(1)
                            val tappedIdx = ((offset.x + step / 2f) / step).toInt()
                                .coerceIn(0, trendPoints.size - 1)
                            activePointIndex = tappedIdx
                        }
                    }
            ) {
                val chartWidth = size.width
                val chartHeight = size.height - 24f
                val count = trendPoints.size
                if (count < 2) return@Canvas

                val minVal = 40f
                val maxVal = 100f
                val valRange = maxVal - minVal

                // Compute point coordinates
                val points = trendPoints.mapIndexed { idx, pt ->
                    val x = idx * (chartWidth / (count - 1))
                    val normalizedY = ((pt.scorePercent - minVal) / valRange).coerceIn(0f, 1f)
                    val y = chartHeight * (1f - (normalizedY * animatedProgress.value))
                    Offset(x, y)
                }

                // Horizontal dashed grid
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                listOf(0.33f, 0.66f, 1.0f).forEach { frac ->
                    val y = chartHeight * (1f - frac)
                    drawLine(
                        color = Color(0xFFE2E8F0),
                        start = Offset(0f, y),
                        end = Offset(chartWidth, y),
                        strokeWidth = 1f,
                        pathEffect = dashEffect
                    )
                }

                // 1. Draw smooth Bézier curve Path
                val strokePath = Path().apply {
                    moveTo(points[0].x, points[0].y)
                    for (i in 0 until points.size - 1) {
                        val p0 = points[i]
                        val p1 = points[i + 1]
                        val cx = (p0.x + p1.x) / 2f
                        cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                    }
                }

                // 2. Draw gradient area underneath
                val fillPath = Path().apply {
                    addPath(strokePath)
                    lineTo(points.last().x, chartHeight)
                    lineTo(points.first().x, chartHeight)
                    close()
                }

                val areaGradient = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x666366F1),
                        Color(0x226366F1),
                        Color(0x00FAF8FF)
                    ),
                    startY = 0f,
                    endY = chartHeight
                )
                drawPath(fillPath, brush = areaGradient)

                // 3. Draw Stroke Line
                drawPath(
                    strokePath,
                    color = AcademicElectricViolet,
                    style = Stroke(width = 3.5f)
                )

                // 4. Draw Point Markers
                points.forEachIndexed { idx, pt ->
                    val isActive = idx == activePointIndex
                    if (isActive) {
                        // Halo Ring
                        drawCircle(
                            color = AcademicElectricViolet.copy(alpha = 0.25f),
                            radius = 12f,
                            center = pt
                        )
                        drawCircle(
                            color = AcademicElectricViolet,
                            radius = 6f,
                            center = pt
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3f,
                            center = pt
                        )
                    } else {
                        drawCircle(
                            color = Color.White,
                            radius = 4f,
                            center = pt
                        )
                        drawCircle(
                            color = AcademicElectricViolet,
                            radius = 4f,
                            center = pt,
                            style = Stroke(width = 2f)
                        )
                    }
                }
            }
        }

        // X-Axis Point Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            trendPoints.forEachIndexed { idx, pt ->
                val isSelected = activePointIndex == idx
                Text(
                    text = pt.label.replace("Session ", "S"),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) AcademicElectricViolet else Color.Gray
                    )
                )
            }
        }
    }
}

/**
 * Recharts-style Spider Radar Chart for Cognitive Exam Competencies.
 */
@Composable
fun RechartsRadarChart(
    competencies: List<RadarCompetency>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = (size.height.coerceAtMost(size.width) / 2f) - 30f
            val count = competencies.size
            if (count < 3) return@Canvas

            val angleStep = (2 * Math.PI / count).toFloat()

            // 1. Draw web grid rings (25%, 50%, 75%, 100%)
            listOf(0.25f, 0.5f, 0.75f, 1f).forEach { ringFrac ->
                val ringPath = Path()
                for (i in 0 until count) {
                    val angle = -Math.PI / 2 + (i * angleStep)
                    val r = radius * ringFrac
                    val x = center.x + (r * cos(angle)).toFloat()
                    val y = center.y + (r * sin(angle)).toFloat()
                    if (i == 0) ringPath.moveTo(x, y) else ringPath.lineTo(x, y)
                }
                ringPath.close()
                drawPath(
                    ringPath,
                    color = Color(0xFFE2E8F0),
                    style = Stroke(width = 1f)
                )
            }

            // 2. Draw axis spokes
            for (i in 0 until count) {
                val angle = -Math.PI / 2 + (i * angleStep)
                val x = center.x + (radius * cos(angle)).toFloat()
                val y = center.y + (radius * sin(angle)).toFloat()
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = center,
                    end = Offset(x, y),
                    strokeWidth = 1f
                )
            }

            // 3. Draw Student Polygon Path
            val studentPath = Path()
            val pointList = mutableListOf<Offset>()
            for (i in 0 until count) {
                val angle = -Math.PI / 2 + (i * angleStep)
                val frac = (competencies[i].scorePercent / 100f).coerceIn(0f, 1f)
                val r = radius * frac
                val x = center.x + (r * cos(angle)).toFloat()
                val y = center.y + (r * sin(angle)).toFloat()
                val pt = Offset(x, y)
                pointList.add(pt)
                if (i == 0) studentPath.moveTo(x, y) else studentPath.lineTo(x, y)
            }
            studentPath.close()

            // Fill & stroke
            drawPath(
                studentPath,
                color = AcademicElectricViolet.copy(alpha = 0.28f)
            )
            drawPath(
                studentPath,
                color = AcademicElectricViolet,
                style = Stroke(width = 2.5f)
            )

            // Draw vertex dots
            pointList.forEach { pt ->
                drawCircle(color = AcademicElectricViolet, radius = 4f, center = pt)
                drawCircle(color = Color.White, radius = 2f, center = pt)
            }
        }
    }
}
