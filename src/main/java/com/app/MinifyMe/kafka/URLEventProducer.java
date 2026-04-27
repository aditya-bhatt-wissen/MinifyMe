package com.app.MinifyMe.kafka;

import com.app.MinifyMe.event.URLEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class URLEventProducer {
    @Autowired(required = false)
    private KafkaTemplate<String, URLEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.url-shortened}")
    private String urlShortenedTopic;

    @Value("${spring.kafka.topics.url-accessed}")
    private String urlAccessedTopic;

    public void publishUrlShortenedEvent(URLEvent event) {
        log.info("Publishing URL shortened event for short code: {}", event.getShortCode());
        if (kafkaTemplate != null) {
            kafkaTemplate.send(urlShortenedTopic, event.getShortCode(), event);
        } else {
            log.warn("KafkaTemplate is not available. Event not published.");
        }
    }

    public void publishUrlAccessedEvent(URLEvent event) {
        log.info("Publishing URL accessed event for short code: {}", event.getShortCode());
        if (kafkaTemplate != null) {
            kafkaTemplate.send(urlAccessedTopic, event.getShortCode(), event);
        } else {
            log.warn("KafkaTemplate is not available. Event not published.");
        }
    }
}
