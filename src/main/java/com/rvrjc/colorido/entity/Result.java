package com.rvrjc.colorido.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @Column(nullable = false)
    private String participantName;

    private String teamName;

    private String collegeName;

    private String score;

    @Column(length = 1000)
    private String remarks;

    @Column(nullable = false)
    private boolean published = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // =========================
    // GETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public Position getPosition() {
        return position;
    }

    public String getParticipantName() {
        return participantName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getScore() {
        return score;
    }

    public String getRemarks() {
        return remarks;
    }

    public boolean isPublished() {
        return published;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // =========================
    // SETTERS
    // =========================

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}