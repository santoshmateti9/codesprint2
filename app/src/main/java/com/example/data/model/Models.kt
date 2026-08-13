package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ResumeData(
    val filename: String = "software_engineer_resume.pdf",
    val parsedText: String = ""
)

data class JobTargetData(
    val title: String = "Senior Full Stack Engineer",
    val company: String = "TechCorp Solutions",
    val description: String = ""
)

data class CopilotContext(
    val resume: ResumeData = ResumeData(),
    val jobTarget: JobTargetData = JobTargetData()
)

data class BulletComparison(
    val original: String,
    val improved: String,
    val reasoning: String
)

data class AtsIssue(
    val category: String, // Formatting, Keywords, Impact, Structure
    val issue: String,
    val recommendation: String,
    val severity: String // High, Medium, Low
)

data class AnalysisResult(
    val matchScore: Int = 85,
    val atsScore: Int = 88,
    val matchingSkills: List<String> = listOf("Kotlin", "Jetpack Compose", "REST APIs", "Coroutines", "Room DB", "Git"),
    val missingSkills: List<String> = listOf("GraphQL", "CI/CD Pipelines", "System Architecture", "Docker"),
    val improvedBullets: List<BulletComparison> = emptyList(),
    val atsIssues: List<AtsIssue> = emptyList(),
    val rewrittenResumeText: String = ""
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "interviewer" or "candidate"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class TurnEvaluation(
    val score: Int = 85,
    val strengths: String = "",
    val improvement: String = ""
)

data class InterviewSession(
    val id: String = java.util.UUID.randomUUID().toString(),
    val jobTitle: String = "Senior Full Stack Engineer",
    val chatHistory: List<ChatMessage> = emptyList(),
    val lastEvaluation: TurnEvaluation? = null,
    val avgScore: Double = 0.0,
    val isComplete: Boolean = false
)

data class SkillGapItem(
    val skillName: String,
    val category: String, // Technical, Soft Skill, Architecture
    val priority: String, // HIGH, MEDIUM, LOW
    val currentLevel: String, // None, Basic, Intermediate
    val requiredLevel: String, // Advanced, Expert
    val impactExplanation: String
)

data class SkillGapReport(
    val currentSkills: List<String> = emptyList(),
    val requiredSkills: List<String> = emptyList(),
    val gaps: List<SkillGapItem> = emptyList(),
    val keyConceptsToMaster: List<String> = emptyList()
)

data class WeekPlan(
    val weekNumber: Int,
    val title: String,
    val focusSkills: List<String>,
    val topics: List<String>,
    val actionItems: List<String>,
    var isCompleted: Boolean = false
)

data class StudyPlan(
    val durationWeeks: Int = 4,
    val weeks: List<WeekPlan> = emptyList()
)

data class UserProfile(
    val name: String = "Alex Rivera",
    val email: String = "alex.rivera@example.com",
    val isLoggedIn: Boolean = true,
    val targetRole: String = "Senior Full Stack / Mobile Engineer",
    val experienceLevel: String = "Mid-Level (3+ years)",
    val preferredDurationWeeks: Int = 4,
    val knownSkills: List<String> = listOf("Kotlin", "Jetpack Compose", "React", "REST APIs", "Git"),
    val careerGoalDescription: String = "Transition into a Senior Engineer role leading mobile & cloud architecture."
)

data class CareerFormInput(
    val userName: String = "",
    val userEmail: String = "",
    val targetPosition: String = "",
    val companyPreference: String = "Tier 1 Tech Company",
    val experienceLevel: String = "Mid-Level (2-4 years)",
    val currentSkills: String = "",
    val timelineWeeks: Int = 4,
    val careerGoals: String = ""
)

// Room Entity for persistent app state
@Entity(tableName = "copilot_state")
data class CopilotStateEntity(
    @PrimaryKey val id: Int = 1,
    val resumeText: String,
    val resumeFilename: String,
    val jobTitle: String,
    val jobCompany: String,
    val jobDescription: String,
    val lastAnalysisJson: String? = null,
    val lastInterviewJson: String? = null,
    val lastSkillGapJson: String? = null,
    val lastStudyPlanJson: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
