# SpringResto - Restaurant Event Management App

SpringResto is a full-stack restaurant event management web application built with **Spring Boot**, **Thymeleaf**, **MySQL**, and **Docker**.

---

## 🚀 Features

- User login/logout
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

```bash
# 1. Clone the repository
git clone https://github.com/YOUR-USERNAME/springresto.git
cd springresto

# 2. Build the project
./mvnw clean package -DskipTests

# 3. Start the app with Docker
docker compose up --build

### 🔐 Database Configuration

The application uses a MySQL database. When using Docker, the configuration is handled in the `docker-compose.yml` file. If running locally, make sure your `application.properties` matches the database settings below.

#### 📦 Docker DB Settings

| Property             | Value         |
|----------------------|---------------|
| Database Name        | `springresto` |
| MySQL Username       | `devJPA`      |
| MySQL Password       | `NBCC1234!`   |
| MySQL Root Password  | `root`        |
| Port                 | `3306`        |

#### ⚙️ `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springresto
spring.datasource.username=devJPA
spring.datasource.password=NBCC1234!
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.thymeleaf.cache=false
