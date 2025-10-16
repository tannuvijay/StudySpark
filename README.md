# AI Study Assistant

This is a full-stack application that helps students study more effectively by leveraging Large Language Models (LLMs). Users can upload their notes (PDFs or text), and the platform will automatically generate summaries, quizzes, and flashcards.

## Tech Stack

- **Frontend:** React, Vite, Axios, React Router
- **Backend:** Java, Spring Boot, Spring Data MongoDB
- **Database:** MongoDB
- **AI/NLP:** Placeholder service for LLM integration (e.g., Gemini API, OpenAI)
- **Deployment:** Docker

## Features

- **File Upload:** Upload PDF files or paste text notes.
- **Auto-Summarize:** Generate concise summaries of the uploaded content.
- **Quiz Generation:** Automatically create Multiple Choice Questions (MCQs) based on the notes.
- **Flashcard Mode:** Generate and review flashcards for key terms and concepts.

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 18+
- Docker & Docker Compose
- MongoDB instance (can be run via Docker)

### Running the Application

1.  **Start Services:**
    From the root directory, run the `docker-compose.yml` file to start the MongoDB instance.
    ```bash
    docker-compose up -d
    ```

2.  **Run Backend:**
    Navigate to the `backend` directory and run the Spring Boot application.
    ```bash
    cd backend
    mvn spring-boot:run
    ```
    The backend will be available at `http://localhost:8080`.

3.  **Run Frontend:**
    Navigate to the `frontend` directory, install dependencies, and start the development server.
    ```bash
    cd frontend
    npm install
    npm run dev
    ```
    The frontend will be available at `http://localhost:5173`.