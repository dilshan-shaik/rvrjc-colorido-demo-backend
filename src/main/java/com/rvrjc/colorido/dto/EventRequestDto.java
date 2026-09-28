package com.rvrjc.colorido.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRequestDto {
    private String name;
    private Long categoryId;
    private Long venueId;
    private String description;
    private String rules;
    private String teamSize;
    private String prizes;
    private String eventDate;
    private String startTime;
    private String endTime;
    private String registrationUrl;
    private String imageUrl;
    private Boolean isFeatured;
    private String status;
    private String coordinatorName;
    private String coordinatorContact;

    public EventRequestDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Object val) {
        this.categoryId = parseLongSafely(val);
    }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Object val) {
        this.venueId = parseLongSafely(val);
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }

    public String getTeamSize() { return teamSize; }
    public void setTeamSize(String teamSize) { this.teamSize = teamSize; }

    public String getPrizes() { return prizes; }
    public void setPrizes(String prizes) { this.prizes = prizes; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getRegistrationUrl() { return registrationUrl; }
    public void setRegistrationUrl(String registrationUrl) { this.registrationUrl = registrationUrl; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCoordinatorName() { return coordinatorName; }
    public void setCoordinatorName(String coordinatorName) { this.coordinatorName = coordinatorName; }

    public String getCoordinatorContact() { return coordinatorContact; }
    public void setCoordinatorContact(String coordinatorContact) { this.coordinatorContact = coordinatorContact; }

    private Long parseLongSafely(Object val) {
        if (val == null) return null;
        String s = val.toString().trim();
        if (s.isEmpty() || s.equals("0")) return null;
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }
}
