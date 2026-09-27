package com.example.data.model

enum class ExamType(val displayName: String, val badgeColorHex: Long) {
    ALL("All Exams", 0xFF1E1B4B),
    NEET("NEET UG", 0xFFF43F5E),
    JEE_MAIN("JEE Main", 0xFF4648D4),
    JEE_ADVANCED("JEE Advanced", 0xFF10B981)
}

enum class Subject(val displayName: String, val shortName: String, val accentColorHex: Long) {
    PHYSICS("Physics", "PHY", 0xFF4648D4),
    CHEMISTRY("Chemistry", "CHEM", 0xFF0EA5E9),
    BIOLOGY("Biology", "BIO", 0xFF10B981),
    MATHEMATICS("Mathematics", "MATH", 0xFFF59E0B)
}

data class PyqMarker(
    val examText: String,      // e.g. "NEET '21, '23"
    val frequencyLabel: String, // e.g. "High Yield"
    val yearCount: Int
)

data class TrapAlert(
    val title: String,
    val description: String,
    val commonMistake: String
)

data class FormulaCard(
    val id: String,
    val name: String,
    val latexFormula: String,
    val subject: Subject,
    val chapterName: String,
    val classGrade: Int = 12,
    val variables: List<String>,
    val siUnits: String,
    val dimensionalFormula: String,
    val crucialCaveats: String,
    val isHighYield: Boolean = true
)

data class TextbookSection(
    val id: String,
    val chapterId: String,
    val title: String,
    val subheader: String,
    val content: String,
    val pyqMarker: PyqMarker? = null,
    val trapAlert: TrapAlert? = null,
    val keyDerivation: String? = null,
    val ncertReference: String? = null,
    val coreConceptPill: String? = null
)

enum class GradeGroup(val displayName: String, val rangeLabel: String) {
    FOUNDATION_5_8("Middle School (5th-8th)", "Classes 5 to 8"),
    SECONDARY_9_10("Secondary (9th-10th)", "Classes 9 & 10"),
    SENIOR_11_12("Senior Secondary (11th-12th)", "Classes 11 & 12 / NEET-JEE")
}

data class Chapter(
    val id: String,
    val name: String,
    val subject: Subject,
    val classGrade: Int = 12, // 5 to 12
    val weightage: String, // e.g. "8-10% High Yield" or "Core Board Syllabus"
    val pyqCount: Int,
    val summary: String,
    val targetExam: ExamType,
    val sections: List<TextbookSection>,
    val videoLesson: ChapterVideoLesson? = null
)

enum class AnimationSceneType {
    ELECTROSTATIC_FLUX,
    DIELECTRIC_POLARIZATION,
    EQUILIBRIUM_SHIFT,
    DNA_REPLICATION_FORK,
    INTEGRAL_KINGS_PROPERTY,
    CUSTOM_AI_SIMULATION
}

data class VideoScene(
    val id: String,
    val title: String,
    val subtitle: String,
    val narration: String,
    val animationType: AnimationSceneType,
    val durationSeconds: Int,
    val visualHighlights: List<String>
)

data class KeyframeMilestone(
    val timestampSeconds: Int,
    val label: String,
    val highYieldAlert: String
)

data class ChapterVideoLesson(
    val id: String,
    val chapterId: String,
    val title: String,
    val subtitle: String,
    val totalDurationSeconds: Int,
    val scenes: List<VideoScene>,
    val keyframes: List<KeyframeMilestone>,
    val aiPromptUsed: String,
    val keyTakeaways: List<String>
)

data class PracticeQuestion(
    val id: String,
    val chapterId: String,
    val subject: Subject,
    val examType: ExamType,
    val classGrade: Int = 12,
    val yearTag: String, // e.g. "NEET 2023" or "CBSE Class 10 Board"
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val trapOptionIndex: Int,
    val trapReason: String,
    val stepByStepSolution: List<String>,
    val ncertLineRef: String,
    val difficulty: String // "Easy", "Medium", "Hard"
)

data class FlashcardItem(
    val id: String,
    val front: String,
    val back: String,
    val subject: Subject,
    val chapterName: String,
    val classGrade: Int = 12,
    val formulaSnippet: String? = null
)
