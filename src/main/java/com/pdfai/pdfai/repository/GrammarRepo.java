package com.pdfai.pdfai.repository;

import com.pdfai.pdfai.entity.GrammarFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarRepo extends JpaRepository<GrammarFile, Long> {
    public GrammarFile findByFileHash(String hash);
}
