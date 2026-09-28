package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.entity.Venue;
import com.rvrjc.colorido.repository.MapLocationRepository;
import com.rvrjc.colorido.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private MapLocationRepository mapLocationRepository;


    // =========================
    // GET ALL VENUES
    // =========================
    @GetMapping
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }


    // =========================
    // CREATE VENUE
    // =========================
    @PostMapping
    public ResponseEntity<Venue> createVenue(
            @RequestBody Venue venue) {

        Venue saved = venueRepository.save(venue);

        return ResponseEntity.ok(saved);
    }


    // =========================
    // UPDATE VENUE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(
            @PathVariable Long id,
            @RequestBody Venue updated) {

        Optional<Venue> opt =
                venueRepository.findById(id);

        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Venue v = opt.get();


        if (updated.getName() != null &&
                !updated.getName().trim().isEmpty()) {

            v.setName(updated.getName());
        }


        if (updated.getCode() != null) {
            v.setCode(updated.getCode());
        }


        if (updated.getDescription() != null) {
            v.setDescription(updated.getDescription());
        }


        if (updated.getCapacity() != null) {
            v.setCapacity(updated.getCapacity());
        }


        if (updated.getLandmark() != null) {
            v.setLandmark(updated.getLandmark());
        }


        if (updated.getMapX() != null) {
            v.setMapX(updated.getMapX());
        }


        if (updated.getMapY() != null) {
            v.setMapY(updated.getMapY());
        }


        return ResponseEntity.ok(
                venueRepository.save(v)
        );
    }


    // =========================
    // DELETE VENUE
    // =========================
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteVenue(
            @PathVariable Long id) {

        if (!venueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }


        // Delete map locations referencing this venue
        mapLocationRepository.deleteByVenueId(id);


        // Now delete the venue
        venueRepository.deleteById(id);


        return ResponseEntity.noContent().build();
    }
}