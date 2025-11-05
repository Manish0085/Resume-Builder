package com.build.resume.controller;

import com.build.resume.dto.AuthResponse;
import com.build.resume.dto.LoginRequest;
import com.build.resume.dto.RegisterRequest;
import com.build.resume.service.AuthService;
import com.build.resume.service.impl.EmailService;
import com.build.resume.service.impl.FileUploadService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.build.resume.util.AppConstants.*;

@RestController
@RequestMapping(EMAIL_CONTROLLER)
public class EmailController {

    private  final Logger log = LoggerFactory.getLogger(EmailController.class);

    private final EmailService emailService;


    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(value = SEND_RESUME, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> sendResumeEmail(
            @RequestPart("recipientEmail") String recipientEmail,
            @RequestPart("subject") String subject,
            @RequestPart("message") String message,
            @RequestPart("pdfFile") MultipartFile pdfFile,
            Authentication authentication
    ) throws IOException, MessagingException {

        Map<String, Object> response = new HashMap<>();
        if (Objects.isNull(recipientEmail) || Objects.isNull(pdfFile)){
            response.put("success", false);
            response.put("message", "Missing required fields");
            return ResponseEntity.badRequest().body(response);
        }

        byte[] pdfBytes = pdfFile.getBytes();
        String originalFilename = pdfFile.getOriginalFilename();
        String fileName = Objects.nonNull(originalFilename) ? originalFilename : "resume.pdf";

        String emailSubject = Objects.nonNull(subject) ? subject : "Resume Application";
        String emailBody = Objects.nonNull(message) ? message : "Please find my resume attached.\n\nBest Regards";

        emailService.sendEmailWithAttachment(recipientEmail, emailSubject, emailBody, pdfBytes, fileName);

        response.put("success", true);
        response.put("message", "Resume send successfully to " + recipientEmail);
        return ResponseEntity.ok(response);
    }
}
