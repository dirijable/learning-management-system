# Learning Management System (LMS)
LMS is a lightweight backend service designed for managing educational courses, instructor profiles, and student enrollments. The system focuses on data integrity, role-based access control, and a clean, layered architecture.

### Core Features

    1. Course Management: Full CRUD workflow for courses, including associations with categories and instructors.
    2. Instructor Profiling: Management of expert profiles (bio, specialization) handled by the Administrator.
    3. Curriculum Structure: Courses are divided into multiple Lessons (One-to-Many relationship).
    4. Student Enrollment: Many-to-Many relationship between Users and Courses.
    5. Role-Based Access Control (RBAC): 
        ADMIN: Full control over courses, categories, and instructor data.
        USER: Browsing courses and self-enrollment.
    6. Soft Delete: Logical deletion mechanism for Categories to prevent accidental data loss.
    7. Transactional Integrity: Atomic operations ensuring database consistency (e.g., creating a course with an initial lesson).

### Domain Model
The system is built around 5 core entities:

    1. User: Handles authentication and authorization. Contains email, password, and role (ADMIN/USER).
    2. Course: The central entity. Linked to an Instructor, a Category, and contains a list of Lessons.
    3. Instructor: A specialized profile entity containing descriptive info about the teacher.
    4. Category: A lookup entity for course classification. Supports soft-delete logic.
    5. Lesson: A child entity of a Course, representing a single unit of study.

### Architecture

The project follows a Layered Architecture pattern:

    1. Web Layer: REST Controllers handling HTTP requests and DTO validation.
    2. Service Layer: Encapsulates business logic and manages transactions via @Transactional.
    3. Persistence Layer: Spring Data JPA repositories for relational database interaction.
    4. Security Layer: Spring Security with JWT (JSON Web Token) for stateless authentication.

### Tech Stack

    1. Java 17 / Spring Boot 3
    2. Spring Data JPA — ORM and Database interaction.
    3. Spring Security + JWT — Authentication and Authorization.
    4. PostgreSQL — Relational database storage.
    5. Lombok & MapStruct — Boilerplate reduction and DTO mapping.# Architecture

LMS follows a layered architecture:

Initial implementation uses an InMemory storage

# Sonar link 
https://sonarcloud.io/summary/overall?id=dirijable_learning-management-system&branch=main