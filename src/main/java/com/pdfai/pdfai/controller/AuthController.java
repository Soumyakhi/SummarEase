package com.pdfai.pdfai.controller;
import com.pdfai.pdfai.dto.Code;
import com.pdfai.pdfai.dto.FileDTO;
import com.pdfai.pdfai.dto.LoginInfoDTO;
import com.pdfai.pdfai.service.FileService;
import com.pdfai.pdfai.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;

@CrossOrigin
@RestController
public class AuthController {
    @Autowired
    LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<?> handleGoogleCallback(@RequestBody Code code) {
        try {
            LoginInfoDTO loginInfoDTO=loginService.login(code);
            if(loginInfoDTO!=null) {
                return new ResponseEntity<>(loginInfoDTO, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @Autowired
    private FileService fileService;
    @PostMapping("/getResGrammar")
    public File getResGrammar(FileDTO fileDTO){
        return fileService.getResultGrammar(fileDTO.getPdfFile());
    }
    @PostMapping("/getResSummary")
    public File getResSummary(FileDTO fileDTO){
        return fileService.getResultSummary(fileDTO.getPdfFile());
    }
    @PostMapping("/getResKeyWords")
    public File getResKeyWords(FileDTO fileDTO){
        return fileService.getResultKeyWords(fileDTO.getPdfFile());
    }

}
