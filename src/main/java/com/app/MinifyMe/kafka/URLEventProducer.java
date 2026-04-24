package com.app.MinifyMe.kafka;

import com.app.MinifyMe.event.URLEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class URLEventProducer {
    private final KafkaTemplate<String, URLEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.url-shortened}")
    private String urlShortenedTopic;

    @Value("${spring.kafka.topics.url-accessed}")
    private String urlAccessedTopic;

    public void publishUrlShortenedEvent(URLEvent event) {
        log.info("Publishing URL shortened event for short code: {}", event.getShortCode());
        kafkaTemplate.send(urlShortenedTopic, event.getShortCode(), event);
    }

    public void publishUrlAccessedEvent(URLEvent event) {
        log.info("Publishing URL accessed event for short code: {}", event.getShortCode());
        kafkaTemplate.send(urlAccessedTopic, event.getShortCode(), event);
    }
}
