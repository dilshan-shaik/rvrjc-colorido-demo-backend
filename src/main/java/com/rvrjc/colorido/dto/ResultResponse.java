package com.rvrjc.colorido.dto;

import com.rvrjc.colorido.entity.Position;
import com.rvrjc.colorido.entity.Result;

public class ResultResponse {

    private Long id;
    private Long eventId;
    private Position position;
    private String participantName;
    private String teamName;
    private String collegeName;
    private String score;
    private String remarks;
    private boolean published;

    public ResultResponse() {
    }

    public ResultResponse(Result result) {
        this.id = result.getId();

        if (result.getEvent() != null) {
            this.eventId = result.getEvent().getId();
        }

        this.position = result.getPosition();
        this.participantName = result.getParticipantName();
        this.teamName = result.getTeamName();
        this.collegeName = result.getCollegeName();
        this.score = result.getScore();
        this.remarks = result.getRemarks();
        this.published = result.isPublished();
    }

    public Long getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
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
}