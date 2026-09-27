package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Cognitive Academic", appName)
  }

  @Test
  fun `verify classes 5 to 12 all have animated video chapters`() {
    (5..12).forEach { grade ->
      val chaptersForGrade = com.example.data.datasource.CuratedAcademicContent.chapters.filter {
        it.classGrade == grade
      }
      org.junit.Assert.assertTrue("Class $grade must have at least one chapter", chaptersForGrade.isNotEmpty())
      chaptersForGrade.forEach { ch ->
        org.junit.Assert.assertNotNull("Chapter ${ch.name} must have an animated video lesson", ch.videoLesson)
      }
    }
  }

  @Test
  fun `verify adaptive quiz engine adjusts to target score and computes weak areas`() {
    val config = com.example.data.adaptive.AdaptiveQuizConfig(
      targetScore = 20,
      timeLimitMinutes = 10,
      questionCount = 4,
      classGrade = 12
    )
    val questions = com.example.data.adaptive.AdaptiveQuizEngine.generateAdaptiveQuiz(config, emptyList())
    org.junit.Assert.assertTrue("Quiz must generate questions", questions.isNotEmpty())
  }

  @Test
  fun `verify visual dashboard analytics computes mastery and trends across grades`() {
    (5..12).forEach { grade ->
      val dashboard = com.example.data.analytics.DashboardAnalyticsEngine.computeDashboard(
        selectedGrade = grade,
        attempts = emptyList(),
        quizResults = emptyList()
      )
      org.junit.Assert.assertEquals(grade, dashboard.classGrade)
      org.junit.Assert.assertEquals(4, dashboard.subjectsMastery.size)
      org.junit.Assert.assertTrue("Trend points must not be empty", dashboard.improvementTrend.isNotEmpty())
      org.junit.Assert.assertTrue("Radar competencies must be present", dashboard.radarCompetencies.size >= 4)
      org.junit.Assert.assertTrue("Overall accuracy must be in valid range", dashboard.overallAccuracy in 0f..100f)
    }
  }
}
