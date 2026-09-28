package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.dto.ScheduleItemRequestDto;
import com.rvrjc.colorido.entity.Event;
import com.rvrjc.colorido.entity.ScheduleItem;
import com.rvrjc.colorido.entity.Venue;
import com.rvrjc.colorido.repository.EventRepository;
import com.rvrjc.colorido.repository.ScheduleItemRepository;
import com.rvrjc.colorido.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @GetMapping
    public List<ScheduleItem> getSchedule(@RequestParam(required = false) Integer day) {
        if (day != null) {
            return scheduleItemRepository.findByDayNumberOrderByStartTimeAsc(day);
        }
        return scheduleItemRepository.findAllByOrderByDayNumberAscStartTimeAsc();
    }

    @PostMapping
    public ResponseEntity<?> createScheduleItem(@RequestBody ScheduleItemRequestDto dto) {
        ScheduleItem item = new ScheduleItem();
        item.setTitle(dto.getTitle());
        item.setDayNumber(dto.getDayNumber() != null ? dto.getDayNumber() : 1);
        item.setScheduleDate(dto.getScheduleDate());
        item.setStartTime(dto.getStartTime());
        item.setEndTime(dto.getEndTime());
        item.setDescription(dto.getDescription());
        item.setType(dto.getType() != null ? dto.getType() : "EVENT");

        if (dto.getEventId() != null) {
            eventRepository.findById(dto.getEventId()).ifPresent(item::setEvent);
        }
        if (dto.getVenueId() != null) {
            venueRepository.findById(dto.getVenueId()).ifPresent(item::setVenue);
        }

        ScheduleItem saved = scheduleItemRepository.save(item);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScheduleItem(@PathVariable Long id, @RequestBody ScheduleItemRequestDto dto) {
        Optional<ScheduleItem> opt = scheduleItemRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        ScheduleItem item = opt.get();
        if (dto.getTitle() != null && !dto.getTitle().trim().isEmpty()) {
            item.setTitle(dto.getTitle());
        }
        if (dto.getDayNumber() != null) {
            item.setDayNumber(dto.getDayNumber());
        }
        if (dto.getScheduleDate() != null) {
            item.setScheduleDate(dto.getScheduleDate());
        }
        if (dto.getStartTime() != null) {
            item.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            item.setEndTime(dto.getEndTime());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getType() != null) {
            item.setType(dto.getType());
        }

        if (dto.getEventId() != null) {
            eventRepository.findById(dto.getEventId()).ifPresent(item::setEvent);
        }

        if (dto.getVenueId() != null) {
            venueRepository.findById(dto.getVenueId()).ifPresent(item::setVenue);
        }

        ScheduleItem saved = scheduleItemRepository.save(item);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScheduleItem(@PathVariable Long id) {
        if (!scheduleItemRepository.existsById(id)) return ResponseEntity.notFound().build();
        scheduleItemRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
