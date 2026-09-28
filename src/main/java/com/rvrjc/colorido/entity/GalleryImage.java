package com.rvrjc.colorido.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gallery_images")
public class GalleryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String category; // CROWD, MUSIC, DANCE, CAMPUS, SPORTS, CELEBRITY

    @Column(length = 1024, nullable = false)
    private String imageUrl;

    @Column(length = 1024)
    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String year;
    private Boolean isFeatured = false;
    private Integer displayOrder = 0;

    public GalleryImage() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public Boolean getIsFeatured() { return isFeatured != null && isFeatured; }
    public void setIsFeatured(Boolean featured) { isFeatured = featured; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}
