package com.build.resume.repository;

import com.build.resume.entity.Resume;
import com.build.resume.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, String>{

    List<Resume> findByUserIdOrderedByUpdatedAtDesc(String userId);
    Optional<Resume> findByUserIdAndId(String userId, String id);
}
