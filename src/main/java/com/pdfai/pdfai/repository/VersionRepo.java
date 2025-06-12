package com.pdfai.pdfai.repository;

import com.pdfai.pdfai.entity.GrammarFile;
import com.pdfai.pdfai.entity.TextContent;
import com.pdfai.pdfai.entity.VersionControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionRepo extends JpaRepository<VersionControl, Long> {
    public List<VersionControl> findAllByTextContent_EditorIdOrderByIdDesc(String editorId);
    public VersionControl findById(long id);
}
