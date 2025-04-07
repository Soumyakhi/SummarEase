package com.pdfai.pdfai.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileService {
    public File getResultGrammar(MultipartFile file);
    public File getResultSummary(MultipartFile file);
    public File getResultKeyWords(MultipartFile file);
}
