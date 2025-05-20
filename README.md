# EBook Library Backend

This repository contains the backend server for the EBook Library platform. 
The backend provides REST APIs for managing users, books, file uploads, read status tracking, and sending push notifications.
It is implemented in Java using Spring Boot and designed to run with Docker and PostgreSQL.

---

## Project Purpose
The EBook Library project is a full-stack system built as a final student project to demonstrate advanced Java programming skills, Android development, and backend design using Spring Boot.

It enables users to browse, read, and manage PDF books, while admins can upload and delete books.
Authentication is handled via JWT tokens, and user-specific reading status is tracked. The system also uses internal JMS-based event handling.

This project showcases real-world implementation of:

- Android development
- JPA (Java Persistence API)
- Spring Boot and Spring Security
- JWT authentication
- JMS internal messaging with publish-subscribe logic

---

## Features

- **Book Management**
  - List all available books
  - Admins may upload new books (PDF + cover image) 
  - Admins may delete existing books (admin only)

- **User Management**
    - User registration and login
    - JWT-based authentication
    - Role-based access control (`USER` and `ADMIN` roles)

- **Reading Status**
    - Users can mark books as read/unread
    - Each user has a personal read/unread list

- **Security**
    - JWT access tokens and refresh tokens
    - Secure token verification
    - Passwords stored using crypted hashing

- **File Upload and Serving**
    - Books and cover images stored on the server
    - Files served securely via a dedicated endpoint

- **Notifications**
    - Firebase Cloud Messaging (FCM) support
    - Sends push notifications to users on new book uploads

- **Docker Support**
    - Easily deployable using Docker and Docker Compose
    - Automatic schema creation and default admin setup

---

## Technologies Used

- Java 23 & Spring Boot 3.4
- Spring Security, Spring Web, Spring Data JPA
- Hibernate
- PostgreSQL (database)
- Firebase Admin SDK (notifications)
- JWT (stateless authentication)
- JMS with Spring’s `@EventListener`
- Docker & Docker Compose

---

## How to Run

1. **Requirements**
    - Docker and Docker Compose installed on your system.

2. **Environment Configuration**
    - Create a `.env` file at the root with required settings like database credentials, JWT secret path, upload directory, and Firebase credentials.
    - Example:
      ```
      DB_URL=jdbc:postgresql://db:5432/ebook_library
      DB_USER=ebook_user
      DB_PASSWORD=your_db_password
 
      UPLOAD_DIR=/app/uploads
      UPLOAD_URL_PREFIX=http://10.0.2.2:8080/files/
 
      JWT_SECRET_PATH=/run/secrets/jwt-secret.key
      FIREBASE_CREDENTIALS_PATH=/run/secrets/firebase-adminsdk.json
 
      DEFAULT_ADMIN_EMAIL=adminUser@ebook.com
      DEFAULT_ADMIN_PASSWORD=adminUserPassword
      ```

3. **Secrets**
    - Create a `secrets/` folder (not included in version control) from the root directory of ebooklibrarybackend.
        - Place your JWT secret key inside: `secrets/jwt-secret.key`
        - Place your Firebase Admin SDK JSON inside: `secrets/firebase-adminsdk.json`

4. **Startup**
    - The project uses a `wait-for-db.sh` script to wait for PostgreSQL readiness before launching the Spring Boot app.
    - Once started, the app will:
        - Connect to PostgreSQL
        - Automatically create tables via JPA
        - Create a default admin user (email and password from `.env`)

5. **Access**
    - App is hosted on: `http://localhost:8080`
    - Files are served from: `http://localhost:8080/files/{filename}`

---

## Admin User

A default administrator account is created automatically on first startup (if it doesn't exist):

- **Email**: Defined via `DEFAULT_ADMIN_EMAIL` in `.env`
- **Password**: Defined via `DEFAULT_ADMIN_PASSWORD` in `.env`

This account is required for uploading or deleting books.

---

## API Summary

| Method | Endpoint             | Access         | Description                              |
|--------|----------------------|----------------|------------------------------------------|
| POST   | `/auth/signup`       | Public         | Register a new user                      |
| POST   | `/auth/login`        | Public         | Authenticate and receive JWT tokens      |
| POST   | `/auth/refresh`      | Public         | Refresh an access token                  |
| GET    | `/books`             | Public/User    | Get all available books                  |
| GET    | `/books/read`        | Authenticated  | Get books the user has marked as read    |
| GET    | `/books/unread`      | Authenticated  | Get books the user has not read          |
| POST   | `/books/{id}/read`   | Authenticated  | Mark a book as read                      |
| POST   | `/books/{id}/unread` | Authenticated  | Mark a book as unread                    |
| POST   | `/books/upload`      | Admin Only     | Upload a new book (PDF + image)          |
| DELETE | `/books/{id}`        | Admin Only     | Delete a book                            |
| GET    | `/files/{filename}`  | Public         | Serve a PDF or image file                |

---

## Firebase Setup (For Push Notifications)

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project with corresponding credentials and enable FCM
3. Generate a private key from “Service accounts”
4. Save your `your_generated_file.json` in the `secrets/` folder
5. Make sure `.env` contains:
  `FIREBASE_CREDENTIALS_PATH=/run/secrets/your_generated_file.json`

---

## File Handling

- Uploaded files (PDFs and cover images) are stored in `./uploads` on the host system.
- The backend serves files via `/files/{filename}` endpoint, so that they are accessed via:
  `http://10.0.2.2:8080/files/{filename}`

---

## Development Notes

- Spring Boot uses JPA to generate schema automatically at startup.
- Admin account is initialized by `AdminUserInitializer.java` class.
- Security is configured via `JwtAuthFilter`, `JwtUtils`, and `SecurityConfig`.

---

## License

This project is licensed under the **Apache License 2.0**.  
See [LICENSE](LICENSE) for the full terms.
