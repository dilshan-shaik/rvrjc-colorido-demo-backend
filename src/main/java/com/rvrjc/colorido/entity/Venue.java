package com.rvrjc.colorido.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer capacity;
    private String landmark;
    
    // Percentage coordinates on base campus map (0.0 to 100.0)
    private Double mapX;
    private Double mapY;

    public Venue() {}

    public Venue(String name, String code, String description, Integer capacity, String landmark, Double mapX, Double mapY) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.capacity = capacity;
        this.landmark = landmark;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public Double getMapX() { return mapX; }
    public void setMapX(Double mapX) { this.mapX = mapX; }

    public Double getMapY() { return mapY; }
    public void setMapY(Double mapY) { this.mapY = mapY; }
}
