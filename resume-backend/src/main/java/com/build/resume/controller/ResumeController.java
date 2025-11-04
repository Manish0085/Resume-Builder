package com.build.resume.controller;


import com.build.resume.dto.CreateResumeRequest;
import com.build.resume.entity.Resume;
import com.build.resume.service.ResumeService;
import com.build.resume.service.impl.FileUploadService;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.build.resume.util.AppConstants.*;

@RestController
@RequestMapping(RESUME)
public class ResumeController {

    private final ResumeService resumeService;
    private final FileUploadService fileUploadService;

    private final Logger log = LoggerFactory.getLogger(ResumeController.class);

    public ResumeController(ResumeService resumeService, FileUploadService fileUploadService) {
        this.resumeService = resumeService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest request,
                                          Authentication authentication) {
        Resume newResume = resumeService.craeteResume(request, authentication.getPrincipal());

        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);
    }

    @GetMapping
    public ResponseEntity<?> getUserResume(Authentication authentication){
        List<Resume> resumes = resumeService.getUserResumes(authentication.getPrincipal());

        return ResponseEntity.ok(resumes);
    }

    @GetMapping(ID)
    public ResponseEntity<?> getResumeById(@PathVariable String id,
                                           Authentication authentication) {
        Resume existingResume = resumeService.getResumeById(id, authentication.getPrincipal());
        return ResponseEntity.ok(existingResume);
    }

    @PutMapping(ID)
    public ResponseEntity<?> updateResume(@PathVariable String id,
                                          @RequestBody Resume updatedData,
                                          Authentication authentication) {

        Resume updatedResume = resumeService.updateResume(id, updatedData, authentication.getPrincipal());

        return ResponseEntity.ok(updatedResume);

    }

    @PutMapping(UPLOAD_IMAGES)
    public ResponseEntity<?> uploadResumeImages(@PathVariable String id,
                                                @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                                @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                Authentication authentication) throws IOException {


        Map<String, String> response = fileUploadService.uploadResumeImages(id, authentication.getPrincipal(), thumbnail, profileImage);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(ID)
    public ResponseEntity<?> deleteResume(@PathVariable String id,
                                          Authentication authentication) {
        resumeService.deleteResume(id, authentication.getPrincipal());

        return ResponseEntity.ok(Map.of("Message", "Resume deleted successfully"));
    }
}
