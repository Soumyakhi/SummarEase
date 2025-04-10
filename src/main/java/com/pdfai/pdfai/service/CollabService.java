package com.pdfai.pdfai.service;

import com.pdfai.pdfai.dto.EditorDeltaJSON;
import jakarta.servlet.http.HttpServletRequest;

public interface CollabService {
    public String fetchDeltaJson(String uuid);
    public boolean updateDoc(EditorDeltaJSON editorDeltaJSON);
}
