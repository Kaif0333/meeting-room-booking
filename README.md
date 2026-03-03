# Meeting Room Booking System

## Overview
Full-stack project (Spring Boot backend + React frontend) for booking meeting rooms.

## Structure
- `backend/` - Spring Boot app (Java 17, Spring Data MongoDB)
- `frontend/` - React app (create-react-app)

## Prerequisites
- Java 17 JDK (not just JRE)
- Maven 3.9+ (or working Maven Wrapper)
- Node.js 18+ and npm
- MongoDB running locally (or a remote URI)

## Environment setup
### Backend environment
- `MONGODB_URI` (optional, default: `mongodb://localhost:27017/meetingdb`)
- `SERVER_PORT` (optional, default: `8080`)
- `APP_CORS_ALLOWED_ORIGIN` (optional, default: `http://localhost:3000`)

### Frontend environment
- Copy `frontend/.env.example` to `frontend/.env` if needed.
- `REACT_APP_API_BASE_URL` (optional, default: `http://localhost:8080/api`)

## Run locally
### 1) Start backend
```powershell
cd backend
mvn spring-boot:run
```
If Maven Wrapper works in your shell, you can use:
```powershell
.\mvnw.cmd spring-boot:run
```
If you downloaded a local JDK into `backend/.tools`, you can run:
```powershell
cd backend
.\run-backend.ps1
```

### 2) Start frontend
```powershell
cd frontend
npm install
npm start
```

### Run from project root
```powershell
.\run-backend.ps1
.\run-frontend.ps1
```
Or start both together:
```powershell
.\run-fullstack.ps1
```

## Verification
```powershell
cd frontend
npm test -- --watchAll=false
npm run build
```

