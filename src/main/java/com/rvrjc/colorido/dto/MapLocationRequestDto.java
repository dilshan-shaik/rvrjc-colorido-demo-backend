package com.rvrjc.colorido.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MapLocationRequestDto {
    private String name;
    private String category;
    private String description;
    private Double posX;
    private Double posY;
    private String icon;
    private Long venueId;
    private Boolean isActive;

    public MapLocationRequestDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPosX() { return posX; }
    public void setPosX(Double posX) { this.posX = posX; }

    public Double getPosY() { return posY; }
    public void setPosY(Double posY) { this.posY = posY; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Object val) {
        if (val == null) { this.venueId = null; return; }
        String s = val.toString().trim();
        if (s.isEmpty() || s.equals("0")) { this.venueId = null; return; }
        try {
            this.venueId = Long.parseLong(s);
        } catch (Exception e) {
            this.venueId = null;
        }
    }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
