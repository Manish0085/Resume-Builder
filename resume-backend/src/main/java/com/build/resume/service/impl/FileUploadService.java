package com.build.resume.service.impl;


import com.build.resume.dto.AuthResponse;
import com.build.resume.entity.Resume;
import com.build.resume.repository.ResumeRepository;
import com.build.resume.service.AuthService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.mail.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class FileUploadService {

    private final Cloudinary cloudinary;
    private final AuthService authService;
    private final ResumeRepository resumeRepo;

    private final Logger log = LoggerFactory.getLogger(FileUploadService.class);

    public FileUploadService(Cloudinary cloudinary, AuthService authService, ResumeRepository resumeRepo) {
        this.cloudinary = cloudinary;
        this.authService = authService;
        this.resumeRepo = resumeRepo;
    }

    public Map<String, String> uploadSingleImage(MultipartFile file) throws IOException {
        Map<String, Object> imageUploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "image"));
        log.info("Inside FileUploadService - uploadSingleImage(): {}", imageUploadResult.get("secure_url"));
        return Map.of("imageUrl", imageUploadResult.get("secure_url").toString());
    }

    public Map<String, String> uploadResumeImages(String resumeId,
                                                  Object principal,
                                                  MultipartFile thumbnail,
                                                  MultipartFile profileImage) throws IOException {

        AuthResponse response = authService.getProfile(principal);

        Resume existingResume = resumeRepo.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Map<String, String> returnValue = new HashMap<>();
        Map<String, String> uploadResult;
        if (Objects.nonNull(thumbnail)){
            uploadResult = uploadSingleImage(thumbnail);
            existingResume.setThumbnailLink(uploadResult.get("imageUrl"));
            returnValue.put("thumbnailLink", uploadResult.get("imageUrl"));
        }

        if (Objects.nonNull(profileImage)){
            uploadResult = uploadSingleImage(thumbnail);
            if (Objects.isNull(existingResume.getProfileInfo())){
                existingResume.setProfileInfo(new Resume.ProfileInfo());
            }
            existingResume.getProfileInfo().setProfilePreviewUrl(uploadResult.get("imageUrl"));
            returnValue.put("profilePreviewUrl", uploadResult.get("imageUrl"));
        }

        resumeRepo.save(existingResume);
        returnValue.put("message", "Images uploaded successfully");

        return returnValue;

    }
}
