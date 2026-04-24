package com.app.MinifyMe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
    // Spring Boot auto-configures KafkaTemplate based on application.properties
    // No additional configuration needed
}
