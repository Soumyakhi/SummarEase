package com.pdfai.pdfai.repository;
import com.pdfai.pdfai.entity.SummaryFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepo extends JpaRepository<SummaryFile, Long> {
    public SummaryFile findByFileHash(String hash);
}