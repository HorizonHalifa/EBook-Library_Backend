# EBook Library Backend

This repository contains the backend server for the EBook Library platform. 
The backend provides REST APIs for managing users, books, file uploads, read status tracking, and sending push notifications.
It is implemented in Java using Spring Boot and designed to run with Docker and PostgreSQL.

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

- **Java 23**, **Spring Boot 3.4**
- Spring Security, Spring Web, Spring Data JPA
- PostgreSQL for data storage
- Hibernate (ORM)
- Firebase Admin SDK
- JWT for stateless authentication
- Docker + Docker Compose

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
      UPLOAD_URL_PREFIX=http://localhost:8080/files
 
      JWT_SECRET_PATH=/run/secrets/jwt-secret.key
      FIREBASE_CREDENTIALS_PATH=/run/secrets/firebase-adminsdk.json
 
      DEFAULT_ADMIN_EMAIL=adminUser@ebook.com
      DEFAULT_ADMIN_PASSWORD=adminUserPassword
      ```

3. **Secrets**
    - Create a `secrets/` folder (not included in version control).
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

## 🔗 API Summary

| Endpoint                    | Access        | Description                        |
|----------------------------|---------------|------------------------------------|
| `/auth/signup`             | Public        | Register a new user                |
| `/auth/login`              | Public        | Authenticate user, return tokens   |
| `/auth/refresh`            | Public        | Refresh access token               |
| `/books`                   | Public        | Get all books                      |
| `/books/read`              | Authenticated | User’s read books                  |
| `/books/unread`            | Authenticated | User’s unread books                |
| `/books/{id}/read`         | Authenticated | Mark book as read                  |
| `/books/{id}/unread`       | Authenticated | Mark book as unread                |
| `/books` (POST)            | Admin only    | Upload book (multipart/form-data)  |
| `/books/{id}` (DELETE)     | Admin only    | Delete a book                      |
| `/files/{filename}`        | Public        | Serve book PDF or cover image      |

---

## Firebase Setup (For Push Notifications)

- Create a Firebase project at: [console.firebase.google.com](https://console.firebase.google.com/)
- Go to “Service Accounts” → “Generate New Private Key”
- Save the JSON to `secrets/firebase-adminsdk.json`
- Reference it in `.env` as:  
  `FIREBASE_CREDENTIALS_PATH=/run/secrets/firebase-adminsdk.json`

---

## File Handling

- Uploaded files (PDFs and cover images) are stored in `./uploads` on the host.
- The backend serves files via `/files/{filename}` endpoint.

---

## Development Notes

- Spring Boot uses JPA to generate schema automatically at startup.
- Admin account is initialized by `AdminUserInitializer.java` class.
- Security is configured via `JwtAuthFilter`, `JwtUtils`, and `SecurityConfig`.

---

## License

This project is licensed under the **Apache License 2.0**.  
See [LICENSE](LICENSE) for the full terms.
