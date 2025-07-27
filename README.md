Nova portal-smart job management system

Project Overview
Smart Job Portal is a full-stack web application that allows users to register, login, browse job postings, apply for jobs, and track their application status. 
Admin users can post jobs, view applicants, and update application statuses. The project demonstrates a complete job management workflow with authentication 
and authorization.

---
Technology Stack
- **Backend:** Java, Spring Boot, Spring Security, JWT (JSON Web Token)
- **Database:** MySQL
- **Frontend:** HTML, CSS, JavaScript, Bootstrap (served separately)
- **API Testing:** Postman

---

Features
- User registration and login with JWT-based authentication
- Role-based access control (User and Admin roles)
- CRUD operations on jobs (Admin)
- Users can view jobs and apply
- Users can track application status
- Admin can update application statuses (Selected/Rejected)

---

Getting Started

 Prerequisites
- Java 17 or above
- Maven
- MySQL Server
- Postman (for API testing)
- Frontend files (served via local server or live server extension)

Setup Backend
1. Clone the repository.
2. Create MySQL database named `jobportal`.
3. Run SQL script to create tables and remove duplicate users (if any).
4. Update `application.properties` with your MySQL credentials.
5. Build and run Spring Boot application:
   ```bash
   mvn clean install
   mvn spring-boot:run


Backend runs on http://localhost:8080.

   
