package com.build.resume.dto;

public class CreateResumeRequest {

    private String title;


    public CreateResumeRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
