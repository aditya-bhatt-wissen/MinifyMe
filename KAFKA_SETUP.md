# Running MinifyMe Without Docker

Since you don't have Docker installed, here are alternative ways to run Kafka locally:

## Option 1: Download and Run Kafka Directly (Recommended for Development)

### Prerequisites
- Java 8+ (you already have Java 22)
- Zookeeper is included with Kafka

### Steps:

1. **Download Kafka:**
   - Go to https://kafka.apache.org/downloads
   - Download the latest version (e.g., `kafka_2.13-3.7.0.tgz` for Linux/Mac or `.zip` for Windows)
   - Extract it to a folder, e.g., `C:\kafka` or `~/kafka`

2. **Start Zookeeper (Terminal 1):**
   
   **On Windows:**
   ```bash
   cd C:\kafka
   .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
   ```
   
   **On Mac/Linux:**
   ```bash
   cd ~/kafka
   ./bin/zookeeper-server-start.sh ./config/zookeeper.properties
   ```
   
   You should see: `INFO binding to port 0.0.0.0/0.0.0.0:2181`

3. **Start Kafka Server (Terminal 2):**
   
   **On Windows:**
   ```bash
   cd C:\kafka
   .\bin\windows\kafka-server-start.bat .\config\server.properties
   ```
   
   **On Mac/Linux:**
   ```bash
   cd ~/kafka
   ./bin/kafka-server-start.sh ./config/server.properties
   ```
   
   You should see: `INFO [KafkaServer id=0] started`

4. **Verify Kafka is Running:**
   
   **Create a test topic (Terminal 3):**
   ```bash
   # Windows
   cd C:\kafka
   .\bin\windows\kafka-topics.bat --create --topic test --bootstrap-server localhost:9092
   
   # Mac/Linux
   cd ~/kafka
   ./bin/kafka-topics.sh --create --topic test --bootstrap-server localhost:9092
   ```

### Keep Terminals Open
You need to keep both Zookeeper and Kafka running while developing. It's best to have 3 terminals open:
- Terminal 1: Zookeeper
- Terminal 2: Kafka
- Terminal 3: Your IDE or Maven commands

---

## Option 2: Use Confluent Platform (Easier Alternative)

Confluent provides an all-in-one package with Kafka, Zookeeper, and management tools.

1. **Download:** https://www.confluent.io/download/
2. **Extract** to your preferred location
3. **Run Confluent:**
   
   **On Windows:**
   ```bash
   cd C:\confluent-7.x.x
   .\bin\confluent local services start
   ```
   
   **On Mac/Linux:**
   ```bash
   cd ~/confluent-7.x.x
   ./bin/confluent local services start
   ```

This starts everything (Zookeeper, Kafka, Schema Registry, etc.) automatically!

Stop it with:
```bash
confluent local services stop
```

---

## Option 3: Use WSL (Windows Subsystem for Linux) + Docker

If you're on Windows and want Docker-like experience:

1. Install WSL2 (Windows Subsystem for Linux)
2. Install Docker inside WSL2
3. Use the `docker-compose.yml` file provided

This gives you Docker benefits without full Docker Desktop overhead.

---

## Option 4: Use Kafka in Memory (No External Broker - Development Only)

For development/testing WITHOUT a real Kafka broker:

### Update application.properties:
```properties
spring.application.name=MinifyMe

# H2 DATABASE
spring.datasource.url=jdbc:h2:mem:minifyme
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Disable Kafka (for testing)
spring.kafka.bootstrap-servers=
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
```

This completely disables Kafka, and your application will still work. The event publishing will be skipped gracefully.

---

## Recommended Setup for Development

### I recommend **Option 1** (Direct Kafka Download):

**Pros:**
- Lightweight
- Easy to control
- Good for local development
- Just Java and Kafka

**Cons:**
- Need to manage multiple terminals
- Manual startup/shutdown

---

## Quick Start Guide (Option 1)

### First Time Setup:
```bash
# Terminal 1: Start Zookeeper
cd C:\kafka  # or ~/kafka on Mac/Linux
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

# Terminal 2: Start Kafka
cd C:\kafka  # or ~/kafka on Mac/Linux
bin\windows\kafka-server-start.bat config\server.properties

# Terminal 3: Start your MinifyMe application
cd D:\SpringBoot\MinifyMe
mvn spring-boot:run
```

### Test the Application:
```bash
# Shorten a URL
curl -X POST "http://localhost:8080/shorten?originalUrl=https://www.google.com&expiryInMinutes=120"

# Result:
# {
#   "shortCode": "abc123",
#   "shortUrl": "http://localhost:8080/abc123"
# }

# Access the shortened URL
curl -X GET "http://localhost:8080/abc123"

# You should see Kafka events in MinifyMe logs:
# Publishing URL shortened event for short code: abc123
# Received URL shortened event - ShortCode: abc123
```

---

## Monitoring Kafka Topics (Optional)

To view messages in Kafka topics:

**List topics:**
```bash
# Windows
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092

# Mac/Linux
./bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

**View messages in a topic:**
```bash
# Windows
bin\windows\kafka-console-consumer.bat --topic url-shortened --from-beginning --bootstrap-server localhost:9092

# Mac/Linux
./bin/kafka-console-consumer.sh --topic url-shortened --from-beginning --bootstrap-server localhost:9092
```

---

## Troubleshooting

### Kafka won't start: "Address already in use"
Port 9092 is already in use. Either:
- Close the application using port 9092
- Change Kafka port in `config/server.properties`: `listeners=PLAINTEXT://localhost:9093`

### Zookeeper connection error
Make sure Zookeeper started successfully before starting Kafka

### Topics not auto-creating
Create topics manually:
```bash
# Windows
bin\windows\kafka-topics.bat --create --topic url-shortened --bootstrap-server localhost:9092
bin\windows\kafka-topics.bat --create --topic url-accessed --bootstrap-server localhost:9092

# Mac/Linux
./bin/kafka-topics.sh --create --topic url-shortened --bootstrap-server localhost:9092
./bin/kafka-topics.sh --create --topic url-accessed --bootstrap-server localhost:9092
```

---

## Which Option to Choose?

| Option | Best For | Difficulty | Setup Time |
|--------|----------|-----------|-----------|
| **Option 1: Direct Kafka** | Local development | Easy | 10 mins |
| **Option 2: Confluent** | Learning & testing | Easy | 15 mins |
| **Option 3: WSL + Docker** | Windows + Docker benefits | Medium | 30 mins |
| **Option 4: No Kafka** | Quick testing | Very Easy | 2 mins |

---

## Next Steps

1. **Choose your option** (I recommend Option 1)
2. **Set it up** following the instructions above
3. **Start Zookeeper and Kafka**
4. **Run MinifyMe:** `mvn spring-boot:run`
5. **Test with Postman or curl**

Good luck! Let me know if you have any issues with setup.
