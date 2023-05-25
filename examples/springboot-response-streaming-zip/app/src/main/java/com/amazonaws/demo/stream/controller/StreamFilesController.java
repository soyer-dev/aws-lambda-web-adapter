package com.amazonaws.demo.stream.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.*;
import java.nio.file.Files;

@RestController
@EnableWebMvc
public class StreamFilesController {

    @RequestMapping(value = "/files/{file_name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("file_name") String fileName) {
        return new FileSystemResource(fileName);
    }

    @GetMapping("/stream")
    public ResponseEntity<Resource> streamFile(@RequestParam("filename") String filename) throws IOException {
        // Load the file from the filesystem or any other source
        File file = new File(filename);

        // Check if the file exists
        if (!file.exists()) {
            // Return an error response if the file does not exist
            return ResponseEntity.notFound().build();
        }

        // Create an InputStreamResource from the file
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        // Stream the file as the response
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/stream-dummy")
    public ResponseEntity<InputStreamResource> streamDummyFile(@RequestParam("size") long fileSize) throws IOException {
        // Create a byte array with the specified size
        byte[] dummyData = new byte[(int) fileSize*1024*1024];
        System.out.println("Size "+fileSize+" array size :"+dummyData.length);

        // Create an InputStream from the byte array
        InputStream inputStream = new ByteArrayInputStream(dummyData);

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dummy_file.bin");

        // Stream the dummy file as the response
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileSize*1024*1024)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }

}
