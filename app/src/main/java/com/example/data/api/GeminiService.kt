package com.example.data.api

import com.example.BuildConfig
import com.example.data.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private fun getApiKey(): String {
        val key = BuildConfig.GEMINI_API_KEY
        return if (key.isNullOrBlank() || key == "MY_GEMINI_API_KEY") "" else key
    }

    suspend fun analyzeResumeAndAts(resumeText: String, jobDescription: String): AnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty()) {
            // Fallback or demo fallback if API key is not yet set in Secrets
            return@withContext com.example.data.SampleData.sampleAnalysisResult
        }

        val prompt = """
            You are an expert AI Resume Builder, ATS Evaluator, and Senior Tech Recruiter.
            Analyze candidate resume against target job description.
            
            RESUME:
            $resumeText
            
            JOB DESCRIPTION:
            $jobDescription
            
            Return a strict JSON object matching this schema:
            {
              "matchScore": integer (0-100),
              "atsScore": integer (0-100),
              "matchingSkills": [string],
              "missingSkills": [string],
              "improvedBullets": [
                {
                  "original": string,
                  "improved": string,
                  "reasoning": string
                }
              ],
              "atsIssues": [
                {
                  "category": string (Keywords|Formatting|Impact|Structure),
                  "issue": string,
                  "recommendation": string,
                  "severity": string (High|Medium|Low)
                }
              ],
              "rewrittenResumeText": string
            }
        """.trimIndent()

        val jsonResponse = callGeminiApi(apiKey, prompt) ?: return@withContext com.example.data.SampleData.sampleAnalysisResult
        try {
            val json = JSONObject(jsonResponse)
            val matchScore = json.optInt("matchScore", 85)
            val atsScore = json.optInt("atsScore", 88)
            
            val matchingSkills = parseStringArray(json.optJSONArray("matchingSkills"))
            val missingSkills = parseStringArray(json.optJSONArray("missingSkills"))
            
            val bullets = mutableListOf<BulletComparison>()
            json.optJSONArray("improvedBullets")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    bullets.add(
                        BulletComparison(
                            original = obj.optString("original", ""),
                            improved = obj.optString("improved", ""),
                            reasoning = obj.optString("reasoning", "")
                        )
                    )
                }
            }
            
            val issues = mutableListOf<AtsIssue>()
            json.optJSONArray("atsIssues")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    issues.add(
                        AtsIssue(
                            category = obj.optString("category", "General"),
                            issue = obj.optString("issue", ""),
                            recommendation = obj.optString("recommendation", ""),
                            severity = obj.optString("severity", "Medium")
                        )
                    )
                }
            }
            
            val rewritten = json.optString("rewrittenResumeText", "")

            AnalysisResult(
                matchScore = matchScore,
                atsScore = atsScore,
                matchingSkills = if (matchingSkills.isNotEmpty()) matchingSkills else listOf("Kotlin", "Android"),
                missingSkills = missingSkills,
                improvedBullets = bullets,
                atsIssues = issues,
                rewrittenResumeText = rewritten
            )
        } catch (e: Exception) {
            e.printStackTrace()
            com.example.data.SampleData.sampleAnalysisResult
        }
    }

    suspend fun startInterview(resumeText: String, jobTarget: JobTargetData): InterviewSession = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty()) {
            return@withContext com.example.data.SampleData.sampleInitialInterview
        }

        val prompt = """
            You are a Senior Tech Hiring Manager conducting an interactive technical interview for the role of ${jobTarget.title} at ${jobTarget.company}.
            
            CANDIDATE RESUME:
            $resumeText
            
            JOB DESCRIPTION:
            ${jobTarget.description}
            
            Generate a warm professional opening greeting introducing yourself and asking the first specific technical or situational question customized to candidate's background and JD.
            Return strict JSON:
            {
              "greetingAndFirstQuestion": string
            }
        """.trimIndent()

        val jsonResponse = callGeminiApi(apiKey, prompt)
        val firstMessage = if (jsonResponse != null) {
            try {
                JSONObject(jsonResponse).optString("greetingAndFirstQuestion", "")
            } catch (e: Exception) { "" }
        } else ""

        val text = if (firstMessage.isNotBlank()) firstMessage else com.example.data.SampleData.sampleInitialInterview.chatHistory.first().text

        InterviewSession(
            jobTitle = "${jobTarget.title} at ${jobTarget.company}",
            chatHistory = listOf(
                ChatMessage(
                    sender = "interviewer",
                    text = text
                )
            )
        )
    }

    suspend fun processInterviewChatTurn(
        resumeText: String,
        jobTarget: JobTargetData,
        chatHistory: List<ChatMessage>,
        userMessage: String
    ): Triple<ChatMessage, TurnEvaluation, Boolean> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty()) {
            // Simulated evaluation turn
            val eval = TurnEvaluation(
                score = (80..95).random(),
                strengths = "Clear technical vocabulary, highlighted key metrics, and referenced architectural trade-offs.",
                improvement = "Consider elaborating on edge cases and automated unit test coverage."
            )
            val reply = ChatMessage(
                sender = "interviewer",
                text = "Great response! You clearly understand state consistency and offline-first patterns. Moving on: How do you handle database migration strategies in Room when updating schemas across active user devices?"
            )
            return@withContext Triple(reply, eval, false)
        }

        val historyText = chatHistory.joinToString("\n") { "${it.sender.uppercase()}: ${it.text}" }

        val prompt = """
            You are a Senior Technical Interviewer evaluating candidate answer in real-time.
            ROLE: ${jobTarget.title} at ${jobTarget.company}
            RESUME: $resumeText
            JOB DESCRIPTION: ${jobTarget.description}
            
            CONVERSATION HISTORY:
            $historyText
            
            LATEST CANDIDATE ANSWER:
            $userMessage
            
            Evaluate the latest candidate answer and craft the next interviewer response.
            Return strict JSON:
            {
              "score": integer (0-100),
              "strengths": string,
              "improvement": string,
              "nextQuestion": string,
              "isComplete": boolean
            }
        """.trimIndent()

        val jsonResponse = callGeminiApi(apiKey, prompt)
        if (jsonResponse != null) {
            try {
                val json = JSONObject(jsonResponse)
                val eval = TurnEvaluation(
                    score = json.optInt("score", 85),
                    strengths = json.optString("strengths", "Good technical detail."),
                    improvement = json.optString("improvement", "Provide concrete metrics.")
                )
                val nextQ = json.optString("nextQuestion", "Thank you! Can you elaborate further on system architecture?")
                val isComplete = json.optBoolean("isComplete", false)
                val replyMsg = ChatMessage(sender = "interviewer", text = nextQ)
                return@withContext Triple(replyMsg, eval, isComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val eval = TurnEvaluation(
            score = 85,
            strengths = "Solid technical response with direct domain focus.",
            improvement = "Elaborate slightly more on error handling."
        )
        val reply = ChatMessage(sender = "interviewer", text = "Thank you. How do you approach designing fault-tolerant asynchronous pipelines?")
        Triple(reply, eval, false)
    }

    suspend fun analyzeSkillGaps(resumeText: String, jobDescription: String): SkillGapReport = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty()) {
            return@withContext com.example.data.SampleData.sampleSkillGapReport
        }

        val prompt = """
            You are a Technical Skill Gap Analyst.
            Compare RESUME against JOB DESCRIPTION and identify skill gaps with priority levels.
            
            RESUME:
            $resumeText
            
            JOB DESCRIPTION:
            $jobDescription
            
            Return strict JSON schema:
            {
              "currentSkills": [string],
              "requiredSkills": [string],
              "gaps": [
                {
                  "skillName": string,
                  "category": string,
                  "priority": string (HIGH|MEDIUM|LOW),
                  "currentLevel": string (None|Basic|Intermediate),
                  "requiredLevel": string (Intermediate|Advanced|Expert),
                  "impactExplanation": string
                }
              ],
              "keyConceptsToMaster": [string]
            }
        """.trimIndent()

        val jsonResponse = callGeminiApi(apiKey, prompt) ?: return@withContext com.example.data.SampleData.sampleSkillGapReport
        try {
            val json = JSONObject(jsonResponse)
            val currentSkills = parseStringArray(json.optJSONArray("currentSkills"))
            val requiredSkills = parseStringArray(json.optJSONArray("requiredSkills"))
            val concepts = parseStringArray(json.optJSONArray("keyConceptsToMaster"))

            val gaps = mutableListOf<SkillGapItem>()
            json.optJSONArray("gaps")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    gaps.add(
                        SkillGapItem(
                            skillName = obj.optString("skillName", "Skill"),
                            category = obj.optString("category", "General"),
                            priority = obj.optString("priority", "MEDIUM"),
                            currentLevel = obj.optString("currentLevel", "Basic"),
                            requiredLevel = obj.optString("requiredLevel", "Advanced"),
                            impactExplanation = obj.optString("impactExplanation", "")
                        )
                    )
                }
            }

            SkillGapReport(
                currentSkills = if (currentSkills.isNotEmpty()) currentSkills else com.example.data.SampleData.sampleSkillGapReport.currentSkills,
                requiredSkills = if (requiredSkills.isNotEmpty()) requiredSkills else com.example.data.SampleData.sampleSkillGapReport.requiredSkills,
                gaps = if (gaps.isNotEmpty()) gaps else com.example.data.SampleData.sampleSkillGapReport.gaps,
                keyConceptsToMaster = if (concepts.isNotEmpty()) concepts else com.example.data.SampleData.sampleSkillGapReport.keyConceptsToMaster
            )
        } catch (e: Exception) {
            e.printStackTrace()
            com.example.data.SampleData.sampleSkillGapReport
        }
    }

    suspend fun generateStudyPlan(skillGapReport: SkillGapReport, weeksCount: Int = 4): StudyPlan = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty()) {
            return@withContext com.example.data.SampleData.sampleStudyPlan
        }

        val gapSummary = skillGapReport.gaps.joinToString("\n") { "- ${it.skillName} (${it.priority}): ${it.impactExplanation}" }

        val prompt = """
            Create a structured week-by-week ($weeksCount weeks) personalized career study & learning plan based on skill gaps:
            $gapSummary
            
            Return strict JSON schema:
            {
              "weeks": [
                {
                  "weekNumber": integer,
                  "title": string,
                  "focusSkills": [string],
                  "topics": [string],
                  "actionItems": [string]
                }
              ]
            }
        """.trimIndent()

        val jsonResponse = callGeminiApi(apiKey, prompt) ?: return@withContext com.example.data.SampleData.sampleStudyPlan
        try {
            val json = JSONObject(jsonResponse)
            val weeksList = mutableListOf<WeekPlan>()
            json.optJSONArray("weeks")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    weeksList.add(
                        WeekPlan(
                            weekNumber = obj.optInt("weekNumber", i + 1),
                            title = obj.optString("title", "Week ${i + 1}"),
                            focusSkills = parseStringArray(obj.optJSONArray("focusSkills")),
                            topics = parseStringArray(obj.optJSONArray("topics")),
                            actionItems = parseStringArray(obj.optJSONArray("actionItems")),
                            isCompleted = i == 0 // First week unlocked
                        )
                    )
                }
            }
            StudyPlan(
                durationWeeks = weeksCount,
                weeks = if (weeksList.isNotEmpty()) weeksList else com.example.data.SampleData.sampleStudyPlan.weeks
            )
        } catch (e: Exception) {
            e.printStackTrace()
            com.example.data.SampleData.sampleStudyPlan
        }
    }

    private fun callGeminiApi(apiKey: String, prompt: String): String? {
        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.4)
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBodyStr = response.body?.string() ?: return null

            val resJson = JSONObject(responseBodyStr)
            val candidates = resJson.optJSONArray("candidates") ?: return null
            if (candidates.length() == 0) return null
            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content") ?: return null
            val parts = content.optJSONArray("parts") ?: return null
            if (parts.length() == 0) return null

            return parts.getJSONObject(0).optString("text")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun parseStringArray(jsonArray: JSONArray?): List<String> {
        if (jsonArray == null) return emptyList()
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.optString(i))
        }
        return list
    }
}
