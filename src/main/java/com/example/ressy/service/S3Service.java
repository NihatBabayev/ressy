package com.example.ressy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface S3Service {
    void uploadPhoto(String photoName, MultipartFile file) throws IOException;

    String getObjectFromS3AsBase64(String fullPath);
     byte[] getObjectFromS3AsBytes(String fullPath);
}
