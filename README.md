# Learning Management System

LMS is a lightweight backend service for managing educational courses, instructors, and student enrollments.
It provides support for course catalogs, category management, instructor profiles, and student registrations, offering a full CRUD workflow with a clear project structure.

# Core Features
    Course management
    Category and specialization management
    Instructor profiling
    Student enrollment system
    Course search and filtering
    RESTful API
    Validation and structured error handling
# Domain Model
### The system is built around the following core entities:
    User (Base for Student and Instructor)
    Course
    Category
    Instructor
    Student
    Specialization
    Enrollment
All entities in the future will support soft delete using a logical deletion field (deleted_at or is_deleted).

### Course Lifecycle
    Course is created by an Instructor and assigned to a Category
    Course is published and becomes available for search via API
    Students enroll in the course (Many-to-Many relationship)
    Course content can be updated, categories changed, or instructors reassigned
    Course can be archived or soft-deleted

# Architecture
LMS follows a layered architecture:

Initial implementation uses an InMemory storage. Here you can see the logical db scheme for the upcoming implementation, all tables will support soft-delete:
https://sonarcloud.io/summary/overall?id=dirijable_learning-management-system&branch=main