package com.build.resume.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.mail.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class FileUploadService {

    private final Cloudinary cloudinary;

    private final Logger log = LoggerFactory.getLogger(FileUploadService.class);

    public FileUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, String> uploadSingleImage(MultipartFile file) throws IOException {
        Map<String, Object> imageUploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "image"));
        log.info("Inside FileUploadService - uploadSingleImage(): {}", imageUploadResult.get("secure_url"));
        return Map.of("imageUrl", imageUploadResult.get("secure_url").toString());
    }
}
