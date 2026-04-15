<<<<<<< HEAD
# end-to-end-ci-cd-jenkins-docker-javaapp
=======
# Islamic Quran and Azkar Web Application

A modern, clean, and professional web application focused on the Holy Quran and Azkar.

## Features

### Quran Section
- Display full Quran text (Arabic)
- Audio recitation (placeholder for Sheikh Yasser Al-Dosari)
- Audio player: Play / Pause, Navigate between Surahs, Highlight Ayah while playing
- Simple search (Surah name or Ayah)

### Reading Mode
- Clean UI for reading Quran
- Adjustable font size
- Dark / Light mode
- Smooth scrolling

### Azkar Section
- Morning Azkar
- Evening Azkar
- Organized lists
- Tasbeeh counter (simple counter)
- Mark as completed (in-memory only)

## Tech Stack
- Backend: Java with Spring Boot
- Frontend: HTML, CSS, JavaScript
- Data: Static JSON files (no database)

## Prerequisites
- Java 17 or higher
- Maven 3.6+

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run`
4. Open http://localhost:8080 in your browser

## Project Structure
- `src/main/java/com/islamicapp/` - Backend code
- `src/main/resources/static/` - Frontend files
- `src/main/resources/static/data/` - JSON data files

## API Endpoints
- GET /api/quran/surahs - Get all Surahs
- GET /api/quran/surah/{number} - Get specific Surah
- GET /api/azkar - Get all Azkar
- GET /api/azkar/{category} - Get Azkar by category
>>>>>>> 65b7d41 (initail commit)
