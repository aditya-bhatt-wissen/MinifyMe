# MinifyMe - URL Shortener Service

A lightweight Spring Boot REST API for shortening URLs with expiration, click tracking, and Kafka event streaming.

## ✨ Features

- 🔗 Shorten long URLs to 6-character unique codes
- ⏱️ Set URL expiration times (default: 60 minutes)
- 📊 Track click counts automatically
- 📡 Stream events to Kafka (optional)
- 🚀 Event-driven architecture
- 💾 H2 in-memory database (no setup needed)

## 📋 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Kafka 3.x (optional)

### Build & Run
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

Visit `http://localhost:8080/` → You should see **"Welcome to MinifyMe!"**

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/` | Welcome message |
| `POST` | `/shorten?originalUrl=<URL>&expiryInMinutes=120` | Shorten a URL |
| `GET` | `/{shortCode}` | Redirect to original URL |
| `GET` | `/api/url/{shortCode}` | Get original URL as JSON |
| `GET` | `/h2-console` | Database console |

### Example: Shorten a URL

**PowerShell:**
```powershell
curl.exe -X POST "http://localhost:8080/shorten?originalUrl=https://www.google.com&expiryInMinutes=120"
```

**Response:**
```json
{
  "shortCode": "aBcDeF",
  "shortUrl": "http://localhost:8080/aBcDeF"
}
```

### Example: Access Shortened URL
```powershell
curl.exe -X GET "http://localhost:8080/aBcDeF" -L
```

## ⚙️ Configuration

### Enable Kafka (Optional)

**`application.properties`:**
```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.topics.url-shortened=url-shortened
spring.kafka.topics.url-accessed=url-accessed
```

**Start Kafka:**
```bash
# Terminal 1: Zookeeper
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

# Terminal 2: Kafka
bin\windows\kafka-server-start.bat config\server.properties
```

### Disable Kafka

```properties
spring.kafka.bootstrap-servers=
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
```

## 📁 Project Structure

```
src/
├── controller/        # REST endpoints
├── service/          # Business logic
├── entity/           # JPA entities
├── repository/       # Data access
├── kafka/            # Kafka producer & consumer
├── event/            # Event models
└── config/           # Kafka configuration
```

## 🎯 How It Works

**Shorten URL:**
```
POST /shorten
  → Save to Database
  → Publish event to kafka/url-shortened
  → Return short code
```

**Access URL:**
```
GET /{shortCode}
  → Increment click count
  → Publish event to kafka/url-accessed
  → Redirect to original URL
```

## 🧪 Testing

**With Postman:**
1. Create POST request to `http://localhost:8080/shorten`
2. Add parameters: `originalUrl` and `expiryInMinutes`
3. Click Send

**With Maven:**
```bash
mvn test
```

## 💾 Database

**H2 Console:** `http://localhost:8080/h2-console`
- **URL:** `jdbc:h2:mem:minifyme`
- **Username:** `sa`
- **Password:** (empty)

Data clears on restart. To persist:
```properties
spring.datasource.url=jdbc:h2:file:./data/minifyme
```

## 🔧 Troubleshooting

| Issue | Solution |
|-------|----------|
| Port 8080 in use | Change port: `server.port=8081` |
| Kafka connection fails | Check Zookeeper (2181) & Kafka (9092) running |
| URL not found | Might be expired (default: 60 min) |
| Duplicate URL error | Use different URL or restart app |

## 📦 Tech Stack

| Component | Version |
|-----------|---------|
| Spring Boot | 4.0.4 |
| Java | 17+ |
| Kafka | Optional |
| H2 Database | 2.4.240 |
| Lombok | 1.18.30 |

## 📝 License

Learning & Development

---

**Version:** 1.0.0-SNAPSHOT | **Last Updated:** April 27, 2026
