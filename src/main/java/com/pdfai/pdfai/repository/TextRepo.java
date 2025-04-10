package com.pdfai.pdfai.repository;

import com.pdfai.pdfai.entity.TextContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepo extends JpaRepository<TextContent, Long> {
    public TextContent findByEditorId(String uuid);
}
