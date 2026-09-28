package com.rvrjc.colorido.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleItemRequestDto {
    private String title;
    private Long eventId;
    private Long venueId;
    private Integer dayNumber;
    private String scheduleDate;
    private String startTime;
    private String endTime;
    private String description;
    private String type;

    public ScheduleItemRequestDto() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getEventId() { return eventId; }
    public void setEventId(Object val) {
        this.eventId = parseLongSafely(val);
    }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Object val) {
        this.venueId = parseLongSafely(val);
    }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    public String getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(String scheduleDate) { this.scheduleDate = scheduleDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

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
