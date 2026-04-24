package com.app.MinifyMe.service;

import com.app.MinifyMe.entity.ShortURL;
import com.app.MinifyMe.event.URLEvent;
import com.app.MinifyMe.kafka.URLEventProducer;
import com.app.MinifyMe.repository.URLRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class URLService {
    private final URLRepository urlRepository;
    private final URLEventProducer eventProducer;
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 6;
    private final Random random = new Random();

    public String shortenURL(String originalUrl, Integer expiryInMinutes) {
        String shortCode = generateUniqueShortCode();

        ShortURL shortURL = ShortURL.builder()
                .originalUrl(originalUrl)
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expiryInMinutes))
                .clickCount(0)
                .build();

        urlRepository.save(shortURL);

        // Publish Kafka event
        URLEvent event = URLEvent.builder()
                .shortCode(shortCode)
                .originalUrl(originalUrl)
                .eventType("SHORTENED")
                .timestamp(LocalDateTime.now())
                .build();
        eventProducer.publishUrlShortenedEvent(event);

        return shortCode;
    }

    // ...existing code...
    private String generateUniqueShortCode() {
        String code;

        do{
            code = generateRandomCode();
        } while(urlRepository.existsByShortCode(code));

        return code;
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }

        return sb.toString();
    }

    public String getOriginalUrl(String shortCode) {
        ShortURL shortUrl = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        // Expiry check
        if (shortUrl.getExpiresAt() != null &&
                shortUrl.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Short URL expired");
        }

        // Increment click count
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        urlRepository.save(shortUrl);

        // Publish Kafka event
        URLEvent event = URLEvent.builder()
                .shortCode(shortCode)
                .originalUrl(shortUrl.getOriginalUrl())
                .eventType("ACCESSED")
                .timestamp(LocalDateTime.now())
                .clickCount(shortUrl.getClickCount())
                .build();
        eventProducer.publishUrlAccessedEvent(event);

        return shortUrl.getOriginalUrl();
    }

}