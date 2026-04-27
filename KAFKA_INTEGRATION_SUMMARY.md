# MinifyMe - Kafka Integration Summary

## What Was Added

### 1. **Dependencies** (pom.xml)
- `spring-kafka` - Spring Boot starter for Kafka integration

### 2. **Kafka Configuration** (src/main/java/com/app/MinifyMe/config/KafkaConfig.java)
- Simple configuration class that enables Kafka
- Relies on Spring Boot's auto-configuration

### 3. **Event Model** (src/main/java/com/app/MinifyMe/event/URLEvent.java)
- `URLEvent` class represents the event published to Kafka
- Fields: `shortCode`, `originalUrl`, `eventType`, `timestamp`, `clickCount`

### 4. **Kafka Producer** (src/main/java/com/app/MinifyMe/kafka/URLEventProducer.java)
- `URLEventProducer` service that publishes events to Kafka
- Methods:
  - `publishUrlShortenedEvent()` - publishes to `url-shortened` topic
  - `publishUrlAccessedEvent()` - publishes to `url-accessed` topic

### 5. **Kafka Consumer** (src/main/java/com/app/MinifyMe/kafka/URLEventConsumer.java)
- `URLEventConsumer` service that listens to Kafka topics
- Methods:
  - `consumeUrlShortenedEvent()` - consumes from `url-shortened` topic
  - `consumeUrlAccessedEvent()` - consumes from `url-accessed` topic

### 6. **Updated URLService** (src/main/java/com/app/MinifyMe/service/URLService.java)
- Injected `URLEventProducer` via constructor
- Publishes events when:
  - URL is shortened
  - URL is accessed

### 7. **Kafka Configuration Properties** (application.properties)
- Bootstrap servers: `localhost:9092`
- Topics: `url-shortened`, `url-accessed`
- Consumer group: `minifyme-group`
- Serializers: `StringSerializer` for keys, `JsonSerializer` for values

### 8. **Docker Compose** (docker-compose.yml)
- Zookeeper service on port 2181
- Kafka service on port 9092
- Auto-creates topics on first message

### 9. **Documentation** (KAFKA_SETUP.md)
- Complete setup guide
- How to run Kafka with Docker
- Testing instructions
- Monitoring with Kafka console tools
- Troubleshooting guide

## Event Flow

### URL Shortened Event
```
POST /shorten?originalUrl=https://example.com
    ↓
URLService.shortenURL()
    ↓
Saves ShortURL to database
    ↓
URLEventProducer.publishUrlShortenedEvent()
    ↓
Message published to 'url-shortened' topic
    ↓
URLEventConsumer.consumeUrlShortenedEvent()
    ↓
Logs event: "Received URL shortened event..."
```

### URL Accessed Event
```
GET /{shortCode}
    ↓
URLService.getOriginalUrl()
    ↓
Increments click count
    ↓
URLEventProducer.publishUrlAccessedEvent()
    ↓
Message published to 'url-accessed' topic
    ↓
URLEventConsumer.consumeUrlAccessedEvent()
    ↓
Logs event: "Received URL accessed event..."
```

## Quick Start

1. **Start Kafka:**
   ```bash
   docker-compose up -d
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Test the API:**
   ```bash
   # Shorten a URL
   curl -X POST "http://localhost:8080/shorten?originalUrl=https://www.example.com&expiryInMinutes=60"
   
   # Access the shortened URL
   curl -X GET "http://localhost:8080/abc123"
   ```

5. **Monitor events in logs:**
   Look for messages like:
   - `Publishing URL shortened event for short code: ...`
   - `Received URL shortened event - ShortCode: ...`
   - `Publishing URL accessed event for short code: ...`
   - `Received URL accessed event - ShortCode: ...`

## Future Enhancements

You can extend this Kafka integration with:
- Analytics/metrics collection
- Email notifications on URL access
- User activity tracking
- Real-time dashboards
- Event replay for data analysis
- Integration with external systems
