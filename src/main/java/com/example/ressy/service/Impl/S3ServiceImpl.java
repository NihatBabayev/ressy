package com.example.ressy.service.Impl;

import com.example.ressy.exception.IllegalFileTypeException;
import com.example.ressy.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client amazonS3;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public void uploadPhoto(String photoName, MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (!contentType.equals("image/png") && !contentType.equals("image/jpeg")) {
            throw new IllegalFileTypeException();
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(photoName)
                .contentType(contentType)
                .build();

        amazonS3.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public String getObjectFromS3AsBase64(String fullPath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fullPath)
                    .build();

            ResponseInputStream<GetObjectResponse> objectBytes = amazonS3.getObject(getObjectRequest);

            byte[] contentBytes = objectBytes.readAllBytes();

            return Base64.getEncoder().encodeToString(contentBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching object from S3", e);
        }
    }
}
