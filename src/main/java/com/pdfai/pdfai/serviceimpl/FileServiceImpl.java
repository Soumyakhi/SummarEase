package com.pdfai.pdfai.serviceimpl;
import com.pdfai.pdfai.entity.GrammarFile;
import com.pdfai.pdfai.entity.KeywordFile;
import com.pdfai.pdfai.entity.SummaryFile;
import com.pdfai.pdfai.repository.GrammarRepo;
import com.pdfai.pdfai.repository.KeywordRepo;
import com.pdfai.pdfai.repository.SummaryRepo;
import com.pdfai.pdfai.service.FileService;
import com.pdfai.pdfai.util.FileUtils;
import com.pdfai.pdfai.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    GrammarRepo grammarRepo;
    @Autowired
    FileUtils fileUtils;
    @Autowired
    HashUtil hashUtil;
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
            String hashText = hashUtil.getHash(extractedText);
            GrammarFile grammarF = grammarRepo.findByFileHash(hashText);
            if( grammarF!= null) {
                System.out.println("retrieved from hash");
                return new File(grammarF.getPath());
            }
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
            try {
                File resultFile = fileUtils.getResultantFile(correctedText); // already a valid .pdf


                // Define target directory and filename
                String currentDirectory = System.getProperty("user.dir");
                String path = currentDirectory + "\\src\\main\\webapp\\results";
                String fileName = hashText + ".pdf";

                // Create the results directory if it doesn't exist
                File folder = new File(path);
                if (!folder.exists()) folder.mkdirs();

                // Define the new destination file
                File targetFile = new File(path + File.separator + fileName);

                // Copy the file to the static directory with new name
                Files.copy(resultFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Save info to DB
                GrammarFile grammarFile = new GrammarFile();
                grammarFile.setPath(targetFile.getAbsolutePath());
                grammarFile.setFileHash(hashText);
                grammarRepo.save(grammarFile);

                return resultFile;

            }
            catch (IOException e){
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Autowired
    SummaryRepo summaryRepo;
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
            String hashText = hashUtil.getHash(extractedText);
            SummaryFile summaryF = summaryRepo.findByFileHash(hashText);
            if( summaryF!= null) {
                System.out.println("retrieved from hash");
                return new File(summaryF.getPath());
            }
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
                File resultFile=fileUtils.getResultantFile(summaryText);
                String currentDirectory = System.getProperty("user.dir");
                String path = currentDirectory + "\\src\\main\\webapp\\results";
                String fileName = hashText + ".pdf";

                // Create the results directory if it doesn't exist
                File folder = new File(path);
                if (!folder.exists()) folder.mkdirs();

                // Define the new destination file
                File targetFile = new File(path + File.separator + fileName);

                // Copy the file to the static directory with new name
                Files.copy(resultFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Save info to DB
                SummaryFile summaryFile = new SummaryFile();
                summaryFile.setPath(targetFile.getAbsolutePath());
                summaryFile.setFileHash(hashText);
                summaryRepo.save(summaryFile);
                return resultFile;
            }
            catch (IOException e){
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Autowired
    KeywordRepo keywordRepo;
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
            String hashText = hashUtil.getHash(extractedText);
            KeywordFile keywordF = keywordRepo.findByFileHash(hashText);
            if( keywordF!= null) {
                System.out.println("retrieved from hash");
                return new File(keywordF.getPath());
            }
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
                File resultFile=fileUtils.highlightRed(pdfFile,hashSet);
                String currentDirectory = System.getProperty("user.dir");
                String path = currentDirectory + "\\src\\main\\webapp\\results";
                String fileName = hashText + ".pdf";

                // Create the results directory if it doesn't exist
                File folder = new File(path);
                if (!folder.exists()) folder.mkdirs();

                // Define the new destination file
                File targetFile = new File(path + File.separator + fileName);

                // Copy the file to the static directory with new name
                Files.copy(resultFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Save info to DB
                KeywordFile keywordFile = new KeywordFile();
                keywordFile.setPath(targetFile.getAbsolutePath());
                keywordFile.setFileHash(hashText);
                keywordRepo.save(keywordFile);
                return resultFile;
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
