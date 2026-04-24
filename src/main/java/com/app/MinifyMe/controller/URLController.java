package com.app.MinifyMe.controller;

import com.app.MinifyMe.service.URLService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class URLController {
    private final URLService urlService;

    @GetMapping
    public String home() {
        return "Welcome to MinifyMe!";
    }

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shortenURL(
            @RequestParam String originalUrl,
            @RequestParam(defaultValue = "60") Integer expiryInMinutes) {
        try {
            String shortCode = urlService.shortenURL(originalUrl, expiryInMinutes);
            Map<String, String> response = new HashMap<>();
            response.put("shortCode", shortCode);
            response.put("shortUrl", "http://localhost:8080/" + shortCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortCode) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode);
            return ResponseEntity.status(302)
                    .header("Location", originalUrl)
                    .build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/url/{shortCode}")
    public ResponseEntity<Map<String, String>> getOriginalURL(@PathVariable String shortCode) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode);
            Map<String, String> response = new HashMap<>();
            response.put("originalUrl", originalUrl);
            response.put("shortCode", shortCode);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}