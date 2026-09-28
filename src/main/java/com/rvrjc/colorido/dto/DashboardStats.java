package com.rvrjc.colorido.dto;

public class DashboardStats {
    private long totalEvents;
    private long totalCategories;
    private long totalVenues;
    private long totalScheduleItems;
    private long totalMapLocations;
    private long totalGalleryImages;
    private long totalContacts;

    public DashboardStats() {}

    public long getTotalEvents() { return totalEvents; }
    public void setTotalEvents(long totalEvents) { this.totalEvents = totalEvents; }

    public long getTotalCategories() { return totalCategories; }
    public void setTotalCategories(long totalCategories) { this.totalCategories = totalCategories; }

    public long getTotalVenues() { return totalVenues; }
    public void setTotalVenues(long totalVenues) { this.totalVenues = totalVenues; }

    public long getTotalScheduleItems() { return totalScheduleItems; }
    public void setTotalScheduleItems(long totalScheduleItems) { this.totalScheduleItems = totalScheduleItems; }

    public long getTotalMapLocations() { return totalMapLocations; }
    public void setTotalMapLocations(long totalMapLocations) { this.totalMapLocations = totalMapLocations; }

    public long getTotalGalleryImages() { return totalGalleryImages; }
    public void setTotalGalleryImages(long totalGalleryImages) { this.totalGalleryImages = totalGalleryImages; }

    public long getTotalContacts() { return totalContacts; }
    public void setTotalContacts(long totalContacts) { this.totalContacts = totalContacts; }
}
