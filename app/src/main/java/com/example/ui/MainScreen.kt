package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.SharedContextCard
import com.example.ui.screens.*

enum class AppNavigationScreen {
    LOGIN,
    CAREER_ASSESSMENT,
    DASHBOARD
}

enum class CopilotModule(val title: String, val icon: ImageVector) {
    RESUME_ANALYZER("Resume", Icons.Default.Description),
    INTERVIEW_ASSISTANT("Interview", Icons.Default.Chat),
    SKILL_GAP("Skill Gaps", Icons.Default.Analytics),
    STUDY_PLANNER("Study Plan", Icons.Default.School)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: CopilotViewModel) {
    var currentScreen by remember { mutableStateOf(AppNavigationScreen.DASHBOARD) }
    var selectedModule by remember { mutableStateOf(CopilotModule.RESUME_ANALYZER) }

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val copilotContext by viewModel.copilotContext.collectAsStateWithLifecycle()
    val analysisResult by viewModel.analysisResult.collectAsStateWithLifecycle()
    val interviewSession by viewModel.interviewSession.collectAsStateWithLifecycle()
    val skillGapReport by viewModel.skillGapReport.collectAsStateWithLifecycle()
    val studyPlan by viewModel.studyPlan.collectAsStateWithLifecycle()

    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()
    val isInterviewThinking by viewModel.isInterviewThinking.collectAsStateWithLifecycle()
    val isSkillGapAnalyzing by viewModel.isSkillGapAnalyzing.collectAsStateWithLifecycle()
    val isStudyPlanGenerating by viewModel.isStudyPlanGenerating.collectAsStateWithLifecycle()
    val isSubmittingAssessment by viewModel.isSubmittingAssessment.collectAsStateWithLifecycle()

    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    when (currentScreen) {
        AppNavigationScreen.LOGIN -> {
            LoginScreen(
                onLoginSuccess = { name, email ->
                    viewModel.loginUser(name, email)
                    currentScreen = AppNavigationScreen.CAREER_ASSESSMENT
                }
            )
        }

        AppNavigationScreen.CAREER_ASSESSMENT -> {
            CareerAssessmentFormScreen(
                currentName = userProfile.name,
                currentEmail = userProfile.email,
                isSubmitting = isSubmittingAssessment,
                onSubmitAssessment = { input ->
                    viewModel.submitCareerAssessment(input)
                    currentScreen = AppNavigationScreen.DASHBOARD
                },
                onSkipToDashboard = {
                    currentScreen = AppNavigationScreen.DASHBOARD
                }
            )
        }

        AppNavigationScreen.DASHBOARD -> {
            var showUserMenu by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Column {
                                Text("AI Career Copilot", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                if (userProfile.targetRole.isNotBlank()) {
                                    Text(
                                        text = "${userProfile.name} • ${userProfile.targetRole}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        },
                        actions = {
                            // Career Requirement Form Button
                            IconButton(onClick = { currentScreen = AppNavigationScreen.CAREER_ASSESSMENT }) {
                                Icon(
                                    imageVector = Icons.Default.Assignment,
                                    contentDescription = "Career Requirement Form",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // User Profile Avatar & Menu
                            Box {
                                IconButton(onClick = { showUserMenu = true }) {
                                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "User Profile")
                                }

                                DropdownMenu(
                                    expanded = showUserMenu,
                                    onDismissRequest = { showUserMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Logged in as ${userProfile.name}") },
                                        onClick = {},
                                        enabled = false
                                    )
                                    Divider()
                                    DropdownMenuItem(
                                        text = { Text("Edit Career Form / Goals") },
                                        leadingIcon = { Icon(Icons.Default.EditNote, contentDescription = null) },
                                        onClick = {
                                            showUserMenu = false
                                            currentScreen = AppNavigationScreen.CAREER_ASSESSMENT
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Sign Out / Switch User") },
                                        leadingIcon = { Icon(Icons.Default.Logout, contentDescription = null) },
                                        onClick = {
                                            showUserMenu = false
                                            viewModel.logoutUser()
                                            currentScreen = AppNavigationScreen.LOGIN
                                        }
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        CopilotModule.values().forEach { module ->
                            NavigationBarItem(
                                selected = selectedModule == module,
                                onClick = { selectedModule = module },
                                icon = { Icon(imageVector = module.icon, contentDescription = module.title) },
                                label = { Text(module.title) }
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Shared Context Panel at top (persists across all 4 modules)
                    SharedContextCard(
                        contextState = copilotContext,
                        onUpdateResume = { filename, text -> viewModel.updateResumeText(filename, text) },
                        onUpdateJobTarget = { title, company, desc -> viewModel.updateJobTarget(title, company, desc) },
                        onLoadDemoData = { viewModel.loadDemoSampleData() },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Dynamic Module Container
                    Box(modifier = Modifier.weight(1f)) {
                        when (selectedModule) {
                            CopilotModule.RESUME_ANALYZER -> {
                                ResumeAnalyzerScreen(
                                    analysisResult = analysisResult,
                                    isAnalyzing = isAnalyzing,
                                    onRunAnalysis = { viewModel.runResumeAnalysis() }
                                )
                            }
                            CopilotModule.INTERVIEW_ASSISTANT -> {
                                InterviewAssistantScreen(
                                    session = interviewSession,
                                    isThinking = isInterviewThinking,
                                    onStartNewInterview = { viewModel.startNewInterview() },
                                    onSendMessage = { text -> viewModel.sendInterviewMessage(text) }
                                )
                            }
                            CopilotModule.SKILL_GAP -> {
                                SkillGapAnalyzerScreen(
                                    report = skillGapReport,
                                    isAnalyzing = isSkillGapAnalyzing,
                                    onRunSkillGapCheck = { viewModel.runSkillGapAnalysis() }
                                )
                            }
                            CopilotModule.STUDY_PLANNER -> {
                                StudyPlannerScreen(
                                    studyPlan = studyPlan,
                                    isGenerating = isStudyPlanGenerating,
                                    onGeneratePlan = { weeks -> viewModel.generateStudyPlan(weeks) },
                                    onToggleWeekCompletion = { weekNum -> viewModel.toggleWeekCompletion(weekNum) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

