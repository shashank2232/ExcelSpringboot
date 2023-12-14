package com.example.exceldemopractice.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.example.exceldemopractice.service.UploadService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class UploadController {

    @Autowired
    private UploadService uploadService;


//    @PostMapping("/read")
//    public ResponseEntity<List<List<String>>> readExcel(@RequestParam("file") MultipartFile file) {
//        try {
//            List<List<String>> result = uploadService.readExcelFile(file);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // method to change/modify excel file
//    @PostMapping("/process")
//    @ResponseBody
//    public ResponseEntity<byte[]> processExcel(@RequestParam("file") MultipartFile file) {
//        try {
//            Workbook modifiedWorkbook = uploadService.processExcelFile(file);
//
//            // Convert modified workbook to byte array
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            modifiedWorkbook.write(outputStream);
//            byte[] modifiedFileBytes = outputStream.toByteArray();

            // Set the response headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=modified_excel.xlsx");

            // Respond with the modified Excel file
//            return new ResponseEntity<>(modifiedFileBytes, headers, HttpStatus.OK);
//
//            return new ResponseEntity<>(modifiedFileBytes, HttpStatus.OK);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/readTitle")
//    @ResponseBody
//    public ResponseEntity<List<String>> readTitleListFromExcel(@RequestParam("file") MultipartFile file) {
//        try {
//            List<String> titleList = uploadService.getTitleListFromExcelFile(file);
//
//            if (!titleList.isEmpty()) {
//                return new ResponseEntity<>(titleList, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    FOR SWAGGER
//    @GetMapping (value="/modify",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> modifying(@RequestParam(value = "file",required = true) MultipartFile file ) throws IOException {
//        uploadService.parentMapping(file);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }



    @GetMapping ("/modify")
    public ResponseEntity<String> modifying() throws IOException {
        uploadService.parentMapping();

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
