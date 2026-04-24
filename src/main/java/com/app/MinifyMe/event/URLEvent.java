package com.app.MinifyMe.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class URLEvent {
    private String shortCode;
    private String originalUrl;
    private String eventType; // "SHORTENED" or "ACCESSED"
    private LocalDateTime timestamp;
    private Integer clickCount;
}
