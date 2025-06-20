# SpringResto - Restaurant Event Management App

SpringResto is a full-stack restaurant event management web application built with **Spring Boot**, **Thymeleaf**, **MySQL**, and **Docker**.

---

## 🚀 Features

- User login/logout
- Create reservations
- Create and edit events
- Add seating times with duration
- Assign menus and layouts to events
- View seatings per day
- Archive past events
- Filter events by date (All, Before, After, Between)
- Responsive interface using Bootstrap

---

## 🐳 Running the Project with Docker (Recommended)

### ✅ Requirements

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- Java 17+ (if running without Docker)

### 🔧 Setup Steps

### 📦 Docker DB Settings

| Property             | Value         |
|----------------------|---------------|
| Database Name        | springresto |
| MySQL Username       | devJPA      |
| MySQL Password       | NBCC1234!   |
| MySQL Root Password  | root        |
| Port                 | 3306        |

```bash
# 1. Clone the repository
git clone https://github.com/nocrag/springresto.git
cd springresto

# 2. Run the SQL file
The file is located in src/main/resources/data.sql

# 2. Build the project
./mvnw clean package -DskipTests

# 3. Start the app with Docker
docker compose up --build
```

### 🔐 Database Configuration

The application uses a MySQL database. When using Docker, the configuration is handled in the
`docker-compose.yml` file. If running locally, make sure your `application.properties` matches
the database settings below.



