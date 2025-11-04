package com.build.resume.service;

import com.build.resume.dto.CreateResumeRequest;
import com.build.resume.entity.Resume;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ResumeService {
    Resume craeteResume(CreateResumeRequest request, Object principalObject);

    List<Resume> getUserResumes(Object principal);

    Resume getResumeById(String id, Object principal);

    Resume updateResume(String id, Resume updatedData, Object principal);

    void deleteResume(String id, Object principal);
}
