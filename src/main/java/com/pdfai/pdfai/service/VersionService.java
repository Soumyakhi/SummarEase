package com.pdfai.pdfai.service;

import com.pdfai.pdfai.dto.SaveVersionDTO;
import com.pdfai.pdfai.entity.VersionControl;

import java.util.List;

public interface VersionService {
    public boolean save(SaveVersionDTO saveVersionDTO);
    public VersionControl fetchVersionControl(long id);
    public List<VersionControl> fetchAllVersions(String editorId);
}
