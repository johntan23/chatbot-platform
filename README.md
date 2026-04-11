# Chatbot Platform

A web-based conversational AI platform built with Java Spring Boot, React, and PostgreSQL.

## Features
- Real-time chat with AI (powered by Groq API)
- Conversation history
- Configurable system prompt and temperature
- Prompt injection protection

## Tech Stack
- **Backend:** Java 21, Spring Boot, PostgreSQL
- **Frontend:** React, Vite
- **AI:** Groq API (llama-3.3-70b-versatile)
- **Infrastructure:** Docker, Docker Compose

## Prerequisites
- Docker Desktop
- Groq API key (free at [console.groq.com](https://console.groq.com))

## Setup & Run

1. Clone the repository:

```bash
git clone https://github.com/johntan23/chatbot-platform.git
cd chatbot-platform
```

2. Create your `.env` file:

```bash
cp .env.example .env
```

3. Add your Groq API key in `.env`:

```
GROQ_API_KEY=your_groq_api_key_here
```

4. Run with Docker:

```bash
docker-compose up --build
```

5. Open your browser at:
- **Frontend:** http://localhost
- **Backend API:** http://localhost:8080

## Usage
- Type a message and press **Enter** or click **Send**
- Click **⚙️ Settings** to configure system prompt and temperature
- Previous conversations are shown in the left sidebar

## Project Structure

```
chatbot-platform/
├── src/                    # Spring Boot backend
│   └── main/java/com/tanidis/chatbot/
│       ├── controller/     # REST endpoints
│       ├── service/        # Business logic
│       ├── model/          # JPA entities
│       ├── repository/     # Database access
│       ├── dto/            # Data transfer objects
│       ├── config/         # Configuration
│       └── security/       # Prompt sanitizer
├── frontend/               # React frontend
│   └── src/
│       └── components/     # React components
├── docker-compose.yml      # Docker orchestration
├── Dockerfile.backend      # Backend Docker image
├── Dockerfile.frontend     # Frontend Docker image
└── nginx.conf              # Nginx configuration
```