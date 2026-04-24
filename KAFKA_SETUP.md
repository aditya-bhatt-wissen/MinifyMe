# MinifyMe - Kafka Integration Guide

## Kafka Setup

### Prerequisites
- Docker and Docker Compose installed
- Java 17+
- Maven

### Running Kafka

1. **Start Kafka and Zookeeper using Docker Compose:**
   ```bash
   docker-compose up -d
   ```

   This will start:
   - Zookeeper on port 2181
   - Kafka on port 9092

2. **Verify Kafka is running:**
   ```bash
   docker ps
   ```
   You should see both `confluentinc/cp-zookeeper` and `confluentinc/cp-kafka` containers running.

### Building and Running the Application

1. **Build the project:**
   ```bash
   mvn clean install
   ```

2. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file:
   ```bash
   java -jar target/MinifyMe-0.0.1-SNAPSHOT.jar
   ```

## Kafka Topics

The application uses two topics:
- **url-shortened**: Published when a URL is shortened
- **url-accessed**: Published when a shortened URL is accessed

## Application Properties

Kafka configuration is defined in `application.properties`:

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.consumer.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=minifyme-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
spring.kafka.topics.url-shortened=url-shortened
spring.kafka.topics.url-accessed=url-accessed
```

## Testing Kafka Events

### 1. Create a Shortened URL (triggers url-shortened event)

**Request:**
```bash
POST http://localhost:8080/shorten?originalUrl=https://www.example.com&expiryInMinutes=120
```

**Response:**
```json
{
    "shortCode": "aBcDeF",
    "shortUrl": "http://localhost:8080/aBcDeF"
}
```

**Kafka Event Published:**
```json
{
    "shortCode": "aBcDeF",
    "originalUrl": "https://www.example.com",
    "eventType": "SHORTENED",
    "timestamp": "2026-04-24T10:30:00"
}
```

### 2. Access a Shortened URL (triggers url-accessed event)

**Request:**
```bash
GET http://localhost:8080/aBcDeF
```

**Kafka Event Published:**
```json
{
    "shortCode": "aBcDeF",
    "originalUrl": "https://www.example.com",
    "eventType": "ACCESSED",
    "timestamp": "2026-04-24T10:31:00",
    "clickCount": 1
}
```

## Monitoring Kafka Events

### Using Kafka Console Consumer

1. **Access Kafka container:**
   ```bash
   docker exec -it <kafka-container-id> bash
   ```

2. **List topics:**
   ```bash
   kafka-topics --list --bootstrap-server localhost:9092
   ```

3. **Consume url-shortened events:**
   ```bash
   kafka-console-consumer --topic url-shortened --from-beginning --bootstrap-server localhost:9092
   ```

4. **Consume url-accessed events:**
   ```bash
   kafka-console-consumer --topic url-accessed --from-beginning --bootstrap-server localhost:9092
   ```

### Application Logs

The application logs all Kafka events. Check the console output for logs like:

```
INFO  : Publishing URL shortened event for short code: aBcDeF
INFO  : Received URL shortened event - ShortCode: aBcDeF, OriginalUrl: https://www.example.com, Timestamp: 2026-04-24T10:30:00
INFO  : Publishing URL accessed event for short code: aBcDeF
INFO  : Received URL accessed event - ShortCode: aBcDeF, ClickCount: 1, Timestamp: 2026-04-24T10:31:00
```

## Stopping Kafka

```bash
docker-compose down
```

## Troubleshooting

**Issue: Connection refused to localhost:9092**
- Make sure Kafka is running: `docker ps`
- Check if port 9092 is available

**Issue: Topics not auto-created**
- Kafka is configured with `KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"` in docker-compose.yml
- Topics will be created automatically when first message is published

**Issue: Consumer group issues**
- Reset consumer group offset: 
  ```bash
  docker exec -it <kafka-container-id> kafka-consumer-groups --bootstrap-server localhost:9092 --group minifyme-group --reset-offsets --to-earliest --execute --all-topics
  ```

## Project Structure

```
src/main/java/com/app/MinifyMe/
├── config/
│   └── KafkaConfig.java         # Kafka configuration
├── event/
│   └── URLEvent.java             # Event model
├── kafka/
│   ├── URLEventProducer.java     # Kafka producer
│   └── URLEventConsumer.java     # Kafka consumer
├── service/
│   └── URLService.java           # Service (publishes events)
├── controller/
│   └── URLController.java        # REST endpoints
├── entity/
│   └── ShortURL.java             # Database entity
└── repository/
    └── URLRepository.java        # JPA repository
```
