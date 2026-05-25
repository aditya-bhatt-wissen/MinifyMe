# Spring Boot URL Shortener

A modern, efficient URL shortening service built with Spring Boot, PostgreSQL, and Thymeleaf. This application allows users to create short, memorable links from long URLs with customizable expiration dates.

## Features

- **URL Shortening**: Convert long URLs into short, shareable links
- **Custom Base URL**: Configurable base URL for shortened links
- **Expiration Management**: Set custom expiration dates for shortened URLs
- **URL Validation**: Optional validation of original URLs before shortening
- **Web Interface**: User-friendly Thymeleaf-based UI for creating and managing short URLs
- **Database Persistence**: PostgreSQL database for reliable data storage
- **RESTful API**: API endpoints for programmatic URL shortening
- **User Management**: Role-based access control for users

## Technology Stack

- **Framework**: Spring Boot 3.4.4
- **Java Version**: Java 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Template Engine**: Thymeleaf with Layout Dialect
- **Web**: Spring Boot Web Starter
- **Validation**: Spring Validation
- **Build Tool**: Maven
- **Containerization**: Docker Compose support

## Prerequisites

- Java 21 or higher
- PostgreSQL 12 or higher
- Maven 3.9.x or higher
- Docker and Docker Compose (optional, for containerized setup)

## Project Structure

```
src/
├── main/
│   ├── java/com/sivalabs/urlshortener/
│   │   ├── SpringBootUrlShortenerApplication.java  # Entry point
│   │   ├── ApplicationProperties.java               # Configuration properties
│   │   ├── domain/
│   │   │   ├── entities/                            # JPA entities
│   │   │   ├── models/                              # Domain models and DTOs
│   │   │   ├── repositories/                        # Data access layer
│   │   │   └── services/                            # Business logic
│   │   └── web/
│   │       ├── controllers/                         # REST/Web controllers
│   │       └── dtos/                                # Data transfer objects
│   └── resources/
│       ├── application.properties                   # Configuration
│       ├── db/migration/                            # Flyway migrations
│       ├── static/                                  # CSS and static assets
│       └── templates/                               # Thymeleaf templates
└── test/
    └── java/com/sivalabs/urlshortener/              # Test classes
```

## Installation & Setup

### 1. Clone or Download the Project

```bash
cd D:\Spring Practice\MinifyMe
```

### 2. Configure the Database

Ensure PostgreSQL is running and create a database:

```sql
CREATE DATABASE urlshortener;
```

Update `application.properties` if using different credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/urlshortener
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run

#### Option A: Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

#### Option B: Using Docker Compose

```bash
docker-compose up -d
mvn spring-boot:run
```

#### Option C: Run JAR File

```bash
mvn clean package
java -jar target/spring-boot-url-shortener-part-5-0.0.1-SNAPSHOT.jar
```

### 4. Access the Application

- **Web Interface**: http://localhost:8080
- **API Base URL**: http://localhost:8080/api

## Configuration

Key application properties in `application.properties`:

```properties
# Application name
spring.application.name=spring-boot-url-shortener

# URL Configuration
app.base-url=http://localhost:8080
app.default-expiry-in-days=30
app.validate-original-url=true

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres

# JPA/Hibernate
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

### Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `app.base-url` | Base URL for shortened links | http://localhost:8080 |
| `app.default-expiry-in-days` | Default expiration period in days | 30 |
| `app.validate-original-url` | Enable URL validation before shortening | true |

## Database Schema

The application uses Flyway for database migrations:

- **V1__create_tables.sql**: Creates initial schema (users, roles, short_urls tables)
- **V2__insert_sample_data.sql**: Inserts sample data for testing

## API Endpoints

### Create Short URL

```http
POST /api/short-urls
Content-Type: application/json

{
  "originalUrl": "https://www.example.com/very/long/url",
  "customSlug": "optional-custom-slug",
  "expiryInDays": 30
}
```

### Redirect to Original URL

```http
GET /{shortCode}
```

### Get Short URL Details

```http
GET /api/short-urls/{shortCode}
```

## Development

### Running Tests

```bash
mvn test
```

### IDE Setup

#### IntelliJ IDEA
1. Open project → File → Open
2. Configure JDK → File → Project Structure → Project SDK → Select Java 21
3. Maven should auto-index → Right-click project → Maven → Reload projects

#### Eclipse
1. Import as Maven Project
2. Configure Java 21 in Project Properties → Java Compiler
3. Maven → Update Project

## Docker Support

The project includes Docker Compose configuration for containerized database setup:

```bash
docker-compose up -d
```

This starts a PostgreSQL container configured for the application.

## Troubleshooting

### JDK Not Specified
- Go to File → Project Structure
- Set Project SDK to Java 21

### PostgreSQL Connection Error
- Ensure PostgreSQL is running
- Verify connection properties in `application.properties`
- Check database credentials

### Maven Dependencies Not Downloaded
- Run `mvn clean install`
- Or reload Maven projects in your IDE

### Port 8080 Already in Use
```bash
# Change in application.properties
server.port=8081
```
