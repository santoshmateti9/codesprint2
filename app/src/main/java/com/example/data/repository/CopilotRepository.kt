package com.example.data.repository

import com.example.data.SampleData
import com.example.data.api.DynamicTextAnalyzer
import com.example.data.api.GeminiService
import com.example.data.local.CopilotDao
import com.example.data.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CopilotRepository(
    private val copilotDao: CopilotDao,
    private val geminiService: GeminiService
) {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = "Alex Rivera",
            email = "alex.rivera@example.com",
            isLoggedIn = true,
            targetRole = "Senior Full Stack / Mobile Engineer",
            experienceLevel = "Mid-Level (3+ years)",
            preferredDurationWeeks = 4,
            knownSkills = listOf("Kotlin", "Jetpack Compose", "React", "REST APIs", "Git"),
            careerGoalDescription = "Transition into a Senior Engineer role leading mobile & cloud architecture."
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun loginUser(name: String, email: String) {
        _userProfile.value = _userProfile.value.copy(
            name = name.ifBlank { "Career Professional" },
            email = email.ifBlank { "user@example.com" },
            isLoggedIn = true
        )
    }

    fun logoutUser() {
        _userProfile.value = _userProfile.value.copy(
            isLoggedIn = false
        )
    }

    suspend fun submitCareerAssessmentForm(input: CareerFormInput) {
        val parsedKnownSkills = input.currentSkills.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .ifEmpty { listOf("Kotlin", "Java", "REST APIs", "Git") }

        _userProfile.value = UserProfile(
            name = input.userName.ifBlank { _userProfile.value.name },
            email = input.userEmail.ifBlank { _userProfile.value.email },
            isLoggedIn = true,
            targetRole = input.targetPosition.ifBlank { "Senior Software Engineer" },
            experienceLevel = input.experienceLevel,
            preferredDurationWeeks = input.timelineWeeks,
            knownSkills = parsedKnownSkills,
            careerGoalDescription = input.careerGoals
        )

        // Build tailored job description based on user target role
        val generatedJd = """
            We are actively hiring a ${input.targetPosition.ifBlank { "Software Engineer" }} at ${input.companyPreference.ifBlank { "Tech Enterprise" }}.
            Target Experience: ${input.experienceLevel}.
            Key Responsibilities & System Requirements:
            - Architect and maintain enterprise production services and responsive applications.
            - Deep expertise in ${parsedKnownSkills.take(3).joinToString(", ")}, System Design, Cloud Deployments, Microservices, and Automated Testing.
            - Lead sprint planning, peer code reviews, and cross-functional performance optimization.
            Candidate Goals: ${input.careerGoals}
        """.trimIndent()

        val generatedResume = """
            NAME: ${input.userName.ifBlank { "Career Candidate" }}
            EMAIL: ${input.userEmail.ifBlank { "candidate@example.com" }}
            TARGET ROLE: ${input.targetPosition.ifBlank { "Software Engineer" }} (${input.experienceLevel})
            
            SUMMARY
            Motivated technology professional with expertise in ${parsedKnownSkills.joinToString(", ")}. Goal: ${input.careerGoals}
            
            CORE COMPETENCIES & SKILLS
            ${parsedKnownSkills.joinToString(" • ")}
            
            EXPERIENCE & ACHIEVEMENTS
            • Developed scalable features using ${parsedKnownSkills.firstOrNull() ?: "Modern Tech"}, improving system throughput by 30%.
            • Collaborated with cross-functional teams to deploy cloud infrastructure and responsive user interfaces.
            • Implemented unit tests and automated CI/CD code quality checks.
        """.trimIndent()

        _copilotContext.value = CopilotContext(
            resume = ResumeData("${input.userName.lowercase().replace(" ", "_").ifEmpty { "my" }}_resume.pdf", generatedResume),
            jobTarget = JobTargetData(input.targetPosition.ifBlank { "Software Engineer" }, input.companyPreference, generatedJd)
        )

        // Generate tailored analysis, skill gap report, and study roadmap
        val skillGap = DynamicTextAnalyzer.generateSkillGaps(generatedResume, generatedJd)
        _skillGapReport.value = skillGap

        val plan = DynamicTextAnalyzer.generateStudyPlan(skillGap, input.timelineWeeks)
        _studyPlan.value = plan

        _analysisResult.value = DynamicTextAnalyzer.analyze(generatedResume, generatedJd)
        saveStateToRoom()
    }

    private val _copilotContext = MutableStateFlow(
        CopilotContext(
            resume = ResumeData("alex_rivera_resume.pdf", SampleData.sampleResumeText),
            jobTarget = JobTargetData("Senior Full Stack / Mobile Engineer", "TechCorp Solutions", SampleData.sampleJobDescription)
        )
    )
    val copilotContext: StateFlow<CopilotContext> = _copilotContext.asStateFlow()

    private val _analysisResult = MutableStateFlow<AnalysisResult?>(SampleData.sampleAnalysisResult)
    val analysisResult: StateFlow<AnalysisResult?> = _analysisResult.asStateFlow()

    private val _interviewSession = MutableStateFlow<InterviewSession?>(SampleData.sampleInitialInterview)
    val interviewSession: StateFlow<InterviewSession?> = _interviewSession.asStateFlow()

    private val _skillGapReport = MutableStateFlow<SkillGapReport?>(SampleData.sampleSkillGapReport)
    val skillGapReport: StateFlow<SkillGapReport?> = _skillGapReport.asStateFlow()

    private val _studyPlan = MutableStateFlow<StudyPlan?>(SampleData.sampleStudyPlan)
    val studyPlan: StateFlow<StudyPlan?> = _studyPlan.asStateFlow()

    val persistedState: Flow<CopilotStateEntity?> = copilotDao.getCopilotState()

    fun updateResumeText(filename: String, text: String) {
        _copilotContext.value = _copilotContext.value.copy(
            resume = ResumeData(filename, text)
        )
    }

    fun updateJobTarget(title: String, company: String, description: String) {
        _copilotContext.value = _copilotContext.value.copy(
            jobTarget = JobTargetData(title, company, description)
        )
    }

    fun loadDemoSampleData() {
        _copilotContext.value = CopilotContext(
            resume = ResumeData("alex_rivera_resume.pdf", SampleData.sampleResumeText),
            jobTarget = JobTargetData("Senior Full Stack / Mobile Engineer", "TechCorp Solutions", SampleData.sampleJobDescription)
        )
        _analysisResult.value = SampleData.sampleAnalysisResult
        _interviewSession.value = SampleData.sampleInitialInterview
        _skillGapReport.value = SampleData.sampleSkillGapReport
        _studyPlan.value = SampleData.sampleStudyPlan
    }

    suspend fun runResumeAnalysis(): AnalysisResult {
        val currentContext = _copilotContext.value
        val result = geminiService.analyzeResumeAndAts(
            resumeText = currentContext.resume.parsedText,
            jobDescription = currentContext.jobTarget.description
        )
        _analysisResult.value = result
        saveStateToRoom()
        return result
    }

    suspend fun startNewInterviewSession(): InterviewSession {
        val currentContext = _copilotContext.value
        val session = geminiService.startInterview(
            resumeText = currentContext.resume.parsedText,
            jobTarget = currentContext.jobTarget
        )
        _interviewSession.value = session
        saveStateToRoom()
        return session
    }

    suspend fun sendInterviewUserMessage(userMessageText: String): InterviewSession {
        val currentSession = _interviewSession.value ?: return startNewInterviewSession()
        val currentContext = _copilotContext.value

        val updatedHistory = currentSession.chatHistory + ChatMessage(sender = "candidate", text = userMessageText)
        _interviewSession.value = currentSession.copy(chatHistory = updatedHistory)

        val (interviewerReply, eval, isComplete) = geminiService.processInterviewChatTurn(
            resumeText = currentContext.resume.parsedText,
            jobTarget = currentContext.jobTarget,
            chatHistory = updatedHistory,
            userMessage = userMessageText
        )

        val newHistory = updatedHistory + interviewerReply
        
        // Calculate dynamic running average score
        val scores = (if (currentSession.lastEvaluation != null) listOf(currentSession.lastEvaluation.score) else emptyList()) + eval.score
        val avg = scores.average()

        val newSession = currentSession.copy(
            chatHistory = newHistory,
            lastEvaluation = eval,
            avgScore = avg,
            isComplete = isComplete
        )

        _interviewSession.value = newSession
        saveStateToRoom()
        return newSession
    }

    suspend fun runSkillGapAnalysis(): SkillGapReport {
        val currentContext = _copilotContext.value
        val report = geminiService.analyzeSkillGaps(
            resumeText = currentContext.resume.parsedText,
            jobDescription = currentContext.jobTarget.description
        )
        _skillGapReport.value = report
        saveStateToRoom()
        return report
    }

    suspend fun generateStudyPlan(weeks: Int = 4): StudyPlan {
        val currentGapReport = _skillGapReport.value ?: runSkillGapAnalysis()
        val plan = geminiService.generateStudyPlan(currentGapReport, weeks)
        _studyPlan.value = plan
        saveStateToRoom()
        return plan
    }

    fun toggleWeekCompletion(weekNumber: Int) {
        val currentPlan = _studyPlan.value ?: return
        val updatedWeeks = currentPlan.weeks.map { week ->
            if (week.weekNumber == weekNumber) {
                week.copy(isCompleted = !week.isCompleted)
            } else week
        }
        _studyPlan.value = currentPlan.copy(weeks = updatedWeeks)
    }

    private suspend fun saveStateToRoom() {
        val context = _copilotContext.value
        val entity = CopilotStateEntity(
            id = 1,
            resumeText = context.resume.parsedText,
            resumeFilename = context.resume.filename,
            jobTitle = context.jobTarget.title,
            jobCompany = context.jobTarget.company,
            jobDescription = context.jobTarget.description,
            updatedAt = System.currentTimeMillis()
        )
        copilotDao.saveCopilotState(entity)
    }
}
