package com.pdfai.pdfai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class PdfAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdfAiApplication.class, args);
        File folder = new File("/app/src/main/webapp/results/");
        if (!folder.exists()) {
            folder.mkdirs(); // Create full directory structure
        }
    }

}
