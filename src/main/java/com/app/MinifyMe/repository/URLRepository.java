package com.app.MinifyMe.repository;

import com.app.MinifyMe.entity.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<ShortURL, Long> {
    Optional<ShortURL> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    void deleteByExpiresAtBefore(LocalDateTime time);
}