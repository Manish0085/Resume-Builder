package com.build.resume.controller;


import com.build.resume.dto.CreateResumeRequest;
import com.build.resume.entity.Resume;
import com.build.resume.service.ResumeService;
import com.build.resume.service.TemplateService;
import com.build.resume.service.impl.FileUploadService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(TEMPLATE)
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<?> getTemplates(Authentication authentication){

        Map<String, Object> response = templateService.getTemplates(authentication.getPrincipal());

        return ResponseEntity.ok(response);
    }


}
