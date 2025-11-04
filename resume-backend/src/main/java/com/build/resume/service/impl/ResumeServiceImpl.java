package com.build.resume.service.impl;


import com.build.resume.dto.AuthResponse;
import com.build.resume.dto.CreateResumeRequest;
import com.build.resume.entity.Resume;
import com.build.resume.repository.ResumeRepository;
import com.build.resume.service.AuthService;
import com.build.resume.service.ResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepo;
    private final AuthService authService;

    private final Logger log = LoggerFactory.getLogger(ResumeServiceImpl.class);

    public ResumeServiceImpl(ResumeRepository resumeRepo, AuthService authService) {
        this.resumeRepo = resumeRepo;
        this.authService = authService;
    }

    @Override
    public Resume craeteResume(CreateResumeRequest request,
                               Object principalObject) {
        Resume newResume = new Resume();

        AuthResponse response = authService.getProfile(principalObject);

        newResume.setUserId(response.getId());
        newResume.setTitle(request.getTitle());

        setDefaultResumeData(newResume);

        return resumeRepo.save(newResume);
    }

    @Override
    public List<Resume> getUserResumes(Object principal) {
        AuthResponse response = authService.getProfile(principal);

        return resumeRepo.findByUserIdOrderedByUpdatedAtDesc(response.getId());
    }

    @Override
    public Resume getResumeById(String resumeId, Object principal) {
        AuthResponse response = authService.getProfile(principal);

        Resume existingResume = resumeRepo.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return existingResume;
    }

    @Override
    public Resume updateResume(String resumeId, Resume updatedData, Object principal) {
        AuthResponse response = authService.getProfile(principal);

        Resume existingResume = resumeRepo.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        existingResume.setTitle(updatedData.getTitle());
        existingResume.setThumbnailLink(updatedData.getThumbnailLink());
        existingResume.setTemplate(updatedData.getTemplate());
        existingResume.setProfileInfo(updatedData.getProfileInfo());
        existingResume.setProfileInfo(updatedData.getProfileInfo());
        existingResume.setSkills(updatedData.getSkills());
        existingResume.setEducation(updatedData.getEducation());
        existingResume.setWorkExperiences(updatedData.getWorkExperiences());
        existingResume.setProjects(updatedData.getProjects());
        existingResume.setCertifications(updatedData.getCertifications());
        existingResume.setInterests(updatedData.getInterests());

        resumeRepo.save(existingResume);

        return existingResume;
    }

    @Override
    public void deleteResume(String resumeId, Object principal) {
        AuthResponse response = authService.getProfile(principal);

        Resume existingResume = resumeRepo.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        resumeRepo.delete(existingResume);



    }

    private void setDefaultResumeData(Resume newResume) {

        newResume.setProfileInfo(new Resume.ProfileInfo());
        newResume.setContactInfo(new Resume.ContactInfo());
        newResume.setWorkExperiences(new ArrayList<>());
        newResume.setEducation(new ArrayList<>());
        newResume.setSkills(new ArrayList<>());
        newResume.setProjects(new ArrayList<>());
        newResume.setCertifications(new ArrayList<>());
        newResume.setLanguages(new ArrayList<>());
        newResume.setInterests(new ArrayList<>());

    }

}
