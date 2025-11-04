package com.build.resume.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "resume")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;
    private String title;
    private String thumbnailLink;

    private Template template;

    private ProfileInfo profileInfo;

    private ContactInfo contactInfo;

    private List<WorkExperience> workExperiences;

    private List<Education> education;
    private List<Skill> skills;
    private List<Project> projects;
    private List<Certification> certifications;
    private List<Language> languages;
    private List<String> interests;


    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Resume(String id, String userId, String title, String thumbnailLink, Template template, ProfileInfo profileInfo, ContactInfo contactInfo, List<WorkExperience> workExperiences, List<Education> education, List<Skill> skills, List<Project> projects, List<Certification> certifications, List<Language> languages, List<String> interests, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.thumbnailLink = thumbnailLink;
        this.template = template;
        this.profileInfo = profileInfo;
        this.contactInfo = contactInfo;
        this.workExperiences = workExperiences;
        this.education = education;
        this.skills = skills;
        this.projects = projects;
        this.certifications = certifications;
        this.languages = languages;
        this.interests = interests;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Resume() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<WorkExperience> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(List<WorkExperience> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Template {
        private String theme;
        private List<String> colorPalette;

        public Template(String theme, List<String> colorPalette) {
            this.theme = theme;
            this.colorPalette = colorPalette;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public List<String> getColorPalette() {
            return colorPalette;
        }

        public void setColorPalette(List<String> colorPalette) {
            this.colorPalette = colorPalette;
        }
    }

    public static class ProfileInfo {
        private String profilePreviewUrl;
        private String fullName;
        private String description;
        private String summary;

        public ProfileInfo(String profilePreviewUrl, String fullName, String description, String summary) {
            this.profilePreviewUrl = profilePreviewUrl;
            this.fullName = fullName;
            this.description = description;
            this.summary = summary;
        }

        public ProfileInfo() {

        }

        public String getProfilePreviewUrl() {
            return profilePreviewUrl;
        }

        public void setProfilePreviewUrl(String profilePreviewUrl) {
            this.profilePreviewUrl = profilePreviewUrl;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }

    public static class ContactInfo {
        private String email;
        private String phone;
        private String location;
        private String linkedIn;
        private String github;
        private String website;

        public ContactInfo(String email, String phone, String location, String linkedIn, String github, String website) {
            this.email = email;
            this.phone = phone;
            this.location = location;
            this.linkedIn = linkedIn;
            this.github = github;
            this.website = website;
        }

        public ContactInfo() {

        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLinkedIn() {
            return linkedIn;
        }

        public void setLinkedIn(String linkedIn) {
            this.linkedIn = linkedIn;
        }

        public String getGithub() {
            return github;
        }

        public void setGithub(String github) {
            this.github = github;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }
    }


    public static class WorkExperience{
        private String company;
        private String role;
        private String startDate;
        private String endDate;
        private String description;

        public WorkExperience(String company, String role, String startDate, String endDate, String description) {
            this.company = company;
            this.role = role;
            this.startDate = startDate;
            this.endDate = endDate;
            this.description = description;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


    public static class Education{
        private String degree;
        private String institution;
        private String startDate;
        private String endDate;

        public Education(String degree, String institution, String startDate, String endDate) {
            this.degree = degree;
            this.institution = institution;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getInstitution() {
            return institution;
        }

        public void setInstitution(String institution) {
            this.institution = institution;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }


    public static class Skill{

        private String skillName;
        private Integer progress;

        public Skill(String skillName, Integer progress) {
            this.skillName = skillName;
            this.progress = progress;
        }

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public Integer getProgress() {
            return progress;
        }

        public void setProgress(Integer progress) {
            this.progress = progress;
        }
    }

    public static class Project {

        private String title;
        private String description;
        private String github;
        private String liveDemo;

        public Project(String title, String description, String github, String liveDemo) {
            this.title = title;
            this.description = description;
            this.github = github;
            this.liveDemo = liveDemo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getGithub() {
            return github;
        }

        public void setGithub(String github) {
            this.github = github;
        }

        public String getLiveDemo() {
            return liveDemo;
        }

        public void setLiveDemo(String liveDemo) {
            this.liveDemo = liveDemo;
        }
    }


    public static class Certification{
        private String title;
        private String issuer;
        private String year;

        public Certification(String title, String issuer, String year) {
            this.title = title;
            this.issuer = issuer;
            this.year = year;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }

    public static class Language {
        private String name;
        private Integer progress;

        public Language(String name, Integer progress) {
            this.name = name;
            this.progress = progress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getProgress() {
            return progress;
        }

        public void setProgress(Integer progress) {
            this.progress = progress;
        }
    }

}
