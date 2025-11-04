package com.build.resume.service;

import com.build.resume.dto.CreateResumeRequest;
import com.build.resume.entity.Resume;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface TemplateService {

    Map<String, Object> getTemplates(Object principal);
}
