package com.pdfai.pdfai.service;

import com.pdfai.pdfai.dto.EditorDeltaJSON;
import jakarta.servlet.http.HttpServletRequest;

public interface CollabService {
    public EditorDeltaJSON fetchDeltaJson(String uuid);
    public boolean updateDoc(EditorDeltaJSON editorDeltaJSON);
    public void addFullDoc(EditorDeltaJSON editorDeltaJSON);
}
