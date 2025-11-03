package com.build.resume.controller;

import com.build.resume.dto.AuthResponse;
import com.build.resume.dto.RegisterRequest;
import com.build.resume.service.AuthService;
import com.build.resume.util.AppConstants;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.build.resume.util.AppConstants.*;

@RestController
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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




}
