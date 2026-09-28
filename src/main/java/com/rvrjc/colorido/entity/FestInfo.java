package com.rvrjc.colorido.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fest_info")
public class FestInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String festName;

    @Column(nullable = false)
    private String collegeName;

    private String collegeLocation;
    private String tagline;
    private String startDate;
    private String endDate;
    private String edition;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String history;

    @Column(columnDefinition = "TEXT")
    private String aboutContent;

    @Column(columnDefinition = "TEXT")
    private String importantNotice;

    private String heroVideoUrl;
    private String heroBannerUrl;
    private String themeColor;

    public FestInfo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFestName() { return festName; }
    public void setFestName(String festName) { this.festName = festName; }

    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

    public String getCollegeLocation() { return collegeLocation; }
    public void setCollegeLocation(String collegeLocation) { this.collegeLocation = collegeLocation; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHistory() { return history; }
    public void setHistory(String history) { this.history = history; }

    public String getAboutContent() { return aboutContent; }
    public void setAboutContent(String aboutContent) { this.aboutContent = aboutContent; }

    public String getImportantNotice() { return importantNotice; }
    public void setImportantNotice(String importantNotice) { this.importantNotice = importantNotice; }

    public String getHeroVideoUrl() { return heroVideoUrl; }
    public void setHeroVideoUrl(String heroVideoUrl) { this.heroVideoUrl = heroVideoUrl; }

    public String getHeroBannerUrl() { return heroBannerUrl; }
    public void setHeroBannerUrl(String heroBannerUrl) { this.heroBannerUrl = heroBannerUrl; }

    public String getThemeColor() { return themeColor; }
    public void setThemeColor(String themeColor) { this.themeColor = themeColor; }
}
