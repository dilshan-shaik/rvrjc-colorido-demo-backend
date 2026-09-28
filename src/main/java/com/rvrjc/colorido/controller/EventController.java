package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.dto.EventRequestDto;
import com.rvrjc.colorido.entity.Event;
import com.rvrjc.colorido.entity.EventCategory;
import com.rvrjc.colorido.entity.Venue;
import com.rvrjc.colorido.repository.EventCategoryRepository;
import com.rvrjc.colorido.repository.EventRepository;
import com.rvrjc.colorido.repository.ScheduleItemRepository;
import com.rvrjc.colorido.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository categoryRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;


    // =========================
    // GET ALL EVENTS
    // =========================
    @GetMapping
    public List<Event> getEvents(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean featured) {

        if (categoryId != null) {
            return eventRepository.findByCategoryId(categoryId);
        }

        if (Boolean.TRUE.equals(featured)) {
            return eventRepository.findByIsFeaturedTrue();
        }

        return eventRepository.findAll();
    }


    // =========================
    // GET EVENT BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {

        return eventRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // =========================
    // CREATE EVENT
    // =========================
    @PostMapping
    public ResponseEntity<?> createEvent(
            @RequestBody EventRequestDto dto) {

        Event event = new Event();

        populateEventFromDto(event, dto);


        // CATEGORY
        if (dto.getCategoryId() == null) {

            // Default to first category if category is not provided
            List<EventCategory> cats = categoryRepository.findAll();

            if (!cats.isEmpty()) {
                event.setCategory(cats.get(0));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body("Category ID is required.");
            }

        } else {

            Optional<EventCategory> catOpt =
                    categoryRepository.findById(dto.getCategoryId());

            if (catOpt.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Category not found.");
            }

            event.setCategory(catOpt.get());
        }


        // VENUE
        if (dto.getVenueId() != null) {

            Optional<Venue> venueOpt =
                    venueRepository.findById(dto.getVenueId());

            if (venueOpt.isPresent()) {
                event.setVenue(venueOpt.get());
            } else {
                return ResponseEntity
                        .badRequest()
                        .body("Venue not found.");
            }
        }


        Event saved = eventRepository.save(event);

        return ResponseEntity.ok(saved);
    }


    // =========================
    // UPDATE EVENT
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequestDto dto) {

        Optional<Event> opt =
                eventRepository.findById(id);

        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = opt.get();

        populateEventFromDto(event, dto);


        // UPDATE CATEGORY
        if (dto.getCategoryId() != null) {

            Optional<EventCategory> catOpt =
                    categoryRepository.findById(dto.getCategoryId());

            if (catOpt.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Category not found.");
            }

            event.setCategory(catOpt.get());
        }


        // UPDATE VENUE
        if (dto.getVenueId() != null) {

            Optional<Venue> venueOpt =
                    venueRepository.findById(dto.getVenueId());

            if (venueOpt.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Venue not found.");
            }

            event.setVenue(venueOpt.get());
        }


        Event saved =
                eventRepository.save(event);

        return ResponseEntity.ok(saved);
    }


    // =========================
    // DELETE EVENT
    // =========================
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteEvent(
            @PathVariable Long id) {

        // Check event exists
        if (!eventRepository.existsById(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }


        // IMPORTANT:
        // Delete schedule items first because
        // schedule_items.event_id references events.id
        scheduleItemRepository.deleteByEventId(id);


        // Now delete the event
        eventRepository.deleteById(id);


        return ResponseEntity
                .noContent()
                .build();
    }


    // =========================
    // POPULATE EVENT
    // =========================
    private void populateEventFromDto(
            Event event,
            EventRequestDto dto) {

        if (dto.getName() != null &&
                !dto.getName().trim().isEmpty()) {

            event.setName(dto.getName());
        }


        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }


        if (dto.getRules() != null) {
            event.setRules(dto.getRules());
        }


        if (dto.getTeamSize() != null) {
            event.setTeamSize(dto.getTeamSize());
        }


        if (dto.getPrizes() != null) {
            event.setPrizes(dto.getPrizes());
        }


        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }


        if (dto.getStartTime() != null) {
            event.setStartTime(dto.getStartTime());
        }


        if (dto.getEndTime() != null) {
            event.setEndTime(dto.getEndTime());
        }


        if (dto.getRegistrationUrl() != null) {
            event.setRegistrationUrl(dto.getRegistrationUrl());
        }


        if (dto.getImageUrl() != null) {
            event.setImageUrl(dto.getImageUrl());
        }


        if (dto.getIsFeatured() != null) {
            event.setIsFeatured(dto.getIsFeatured());
        }


        if (dto.getStatus() != null) {
            event.setStatus(dto.getStatus());
        }


        if (dto.getCoordinatorName() != null) {
            event.setCoordinatorName(
                    dto.getCoordinatorName()
            );
        }


        if (dto.getCoordinatorContact() != null) {
            event.setCoordinatorContact(
                    dto.getCoordinatorContact()
            );
        }
    }
}