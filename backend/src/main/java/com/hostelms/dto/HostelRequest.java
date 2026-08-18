package com.hostelms.dto;

import jakarta.validation.constraints.NotBlank;

public class HostelRequest {

    @NotBlank(message = "Hostel name is required")
    private String name;

    private String location;

    private Long wardenId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getWardenId() {
        return wardenId;
    }

    public void setWardenId(Long wardenId) {
        this.wardenId = wardenId;
    }
}
