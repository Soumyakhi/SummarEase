package com.pdfai.pdfai.serviceimpl;

import com.pdfai.pdfai.dto.EditorDeltaJSON;
import com.pdfai.pdfai.repository.TextRepo;
import com.pdfai.pdfai.service.CollabService;
import com.pdfai.pdfai.util.MyWebSocketHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollabServiceImpl implements CollabService {
    @Autowired
    MyWebSocketHandler myWebSocketHandler;
    @Override
    public String fetchDeltaJson(String uuid) {
        return myWebSocketHandler.getEditorContent(uuid);
    }
    @Autowired
    TextRepo textRepo;
    @Override
    public boolean updateDoc(EditorDeltaJSON editorDeltaJSON) {
        if(textRepo.findByEditorId(editorDeltaJSON.getUuid())==null){
            return false;
        }
        updateDoc(editorDeltaJSON);
        return true;
    }
}
