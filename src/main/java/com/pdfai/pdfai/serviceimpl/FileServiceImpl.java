package com.pdfai.pdfai.serviceimpl;
import com.pdfai.pdfai.service.FileService;
import com.pdfai.pdfai.util.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileUtils fileUtils;
    @Override
    public File getResultGrammar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            return null;
        }
        File pdfFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try {
            file.transferTo(pdfFile);
            String extractedText = fileUtils.extractTextFromPDF(pdfFile);
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/grammar";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", extractedText);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            String correctedText="Error: API request failed";
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                correctedText = (String) response.getBody().get("corrected");
            }
            try{
                return fileUtils.getResultantFile(correctedText);
            }
            catch (IOException e){
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public File getResultSummary(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            return null;
        }
        File pdfFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try {
            file.transferTo(pdfFile);
            String extractedText = fileUtils.extractTextFromPDF(pdfFile);
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/summarize";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", extractedText);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            String summaryText="Error: API request failed";
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                summaryText = (String) response.getBody().get("summary");
            }
            try{
                return fileUtils.getResultantFile(summaryText);
            }
            catch (IOException e){
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getResultKeyWords(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }
        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            return null;
        }
        File pdfFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try {
            file.transferTo(pdfFile);
            String extractedText = fileUtils.extractTextFromPDF(pdfFile);
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/keywords";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", extractedText);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            HashSet<String> hashSet=new HashSet<>();
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object summaryObject = response.getBody().get("relevant_sentences");
                if (summaryObject instanceof List<?>) {
                    List<?> list = (List<?>) summaryObject;
                    for (Object obj : list) {
                        if (obj instanceof String) {
                            String str = (String) obj;
                            str=str.replace("\n","");
                            str=str.trim();
                            hashSet.add((String) str);
                        }
                    }
                }
            }
            try{
                return fileUtils.highlightRed(pdfFile,hashSet);
            }
            catch (IOException e){
                System.out.println(e);
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
