package com.example.data.api

import com.example.data.model.*
import java.util.Locale

object DynamicTextAnalyzer {

    private val commonTechSkills = listOf(
        "Kotlin", "Java", "Python", "JavaScript", "TypeScript", "C++", "C#", "Go", "Rust", "Swift",
        "React", "Vue", "Angular", "Next.js", "Node.js", "Express", "FastAPI", "Django", "Flask",
        "Android", "Jetpack Compose", "Room", "Coroutines", "iOS", "SwiftUI", "Flutter",
        "SQL", "PostgreSQL", "MySQL", "MongoDB", "Redis", "Firebase", "Supabase", "GraphQL", "REST APIs",
        "Docker", "Kubernetes", "AWS", "GCP", "Azure", "CI/CD", "Git", "GitHub Actions",
        "System Architecture", "Microservices", "Unit Testing", "Agile", "Scrum", "Jira"
    )

    fun analyze(resumeText: String, jobDescription: String): AnalysisResult {
        val cleanResume = resumeText.lowercase(Locale.ROOT)
        val cleanJd = jobDescription.lowercase(Locale.ROOT)

        val jdSkills = commonTechSkills.filter { cleanJd.contains(it.lowercase(Locale.ROOT)) }.toSet()
        val resumeSkills = commonTechSkills.filter { cleanResume.contains(it.lowercase(Locale.ROOT)) }.toSet()

        val matching = if (jdSkills.isNotEmpty()) {
            jdSkills.intersect(resumeSkills).toList()
        } else {
            resumeSkills.toList()
        }

        val missing = if (jdSkills.isNotEmpty()) {
            jdSkills.subtract(resumeSkills).toList()
        } else {
            listOf("Docker", "Kubernetes", "CI/CD Pipelines", "System Architecture")
        }

        val totalRelevant = (matching.size + missing.size).coerceAtLeast(1)
        val matchScore = ((matching.size.toFloat() / totalRelevant) * 100).toInt().coerceIn(45, 98)
        val atsScore = (matchScore + 5).coerceIn(50, 95)

        // Extract bullets from resume
        val rawBullets = resumeText.lines()
            .map { it.trim().removePrefix("-").removePrefix("•").removePrefix("*").trim() }
            .filter { it.length > 20 }

        val improvedBullets = if (rawBullets.isNotEmpty()) {
            rawBullets.take(3).map { bullet ->
                BulletComparison(
                    original = bullet,
                    improved = "$bullet, improving system performance by 35% through optimized design patterns.",
                    reasoning = "Added quantifiable SLA metrics and key terminology for ATS parsers."
                )
            }
        } else {
            listOf(
                BulletComparison(
                    original = "Developed software modules and user interfaces.",
                    improved = "Engineered high-concurrency microservices and responsive web UI, cutting load times by 35%.",
                    reasoning = "Replaced generic action verbs with high-impact engineering metrics."
                )
            )
        }

        val atsIssues = mutableListOf<AtsIssue>()
        if (missing.isNotEmpty()) {
            atsIssues.add(
                AtsIssue(
                    category = "Keywords",
                    issue = "Missing key competencies: ${missing.take(3).joinToString(", ")}.",
                    recommendation = "Incorporate target skills directly into work experience and skill sections.",
                    severity = "High"
                )
            )
        }
        if (!resumeText.contains("•") && !resumeText.contains("-")) {
            atsIssues.add(
                AtsIssue(
                    category = "Formatting",
                    issue = "Resume text lacks standardized bullet point delimiters.",
                    recommendation = "Use clean bullet symbols for optimal ATS parser section detection.",
                    severity = "Medium"
                )
            )
        }
        if (atsIssues.isEmpty()) {
            atsIssues.add(
                AtsIssue(
                    category = "Impact",
                    issue = "Quantifiable metrics could be emphasized further.",
                    recommendation = "Include percentage improvements and user volume numbers in work history.",
                    severity = "Low"
                )
            )
        }

        val rewrittenText = """
            PROFESSIONAL RESUME (ATS OPTIMIZED)
            
            SUMMARY
            Experienced tech professional with strong background in ${matching.take(4).joinToString(", ").ifEmpty { "Software Engineering" }}.
            
            CORE SKILLS
            ${(matching + missing).joinToString(" • ")}
            
            KEY ACHIEVEMENTS
            ${improvedBullets.joinToString("\n") { "• ${it.improved}" }}
            
            ORIGINAL CONTENT:
            $resumeText
        """.trimIndent()

        return AnalysisResult(
            matchScore = matchScore,
            atsScore = atsScore,
            matchingSkills = if (matching.isNotEmpty()) matching else listOf("Software Development", "Problem Solving"),
            missingSkills = missing,
            improvedBullets = improvedBullets,
            atsIssues = atsIssues,
            rewrittenResumeText = rewrittenText
        )
    }

    fun generateSkillGaps(resumeText: String, jobDescription: String): SkillGapReport {
        val analysis = analyze(resumeText, jobDescription)
        val gaps = analysis.missingSkills.mapIndexed { index, skill ->
            SkillGapItem(
                skillName = skill,
                category = if (skill in listOf("Docker", "Kubernetes", "AWS", "CI/CD")) "DevOps & Cloud" else "Technical Core",
                priority = if (index < 2) "HIGH" else "MEDIUM",
                currentLevel = "Basic",
                requiredLevel = "Advanced",
                impactExplanation = "Required by target job specification to lead architecture and maintain system reliability."
            )
        }

        return SkillGapReport(
            currentSkills = analysis.matchingSkills,
            requiredSkills = analysis.matchingSkills + analysis.missingSkills,
            gaps = gaps,
            keyConceptsToMaster = gaps.map { "Mastering ${it.skillName} for production application deployment" }
        )
    }

    fun generateStudyPlan(report: SkillGapReport, durationWeeks: Int): StudyPlan {
        val weeksList = mutableListOf<WeekPlan>()
        val gapSkills = report.gaps.map { it.skillName }.ifEmpty { listOf("System Architecture", "Cloud Infrastructure") }

        val weeksCount = durationWeeks.coerceIn(1, 8)
        val skillsPerWeek = (gapSkills.size + weeksCount - 1) / weeksCount

        for (w in 1..weeksCount) {
            val weekSkills = gapSkills.drop((w - 1) * skillsPerWeek).take(skillsPerWeek.coerceAtLeast(1))
            val titleSkill = weekSkills.firstOrNull() ?: "Technical Mastery"
            weeksList.add(
                WeekPlan(
                    weekNumber = w,
                    title = "Week $w: $titleSkill & Architecture",
                    focusSkills = if (weekSkills.isNotEmpty()) weekSkills else listOf(titleSkill),
                    topics = listOf(
                        "Deep-dive into $titleSkill core concepts and best practices",
                        "Building hands-on prototype integrating $titleSkill with existing stack",
                        "Optimizing performance, security, and automated testing"
                    ),
                    actionItems = listOf(
                        "Complete hands-on project incorporating $titleSkill",
                        "Document architecture decisions and write unit test coverage",
                        "Review progress and update resume with new project achievements"
                    ),
                    isCompleted = w == 1
                )
            )
        }

        return StudyPlan(
            durationWeeks = weeksCount,
            weeks = weeksList
        )
    }
}
