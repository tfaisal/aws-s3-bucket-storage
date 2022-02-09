package com.awss3cloud.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private AmazonS3Client amazonS3Client;

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return "File uploaded : " + fileName;
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    public String createBucket(String bucketName) {
        BucketName = "my-first-bucket" + UUID.randomUUID();

        if(!s3Client.doesBucketExist(bucketName))
        {
            s3Client.createBucket(new CreateBucketRequest(BucketName));
            return "Bucket Created \n Bucket Name:-" + bucketName + "\nregion:-"
                    + s3Client.getBucketLocation(new GetBucketLocationRequest(BucketName));
            log.info(BucketName+ "Successfully Created");
        }
        return " Bucket already exist";
    }

    public List<Bucket> getAllBuckets() {
        int counter = 0;
        List<Bucket> buckets = amazonS3Client.listBuckets();
        for(Bucket bucket : buckets)
        {
            counter++;
            log.info("Bucket"+counter+ "::: "+ bucket);
        }
        return buckets;
    }

    public List<BucketPolicy> getBucketPolicy(String bucketName) {
        int counter = 0;
        BucketPolicy bucketPolicies = s3Client.getBucketPolicy(bucketName);
        for(BucketPolicy bucketPolicy : bucketPolicies)
        {
            counter++;
            log.info("BucketPolicy"+counter+ "::: "+ bucketPolicy);
        }
        return bucketPolicies;
    }

    public List<BucketPolicy> getBucketAclPolicy(String bucketName) {
        int counter = 0;
        AccessControlList acl = s3Client.getBucketAcl(bucketName);
        List<Grant> grants = acl.getGrantsAsList();
        for(Grant grant : grants)
        {
            counter++;
            log.info("BucketAclPolicy"+counter+ "::: "+ grant);
        }
        return grants;
    }

}
