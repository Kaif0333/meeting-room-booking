# Meeting Room Booking System

## Overview
Full-stack project (Spring Boot backend + React frontend) for booking meeting rooms.

## Structure
- `backend/` - Spring Boot app (Java 17, Spring Data MongoDB)
- `frontend/` - React app (create-react-app)
- `docs/` - Report, PPTX, PDF

## Run locally

### Backend
1. `cd backend`
2. Start MongoDB (`mongod`)
3. `./mvnw spring-boot:run`  (or `mvn spring-boot:run`)

### Frontend
1. `cd frontend`
2. `npm install`
3. `npm start`

## Notes
- Ensure `application.properties` is not committed with credentials.
- Add environment variables for MongoDB URI when deploying.

