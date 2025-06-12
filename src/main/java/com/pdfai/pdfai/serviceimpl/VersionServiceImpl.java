package com.pdfai.pdfai.serviceimpl;

import com.pdfai.pdfai.dto.SaveVersionDTO;
import com.pdfai.pdfai.entity.TextContent;
import com.pdfai.pdfai.entity.VersionControl;
import com.pdfai.pdfai.repository.TextRepo;
import com.pdfai.pdfai.repository.VersionRepo;
import com.pdfai.pdfai.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionServiceImpl implements VersionService {
    @Autowired
    private VersionRepo versionRepo;
    @Autowired
    private TextRepo textRepo;
    @Override
    public boolean save(SaveVersionDTO saveVersionDTO) {
        VersionControl versionControl = new VersionControl();
        TextContent textContent = textRepo.findByEditorId(saveVersionDTO.getEditorId());
        if(textContent != null) {
            versionControl.setDeltaJson(saveVersionDTO.getDeltaJson());
            versionControl.setTextContent(textContent);
            versionRepo.save(versionControl);
            System.out.println("version added");
            return true;
        }
        return false;
    }
    @Override
    public VersionControl fetchVersionControl(long id) {
        return versionRepo.findById(id);
    }
    public List<VersionControl> fetchAllVersions(String editorId){
        return versionRepo.findAllByTextContent_EditorIdOrderByIdDesc(editorId);
    }
}
