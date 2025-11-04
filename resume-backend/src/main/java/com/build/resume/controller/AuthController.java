package com.build.resume.controller;

import com.build.resume.dto.AuthResponse;
import com.build.resume.dto.LoginRequest;
import com.build.resume.dto.RegisterRequest;
import com.build.resume.service.AuthService;
import com.build.resume.service.impl.FileUploadService;
import com.build.resume.util.AppConstants;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.build.resume.util.AppConstants.*;

@RestController
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {

    private final AuthService authService;
    private final FileUploadService fileUploadService;

    public AuthController(AuthService authService, FileUploadService fileUploadService) {
        this.authService = authService;
        this.fileUploadService = fileUploadService;
    }

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        AuthResponse response = authService.register(request);
        logger.info("Response from service: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }



    @GetMapping(VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        authService.verifyEmail(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("message", "Email verified Successfully"));
    }


    @PostMapping(UPLOAD_IMAGE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        logger.info("Inside AuthController - uploadImage()");
        Map<String, String> response = fileUploadService.uploadSingleImage(file);
        return ResponseEntity.of(Optional.ofNullable(response));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(RESEND_VERIFICATION)
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> body){
        String email = body.get("email");

        if (Objects.isNull(email)){
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        authService.resendVerification(email);

        return ResponseEntity.ok(Map.of("success", true, "message", "Verification is sent"));
    }


    @PostMapping(PROFILE)
    public ResponseEntity<?> getProfile(Authentication authentication){
        Object principal = authentication.getPrincipal();

        AuthResponse currentProfile = authService.getProfile(principal);

        return ResponseEntity.ok(currentProfile);
    }

}
