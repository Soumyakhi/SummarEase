package com.pdfai.pdfai.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileDTO {
    private MultipartFile pdfFile;
    public MultipartFile getPdfFile() {
        return pdfFile;
    }
    public void setPdfFile(MultipartFile pdfFile) {
        this.pdfFile = pdfFile;
    }
}
