package com.awss3cloud.s3.controller;


import com.javatechie.s3.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private StorageService service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }

    @PostMapping(value = "bucket/create/{bucketName}")
    public ResponseEntity<String> createBucket(@PathVariable String bucketName) {
        return new ResponseEntity<>(service.createBucket(bucketName), HttpStatus.OK);
    }

    @GetMapping(value = "/buckets/list")
    public List<Bucket> getlistBucket() {
        return service.getAllBuckets();
    }

    @GetMapping(value = "/bucket/policy/{bucketName}")
    public ResponseEntity<List<String>> getBucketPolicy(@PathVariable String bucketName) {
        return new ResponseEntity<>(service.getBucketPolicy(bucketName), HttpStatus.OK);
    }

    @GetMapping(value = "/bucket/Aclpolicy/{bucketName}")
    public ResponseEntity<List<String>> getBuckeAcltPolicy(@PathVariable String bucketName) {
        return new ResponseEntity<>(service.getBucketAclPolicy(bucketName), HttpStatus.OK);
    }

}
