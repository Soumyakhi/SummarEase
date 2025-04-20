package com.pdfai.pdfai.controller;
import com.pdfai.pdfai.dto.EditorDeltaJSON;
import com.pdfai.pdfai.dto.FileDTO;
import com.pdfai.pdfai.entity.TextContent;
import com.pdfai.pdfai.service.AddEditor;
import com.pdfai.pdfai.service.CollabService;
import com.pdfai.pdfai.service.FileService;
import com.pdfai.pdfai.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private CollabService collabService;
    @GetMapping("/fetchContent/{uuid}")
    public ResponseEntity<?> fetchContent(@PathVariable String uuid){
        EditorDeltaJSON editorDeltaJSON=collabService.fetchDeltaJson(uuid);
        if(editorDeltaJSON==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(editorDeltaJSON,HttpStatus.OK);
    }
    @PostMapping("/updateContent")
    public ResponseEntity<?> updateContent(@RequestBody EditorDeltaJSON editorDeltaJSON){
        if(!collabService.updateDoc(editorDeltaJSON)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Autowired
    private AddEditor addEditor;
    @PostMapping("/createEditor")
    public boolean createEditor(@RequestBody TextContent textContent){
        System.out.println(textContent.getEditorId());
        return addEditor.addEditor(textContent.getEditorId(),textContent.getDeltaJson());
    }
    @PostMapping("/setFullContent")
    public String setFullContent(@RequestBody EditorDeltaJSON editorDeltaJSON){
        collabService.addFullDoc(editorDeltaJSON);
        return "Success";
    }
}
