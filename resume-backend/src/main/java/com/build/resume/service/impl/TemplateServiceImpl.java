package com.build.resume.service.impl;


import com.build.resume.dto.AuthResponse;
import com.build.resume.dto.CreateResumeRequest;
import com.build.resume.entity.Resume;
import com.build.resume.repository.ResumeRepository;
import com.build.resume.service.AuthService;
import com.build.resume.service.ResumeService;
import com.build.resume.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.build.resume.util.AppConstants.PREMIUM;

@Service
public class TemplateServiceImpl implements TemplateService {


    private final AuthService authService;

    public TemplateServiceImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Map<String, Object> getTemplates(Object principal) {
        AuthResponse response = authService.getProfile(principal);

        List<String> allTemplates = Arrays.asList("01", "02", "03");
        List<String> availableTemplates;

        Boolean isPremium = PREMIUM.equalsIgnoreCase(response.getSubscriptionPlan());
        if (isPremium){
            availableTemplates = allTemplates;
        } else {
            availableTemplates = List.of("01");
        }

        Map<String, Object> restrictions = new HashMap<>();
        restrictions.put("availableTemplates", availableTemplates);
        restrictions.put("allTemplates", List.of("01", "02", "03"));
        restrictions.put("subscriptionPlan", response.getSubscriptionPlan());
        restrictions.put("isPremium", isPremium);

        return restrictions;
    }
}
