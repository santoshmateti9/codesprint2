package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiService
import com.example.data.local.CopilotDatabase
import com.example.data.model.*
import com.example.data.repository.CopilotRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CopilotViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CopilotRepository

    init {
        val database = CopilotDatabase.getDatabase(application)
        val geminiService = GeminiService()
        repository = CopilotRepository(database.copilotDao(), geminiService)
    }

    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val copilotContext: StateFlow<CopilotContext> = repository.copilotContext
    val analysisResult: StateFlow<AnalysisResult?> = repository.analysisResult
    val interviewSession: StateFlow<InterviewSession?> = repository.interviewSession
    val skillGapReport: StateFlow<SkillGapReport?> = repository.skillGapReport
    val studyPlan: StateFlow<StudyPlan?> = repository.studyPlan

    private val _isSubmittingAssessment = MutableStateFlow(false)
    val isSubmittingAssessment: StateFlow<Boolean> = _isSubmittingAssessment.asStateFlow()

    fun loginUser(name: String, email: String) {
        repository.loginUser(name, email)
        _toastMessage.value = "Welcome, ${name.ifBlank { "User" }}!"
    }

    fun logoutUser() {
        repository.logoutUser()
        _toastMessage.value = "Logged out successfully"
    }

    fun submitCareerAssessment(input: CareerFormInput) {
        viewModelScope.launch {
            _isSubmittingAssessment.value = true
            try {
                repository.submitCareerAssessmentForm(input)
                _toastMessage.value = "Tailored roadmap & skill gap report generated for ${input.targetPosition}!"
            } catch (e: Exception) {
                _toastMessage.value = "Assessment failed: ${e.message}"
            } finally {
                _isSubmittingAssessment.value = false
            }
        }
    }

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _isInterviewThinking = MutableStateFlow(false)
    val isInterviewThinking: StateFlow<Boolean> = _isInterviewThinking.asStateFlow()

    private val _isSkillGapAnalyzing = MutableStateFlow(false)
    val isSkillGapAnalyzing: StateFlow<Boolean> = _isSkillGapAnalyzing.asStateFlow()

    private val _isStudyPlanGenerating = MutableStateFlow(false)
    val isStudyPlanGenerating: StateFlow<Boolean> = _isStudyPlanGenerating.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun loadDemoSampleData() {
        repository.loadDemoSampleData()
        _toastMessage.value = "Demo sample resume & job target loaded!"
    }

    fun updateResumeText(filename: String, text: String) {
        repository.updateResumeText(filename, text)
    }

    fun updateJobTarget(title: String, company: String, description: String) {
        repository.updateJobTarget(title, company, description)
    }

    fun runResumeAnalysis() {
        viewModelScope.launch {
            _isAnalyzing.value = true
            try {
                repository.runResumeAnalysis()
                _toastMessage.value = "Resume analysis & ATS check completed!"
            } catch (e: Exception) {
                _toastMessage.value = "Analysis failed: ${e.message}"
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun startNewInterview() {
        viewModelScope.launch {
            _isInterviewThinking.value = true
            try {
                repository.startNewInterviewSession()
                _toastMessage.value = "AI Interview session started!"
            } catch (e: Exception) {
                _toastMessage.value = "Failed to start interview: ${e.message}"
            } finally {
                _isInterviewThinking.value = false
            }
        }
    }

    fun sendInterviewMessage(messageText: String) {
        if (messageText.isBlank()) return
        viewModelScope.launch {
            _isInterviewThinking.value = true
            try {
                repository.sendInterviewUserMessage(messageText)
            } catch (e: Exception) {
                _toastMessage.value = "Chat response error: ${e.message}"
            } finally {
                _isInterviewThinking.value = false
            }
        }
    }

    fun runSkillGapAnalysis() {
        viewModelScope.launch {
            _isSkillGapAnalyzing.value = true
            try {
                repository.runSkillGapAnalysis()
                _toastMessage.value = "Skill gap analysis generated!"
            } catch (e: Exception) {
                _toastMessage.value = "Skill gap check failed: ${e.message}"
            } finally {
                _isSkillGapAnalyzing.value = false
            }
        }
    }

    fun generateStudyPlan(durationWeeks: Int = 4) {
        viewModelScope.launch {
            _isStudyPlanGenerating.value = true
            try {
                repository.generateStudyPlan(durationWeeks)
                _toastMessage.value = "Personalized $durationWeeks-week study plan ready!"
            } catch (e: Exception) {
                _toastMessage.value = "Study plan creation error: ${e.message}"
            } finally {
                _isStudyPlanGenerating.value = false
            }
        }
    }

    fun toggleWeekCompletion(weekNumber: Int) {
        repository.toggleWeekCompletion(weekNumber)
    }
}
