# Gawe-an — Job Finder Android Application

## 📌 Competition Information
**Competition:** LKS Provinsi Jawa Timur 2025  
**Category:** IT Software Solutions for Business  
**Phase:** Phase 1 – Job Seeker Application  
**Project Name (Original):** Gawe-an  
**Developed by:** Android Application Developer (Participant)

---

## 📖 Project Overview
Gawe-an is an Android application designed to connect job seekers with companies offering job vacancies.  
This application focuses on the **job seeker side**, allowing users to browse available jobs, save vacancies, apply for jobs, and track their application status efficiently.

The project was developed as part of **LKS Provinsi Jawa Timur 2025**, following the provided business case and technical constraints.

---

## 🎯 Project Scope (Phase 1)
This phase focuses **only on job seekers**, including:
- User authentication (Login & Register)
- Job browsing and searching
- Job application process
- Saved jobs and application tracking
- User profile display

---

## 📱 Application Features

### 🔐 Login Screen
- Job seekers can log in using their credentials
- Only job seekers are allowed to access the system
- Registered users can log in immediately

### 📝 Register Screen
- New job seekers can create an account
- Phone number accepts numeric input only
- Password and confirmation password must match
- Email address must be unique

### 📃 Job List Screen
- View available job vacancies
- Filter jobs by location
- Search jobs using keywords
- Save job vacancies for later
- Visual indicator for saved jobs
- Apply directly or view job details

### 📄 Job Detail Screen
- View company information and overview
- View job responsibilities
- View required qualifications
- View required soft skills
- Save job for later
- Share job vacancy via text:
  > “Hey! Checkout this job: [job_link]”
- Apply to a job (cannot apply to the same job twice)

### 📂 My Job Screen
- View saved jobs
- Remove saved jobs
- Apply or view job details
- View job application status
- Saved jobs are removed automatically if no longer available

### 👤 Profile Screen
- View job seeker profile information

---

## 🔌 Backend Integration
This application consumes a REST API provided by the backend team.

- **Swagger API Documentation:**  
  `http://localhost:5000/swagger`

- **Image Access:**  
  `http://localhost:5000/images/[imageName]`

---

## 🔗 Related Repository (Backend API)

This Android application requires a backend REST API to function properly.

Backend Repository:
👉 https://github.com/Finsa-SC/Job_Finder_API

Please follow the setup instructions in the backend repository before running this Android application.

---

## ⚠️ Technical Constraints
- No additional dependencies were added beyond the default Android project setup
- Application follows provided wireframe with additional UI improvements that do not affect the main flow
- Project was completed within the given time constraint (3 hours)

---

## 🧪 Dummy Account
Dummy user credentials are provided in the project file:
- **User Login.jpg**

---

## 🧑‍💻 Tech Stack
- Android Studio
- Kotlin
- RecyclerView
- REST API (HTTP)
- JSON Parsing

---

## 🎨 UI/UX Enhancements
In addition to the required features, several UI/UX improvements were implemented to enhance user experience without affecting the main application flow, including:
- Improved icons and visual indicators
- User-friendly notifications for important actions
- Minor layout and interaction refinements to reduce repetitive actions
- Consistent visual feedback for user interactions

All enhancements follow the original wireframe intent and do not introduce additional dependencies or alter the core business logic.

---

## 🚀 How to Run the Project

### 1️⃣ Run Backend API
Make sure the backend API is running before launching the Android app.

See backend setup instructions:
👉 https://github.com/Finsa-SC/Job_Finder_API

### 2️⃣ Run Android Application
- Open project in Android Studio
- Sync Gradle
- Run on emulator or physical device

---

## 📜 Credits
- Backend REST API provided by **ITSSB Indonesia Team**
- Case Study & Specification by **LKS Provinsi Jawa Timur 2025**
- Android Application developed for educational & competition purposes only

---

## ⚠️ Disclaimer
This project is created **solely for educational and competition purposes**.  
All backend services are provided externally and credited accordingly.
