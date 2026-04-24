package com.app.MinifyMe.kafka;

import com.app.MinifyMe.event.URLEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class URLEventConsumer {

    @KafkaListener(topics = "${spring.kafka.topics.url-shortened}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUrlShortenedEvent(URLEvent event) {
        log.info("Received URL shortened event - ShortCode: {}, OriginalUrl: {}, Timestamp: {}",
                event.getShortCode(), event.getOriginalUrl(), event.getTimestamp());
        // Process the URL shortened event (e.g., logging, analytics, etc.)
    }

    @KafkaListener(topics = "${spring.kafka.topics.url-accessed}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUrlAccessedEvent(URLEvent event) {
        log.info("Received URL accessed event - ShortCode: {}, ClickCount: {}, Timestamp: {}",
                event.getShortCode(), event.getClickCount(), event.getTimestamp());
        // Process the URL accessed event (e.g., analytics, tracking, etc.)
    }
}
