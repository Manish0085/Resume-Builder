package com.build.resume.service;

import com.build.resume.dto.AuthResponse;
import com.build.resume.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    void verifyEmail(String token);
}
