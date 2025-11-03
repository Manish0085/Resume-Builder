package com.build.resume.service.impl;

import com.build.resume.dto.AuthResponse;
import com.build.resume.dto.RegisterRequest;
import com.build.resume.entity.User;
import com.build.resume.exception.ResourceExistsException;
import com.build.resume.repository.UserRepository;
import com.build.resume.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final EmailService emailService;


    public AuthServiceImpl(UserRepository userRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    @Value("${app.base.url:http://localhost:8081}")
    private String appBaseUrl;
    private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public AuthResponse register(RegisterRequest request){
        log.info("Inside AuthService: register() {}", request);
        if (userRepo.existsByEmail(request.getEmail())){
            throw new ResourceExistsException("User already exists with this email");
        }
        User newUser = toEntity(request);
        userRepo.save(newUser);

        // send verification email
        sendVerificationEmail(newUser);

       return toResponse(newUser);
    }

    private void sendVerificationEmail(User newUser) {
        log.info("Inside AuthService - sendVerificationEmail(): {}", newUser);
        try {
            String link = appBaseUrl+"/api/auth/verify-email?token="+newUser.getEmailVerificationToken();
            String html = "<div style='font-family:sans-serif'>" +
                    "<h2>Verify your email</h2>" +
                    "<p>Hi " + newUser.getName() + ", please confirm your email to activate your account.</p>" +
                    "<a href='" + link + "' " +
                    "style='display:inline-block;padding:10px 16px;background:#6366f1;color:#fff;" +
                    "text-decoration:none;border-radius:5px;'>Verify Email</a>" +
                    "<p>Or copy this link: " + link + "</p>" +
                    "<p>This link expires in 24 hours.</p>" +
                    "</div>";
            emailService.sendHtmlEmail(newUser.getEmail(), "Verify your Email", html);
        }
        catch (Exception ex){
            log.info("Exception occurred at sendVerification(): {}", ex.getMessage());
            throw new RuntimeException("Failed to send verification email: " + ex.getMessage());
        }
    }

    private AuthResponse toResponse(User newUser) {
        return new AuthResponse(
                newUser.getId(),
                newUser.getName(),
                newUser.getEmail(),
                newUser.getProfileImageUrl(),
                newUser.isEmailVerified(),
                newUser.getSubscriptionPlan(),
                newUser.getCreatedAt(),
                newUser.getUpdatedAt()
        );
    }


    private User toEntity(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setSubscriptionPlan("Basic");
        user.setEmailVerified(false);
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        user.setVerificationExpires(LocalDateTime.now().plusHours(24));
        return user;
    }


    @Override
    public void verifyEmail(String token){
        log.info("Inside AuthService: verifyEmail(): {}", token);
        User user = userRepo.findByEmailVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification token"));

        if (user.getVerificationExpires() != null && user.getVerificationExpires().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Verification token has expired. Please request new one.");
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setVerificationExpires(null);
        userRepo.save(user);

    }

}
