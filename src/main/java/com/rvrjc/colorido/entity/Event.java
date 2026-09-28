package com.rvrjc.colorido.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private EventCategory category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    private String eventDate;
    private String startTime;
    private String endTime;

    @Column(columnDefinition = "TEXT")
    private String rules;

    private String teamSize;
    private String prizes;

    @Column(length = 1024)
    private String registrationUrl;

    @Column(length = 1024)
    private String imageUrl;

    private Boolean isFeatured = false;
    private String status = "OPEN";

    private String coordinatorName;
    private String coordinatorContact;

    public Event() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public EventCategory getCategory() { return category; }
    public void setCategory(EventCategory category) { this.category = category; }

    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }

    public String getTeamSize() { return teamSize; }
    public void setTeamSize(String teamSize) { this.teamSize = teamSize; }

    public String getPrizes() { return prizes; }
    public void setPrizes(String prizes) { this.prizes = prizes; }

    public String getRegistrationUrl() { return registrationUrl; }
    public void setRegistrationUrl(String registrationUrl) { this.registrationUrl = registrationUrl; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsFeatured() { return isFeatured != null && isFeatured; }
    public void setIsFeatured(Boolean featured) { isFeatured = featured; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCoordinatorName() { return coordinatorName; }
    public void setCoordinatorName(String coordinatorName) { this.coordinatorName = coordinatorName; }

    public String getCoordinatorContact() { return coordinatorContact; }
    public void setCoordinatorContact(String coordinatorContact) { this.coordinatorContact = coordinatorContact; }
}
