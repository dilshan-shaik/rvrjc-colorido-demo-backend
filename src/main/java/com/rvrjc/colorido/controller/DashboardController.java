package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.dto.DashboardStats;
import com.rvrjc.colorido.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository categoryRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private MapLocationRepository mapLocationRepository;

    @Autowired
    private GalleryImageRepository galleryImageRepository;

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    @GetMapping("/stats")
    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        stats.setTotalEvents(eventRepository.count());
        stats.setTotalCategories(categoryRepository.count());
        stats.setTotalVenues(venueRepository.count());
        stats.setTotalScheduleItems(scheduleItemRepository.count());
        stats.setTotalMapLocations(mapLocationRepository.count());
        stats.setTotalGalleryImages(galleryImageRepository.count());
        stats.setTotalContacts(contactInfoRepository.count());
        return stats;
    }
}
