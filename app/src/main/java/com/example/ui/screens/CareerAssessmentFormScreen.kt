package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CareerFormInput
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerAssessmentFormScreen(
    currentName: String,
    currentEmail: String,
    isSubmitting: Boolean,
    onSubmitAssessment: (CareerFormInput) -> Unit,
    onSkipToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    var userName by remember { mutableStateOf(currentName) }
    var userEmail by remember { mutableStateOf(currentEmail) }
    var targetPosition by remember { mutableStateOf("Senior Android Engineer") }
    var companyPreference by remember { mutableStateOf("Tier 1 Tech Company") }
    var experienceLevel by remember { mutableStateOf("Mid-Level (2-4 years)") }
    var currentSkills by remember { mutableStateOf("Kotlin, Jetpack Compose, React, REST APIs, Git") }
    var timelineWeeks by remember { mutableStateOf(4) }
    var careerGoals by remember { mutableStateOf("Master modern system design, lead high-concurrency app development, and prepare for technical interviews.") }

    val rolePresets = listOf(
        "Senior Android Engineer",
        "Full Stack Web Developer",
        "AI / ML Engineer",
        "Cloud DevOps Specialist",
        "Data Scientist",
        "Product Manager"
    )

    val expLevels = listOf(
        "Entry-Level / Graduate",
        "Mid-Level (2-4 years)",
        "Senior / Lead (5+ years)",
        "Career Transitioner"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Career Requirement Form", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onSkipToDashboard) {
                        Text("Skip to App", color = SecondaryCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = PrimaryIndigo,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tell Us What You Want To Achieve",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Fill out your target role details below. AI Career Copilot will automatically analyze your requirements, identify skill gaps, and generate a personalized step-by-step roadmap.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section 1: User Profile Details
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("1. Personal & Contact Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                        OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            label = { Text("Your Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = userEmail,
                            onValueChange = { userEmail = it },
                            label = { Text("Email Address") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Section 2: Target Position & Role
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("2. Which Position / Role Are You Looking For?", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                        OutlinedTextField(
                            value = targetPosition,
                            onValueChange = { targetPosition = it },
                            label = { Text("Target Position Title") },
                            placeholder = { Text("e.g. Senior Android Engineer") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text("Quick Role Suggestions:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            rolePresets.chunked(2).forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    row.forEach { preset ->
                                        FilterChip(
                                            selected = targetPosition == preset,
                                            onClick = { targetPosition = preset },
                                            label = { Text(preset, fontSize = 11.sp) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = companyPreference,
                            onValueChange = { companyPreference = it },
                            label = { Text("Target Company / Industry Preference") },
                            placeholder = { Text("e.g. Tier 1 Tech, FinTech Startup, Remote Global") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Section 3: Experience & Known Skills
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("3. Experience & Current Skills", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                        Text("Current Experience Level:", style = MaterialTheme.typography.labelMedium)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            expLevels.forEach { level ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    RadioButton(
                                        selected = experienceLevel == level,
                                        onClick = { experienceLevel = level }
                                    )
                                    Text(level, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = currentSkills,
                            onValueChange = { currentSkills = it },
                            label = { Text("Skills You Currently Know (Comma Separated)") },
                            placeholder = { Text("e.g. Kotlin, React, Python, SQL, Git") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Section 4: Timeline & Specific Goals
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("4. Target Timeline & Objectives", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                        Text("Preferred Learning Roadmap Duration:", style = MaterialTheme.typography.labelMedium)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(2, 4, 6, 8).forEach { weeks ->
                                FilterChip(
                                    selected = timelineWeeks == weeks,
                                    onClick = { timelineWeeks = weeks },
                                    label = { Text("$weeks Weeks") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = careerGoals,
                            onValueChange = { careerGoals = it },
                            label = { Text("Specific Career Goals / Target Milestones") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        val input = CareerFormInput(
                            userName = userName,
                            userEmail = userEmail,
                            targetPosition = targetPosition,
                            companyPreference = companyPreference,
                            experienceLevel = experienceLevel,
                            currentSkills = currentSkills,
                            timelineWeeks = timelineWeeks,
                            careerGoals = careerGoals
                        )
                        onSubmitAssessment(input)
                    },
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analyzing Requirements & Generating Roadmap...", fontSize = 14.sp)
                    } else {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Tailored Roadmap & Skill Report", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
