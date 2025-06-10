package com.pdfai.pdfai.repository;

import com.pdfai.pdfai.entity.KeywordFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepo extends JpaRepository<KeywordFile, Long> {
    public KeywordFile findByFileHash(String hash);
}
