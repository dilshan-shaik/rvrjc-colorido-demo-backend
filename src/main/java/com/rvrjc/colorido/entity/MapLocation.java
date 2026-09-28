package com.rvrjc.colorido.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "map_locations")
public class MapLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category; // VENUE, FOOD, ENTRY, INFO, SPORTS, FACILITY

    @Column(columnDefinition = "TEXT")
    private String description;

    // Percentages 0.0 - 100.0 on campus map
    private Double posX;
    private Double posY;

    private String icon; // utensils, stage, info, shield, heart-pulse, car, trophy

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    private Boolean isActive = true;

    public MapLocation() {}

    public MapLocation(String name, String category, String description, Double posX, Double posY, String icon, Venue venue, Boolean isActive) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.posX = posX;
        this.posY = posY;
        this.icon = icon;
        this.venue = venue;
        this.isActive = isActive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

    public Boolean getIsActive() { return isActive != null && isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}
