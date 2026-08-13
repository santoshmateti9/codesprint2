package com.example.data

import com.example.data.model.*

object SampleData {
    val sampleResumeText = """
        Alex Rivera
        San Francisco, CA | alex.rivera@email.com | linkedin.com/in/arivera
        
        SUMMARY
        Full Stack Software Engineer with 4+ years of experience designing and scaling modern Android and Web applications. Specialized in Kotlin, Jetpack Compose, React, TypeScript, Python, and RESTful microservices. Proven track record of improving app performance by 35% and accelerating release cycles.
        
        SKILLS
        Languages: Kotlin, Java, JavaScript, TypeScript, Python, SQL
        Mobile & Web: Jetpack Compose, Android SDK, React, HTML5/CSS3, Tailwind CSS
        Backend & Cloud: Node.js, FastAPI, Room Database, PostgreSQL, Firebase, REST APIs
        Tools & Practices: Git, Docker, CI/CD, Agile/Scrum, Unit Testing (JUnit, Robolectric)
        
        EXPERIENCE
        Mobile Application Engineer | NexaTech Solutions | 2022 – Present
        - Architected modern Android application using Jetpack Compose and MVVM clean architecture, increasing user retention by 28%.
        - Integrated Room database with offline sync capabilities, reducing network payload size by 40%.
        - Engineered background sync services using Kotlin Coroutines and WorkManager.
        - Mentored 3 junior developers and conducted weekly code reviews to enforce software quality standards.
        
        Software Developer | CloudWave Inc | 2020 – 2022
        - Built REST APIs in Python (FastAPI) and connected to PostgreSQL database for high-throughput reporting engine.
        - Developed key features for cross-platform web client using React and Redux Toolkit.
        - Reduced API response times by 45% by implementing Redis caching layer and optimizing SQL queries.
        
        EDUCATION
        B.S. in Computer Science | University of California, Berkeley | 2020
    """.trimIndent()

    val sampleJobDescription = """
        Job Title: Senior Full Stack / Mobile Engineer
        Company: TechCorp Solutions
        
        About the Role:
        TechCorp Solutions is seeking a Senior Engineer to lead the development of high-performance mobile and web products. You will build cutting-edge features using Kotlin, Jetpack Compose, React, and Python, while shaping scalable architecture and CI/CD pipelines.
        
        Key Responsibilities:
        - Design and implement scalable Android applications using Jetpack Compose, Coroutines, and Room DB.
        - Build robust backend microservices with Python (FastAPI) or Node.js.
        - Optimize application performance, memory utilization, and network operations.
        - Collaborate with UI/UX designers and product managers to define technical roadmaps.
        - Drive DevOps practices including Docker containerization, Kubernetes orchestration, and Automated CI/CD.
        
        Requirements:
        - 4+ years of professional experience in Kotlin / Android development.
        - Strong mastery of Jetpack Compose, Coroutines, StateFlow, and Room.
        - Proficiency in Web technologies (React, TypeScript, Tailwind) and Backend APIs (FastAPI/Python or Node.js).
        - Demonstrated experience with System Architecture, Docker, CI/CD pipelines, and Cloud deployment (AWS/GCP).
        - Passion for technical leadership, code craftsmanship, and mentorship.
    """.trimIndent()

    val sampleAnalysisResult = AnalysisResult(
        matchScore = 88,
        atsScore = 92,
        matchingSkills = listOf(
            "Kotlin", "Jetpack Compose", "Coroutines", "Room Database",
            "Python", "FastAPI", "React", "TypeScript", "REST APIs", "Git", "SQL"
        ),
        missingSkills = listOf(
            "Docker", "Kubernetes", "CI/CD Pipelines", "System Architecture", "AWS / Cloud Infrastructure"
        ),
        improvedBullets = listOf(
            BulletComparison(
                original = "Architected modern Android application using Jetpack Compose and MVVM clean architecture, increasing user retention by 28%.",
                improved = "Architected scalable Android app using Kotlin, Jetpack Compose, and Clean Architecture (MVVM), driving a 28% increase in 90-day user retention across 150K+ DAU.",
                reasoning = "Added specific tech keywords (Kotlin, Clean Architecture) and quantifiable user metrics (150K+ DAU, 90-day retention) for strong ATS matching."
            ),
            BulletComparison(
                original = "Integrated Room database with offline sync capabilities, reducing network payload size by 40%.",
                improved = "Engineered resilient offline-first data layer using Room DB and Kotlin Flow, cutting network data consumption by 40% and eliminating 99.4% of offline crash reports.",
                reasoning = "Highlighted offline-first pattern, Kotlin Flow reactive streams, and zero-crash reliability metric."
            ),
            BulletComparison(
                original = "Built REST APIs in Python (FastAPI) and connected to PostgreSQL database for high-throughput reporting engine.",
                improved = "Designed high-concurrency RESTful backend microservices using Python (FastAPI) and PostgreSQL, serving 2M+ daily requests with sub-50ms latency.",
                reasoning = "Emphasized microservices architecture, throughput metrics (2M+ daily requests), and SLA latency performance."
            )
        ),
        atsIssues = listOf(
            AtsIssue(
                category = "Keywords",
                issue = "Missing explicit DevOps & Cloud terms mentioned in JD (Docker, CI/CD, AWS).",
                recommendation = "Add a dedicated DevOps/Cloud section under Skills or mention containerization projects.",
                severity = "High"
            ),
            AtsIssue(
                category = "Impact",
                issue = "System Architecture achievements could highlight design decisions more prominently.",
                recommendation = "Use action verbs like 'Architected', 'Spearheaded', and 'Standardized' with concrete system metrics.",
                severity = "Medium"
            ),
            AtsIssue(
                category = "Formatting",
                issue = "Dates and locations are clear, standard bullet formatting detected.",
                recommendation = "Maintain current clean single-column format for optimal ATS parsing.",
                severity = "Low"
            )
        ),
        rewrittenResumeText = """
            ALEX RIVERA
            San Francisco, CA | alex.rivera@email.com | linkedin.com/in/arivera
            
            EXECUTIVE SUMMARY
            Senior Full Stack & Mobile Engineer with 4+ years of expertise architecting high-performance Android applications (Kotlin, Jetpack Compose) and backend microservices (Python FastAPI, PostgreSQL, REST APIs). Track record of scaling mobile apps to 150K+ DAU, reducing latency by 45%, and leading cross-functional developer teams.
            
            CORE COMPETENCIES
            Mobile Engineering: Kotlin, Jetpack Compose, Android SDK, Coroutines, StateFlow, Room DB, Clean Architecture
            Web & Frontend: React.js, TypeScript, Tailwind CSS, Redux Toolkit, State Management
            Backend & Cloud: Python (FastAPI), Node.js, PostgreSQL, Redis, RESTful Microservices, Docker, CI/CD
            Practices: Agile/Scrum, System Architecture, Unit/UI Testing (Robolectric, JUnit), Technical Mentorship
            
            PROFESSIONAL EXPERIENCE
            Senior Mobile Application Engineer | NexaTech Solutions | 2022 – Present
            • Architected modular Android app using Kotlin, Jetpack Compose, and Clean Architecture (MVVM), driving a 28% increase in 90-day retention across 150K+ active users.
            • Engineered resilient offline-first data layer with Room DB and Kotlin Flow, reducing network data payload by 40% and cutting offline crashes to near-zero.
            • Implemented high-performance background processing with WorkManager & Coroutines for instant data sync.
            • Mentored junior engineers and standardized CI/CD code quality pipelines, increasing release velocity by 30%.
            
            Full Stack Software Engineer | CloudWave Inc | 2020 – 2022
            • Designed high-concurrency RESTful backend microservices in Python (FastAPI) and PostgreSQL, serving 2M+ daily requests.
            • Optimized database query execution and implemented Redis cache layer, reducing API latency by 45% (sub-50ms response times).
            • Built responsive client dashboard in React, TypeScript, and Tailwind CSS.
            
            EDUCATION
            B.S. in Computer Science | University of California, Berkeley
        """.trimIndent()
    )

    val sampleInitialInterview = InterviewSession(
        jobTitle = "Senior Full Stack / Mobile Engineer at TechCorp Solutions",
        chatHistory = listOf(
            ChatMessage(
                sender = "interviewer",
                text = "Hello Alex! Welcome to your technical interview for the Senior Full Stack & Mobile Engineer position at TechCorp Solutions. I reviewed your background in Kotlin, Jetpack Compose, and FastAPI microservices. To kick things off, could you walk me through a challenging architectural decision you made when building your offline-first Android data layer with Room and Kotlin Flow, and how you ensured state consistency?"
            )
        ),
        lastEvaluation = null,
        avgScore = 0.0,
        isComplete = false
    )

    val sampleSkillGapReport = SkillGapReport(
        currentSkills = listOf("Kotlin", "Jetpack Compose", "Coroutines", "Room DB", "Python", "FastAPI", "React", "TypeScript", "REST APIs", "Git", "SQL"),
        requiredSkills = listOf("Kotlin", "Jetpack Compose", "Room DB", "FastAPI", "Docker", "Kubernetes", "CI/CD Pipelines", "System Architecture", "AWS Cloud"),
        gaps = listOf(
            SkillGapItem(
                skillName = "Docker & Containerization",
                category = "DevOps & Deployment",
                priority = "HIGH",
                currentLevel = "Basic",
                requiredLevel = "Advanced",
                impactExplanation = "The job description specifically calls for containerizing microservices and managing multi-container production environments."
            ),
            SkillGapItem(
                skillName = "CI/CD Automation Pipelines",
                category = "DevOps & Engineering Practices",
                priority = "HIGH",
                currentLevel = "Basic",
                requiredLevel = "Intermediate",
                impactExplanation = "TechCorp automates mobile APK builds and backend deployments using GitHub Actions / GitLab CI."
            ),
            SkillGapItem(
                skillName = "System Architecture & High-Scale Design",
                category = "Architecture",
                priority = "MEDIUM",
                currentLevel = "Intermediate",
                requiredLevel = "Advanced",
                impactExplanation = "Senior role requires leading architectural discussions, designing distributed caches, and microservice communication patterns."
            ),
            SkillGapItem(
                skillName = "AWS / Cloud Infrastructure",
                category = "Cloud & Infrastructure",
                priority = "LOW",
                currentLevel = "None",
                requiredLevel = "Intermediate",
                impactExplanation = "Cloud deployment experience will strengthen backend leadership for microservices deployed on AWS ECS/EKS."
            )
        ),
        keyConceptsToMaster = listOf(
            "Docker multi-stage builds & docker-compose for FastAPI + Postgres",
            "GitHub Actions workflows for automated Android APK build & unit test runs",
            "Microservices caching strategies (Redis write-through vs read-aside)",
            "System design for real-time mobile push notifications and data sync",
            "Database indexing and query execution plan optimization"
        )
    )

    val sampleStudyPlan = StudyPlan(
        durationWeeks = 4,
        weeks = listOf(
            WeekPlan(
                weekNumber = 1,
                title = "Docker & Microservices Containerization",
                focusSkills = listOf("Docker", "Dockerfile", "Docker Compose", "FastAPI Packaging"),
                topics = listOf(
                    "Container fundamentals & Docker engine architecture",
                    "Writing multi-stage Dockerfiles for Python FastAPI and React apps",
                    "Orchestrating local dev environment with Docker Compose (FastAPI + PostgreSQL + Redis)",
                    "Volume persistence, network networking, and environment secret injection"
                ),
                actionItems = listOf(
                    "Containerize existing FastAPI backend project with multi-stage Dockerfile",
                    "Set up docker-compose.yml linking FastAPI, PostgreSQL, and Redis cache",
                    "Verify sub-second container startup and zero-leak environment variables"
                ),
                isCompleted = true
            ),
            WeekPlan(
                weekNumber = 2,
                title = "Automated CI/CD Pipelines for Mobile & Web",
                focusSkills = listOf("GitHub Actions", "Fastlane", "Gradle CI", "Automated Testing"),
                topics = listOf(
                    "GitHub Actions syntax, triggers, matrix builds, and caching Gradle dependencies",
                    "Automating Robolectric and JUnit unit test suites on every pull request",
                    "Building signed APK/AAB outputs and artifact uploading",
                    "Automating backend Docker image builds and pushing to container registry"
                ),
                actionItems = listOf(
                    "Create .github/workflows/android.yml to compile applet and run unit tests on push",
                    "Add automated linting and code formatting checks to CI pipeline",
                    "Configure pipeline secrets for secure automated builds"
                ),
                isCompleted = false
            ),
            WeekPlan(
                weekNumber = 3,
                title = "High-Scale System Architecture & Microservice Patterns",
                focusSkills = listOf("System Design", "Distributed Caching", "API Gateway", "Message Queues"),
                topics = listOf(
                    "High-availability architecture: Load balancers, API gateways, and rate limiters",
                    "Database sharding, read replicas, and query execution optimization in Postgres",
                    "Asynchronous event processing using Celery / RabbitMQ / Kafka",
                    "Mobile sync protocols: WebSockets vs SSE vs Optimistic UI with Room"
                ),
                actionItems = listOf(
                    "Sketch end-to-end architecture diagram for high-throughput mobile application",
                    "Implement Redis caching decorator in FastAPI for instant response delivery",
                    "Benchmark backend under simulated 1,000 req/sec load with Locust"
                ),
                isCompleted = false
            ),
            WeekPlan(
                weekNumber = 4,
                title = "Cloud Deployment & Technical Interview Mastery",
                focusSkills = listOf("AWS ECS / Cloud Run", "System Design Mock Interviews", "Behavioral Leadership"),
                topics = listOf(
                    "Deploying containerized services to AWS ECS / Cloud Run with custom domain SSL",
                    "Mock system design interviews focusing on mobile-first scalable backends",
                    "STAR method behavioral answers for senior engineering leadership scenarios",
                    "Final resume bullet tuning & live portfolio presentation"
                ),
                actionItems = listOf(
                    "Deploy sample FastAPI microservice to cloud hosting platform",
                    "Complete 3 AI Interview Assistant chatbot mock sessions achieving >90% average score",
                    "Export polished ATS-optimized resume PDF for application submission"
                ),
                isCompleted = false
            )
        )
    )
}
