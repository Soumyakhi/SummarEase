package com.pdfai.pdfai.serviceimpl;

import com.pdfai.pdfai.entity.TextContent;
import com.pdfai.pdfai.repository.TextRepo;
import com.pdfai.pdfai.service.AddEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddEditorImpl implements AddEditor {
    @Autowired
    TextRepo TextRepo;
    @Autowired
    private TextRepo textRepo;

    @Override
    public boolean addEditor(String uuid, String deltaJson) {
        if(textRepo.findByEditorId(uuid) == null) {
            TextContent textContent = new TextContent();
            textContent.setEditorId(uuid);
            textContent.setDeltaJson(deltaJson);
            TextRepo.save(textContent);
            return true;
        }
        return false;
    }
}
