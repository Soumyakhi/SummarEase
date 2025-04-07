package com.pdfai.pdfai.controller;
import com.pdfai.pdfai.dto.FileDTO;
import com.pdfai.pdfai.service.FileService;
import com.pdfai.pdfai.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@CrossOrigin
@RestController
@RequestMapping("/index")
public class FilteredController {
    @GetMapping("/home")
    public String home() {
        return "Hello World";
    }
    @Autowired
    private LogoutService logout;
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        logout.logoutUser(request);
        return "Logout successful";
    }

}
