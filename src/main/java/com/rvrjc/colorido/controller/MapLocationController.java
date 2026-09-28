package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.dto.MapLocationRequestDto;
import com.rvrjc.colorido.entity.MapLocation;
import com.rvrjc.colorido.repository.MapLocationRepository;
import com.rvrjc.colorido.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/map")
public class MapLocationController {

    @Autowired
    private MapLocationRepository mapLocationRepository;

    @Autowired
    private VenueRepository venueRepository;

    @GetMapping
    public List<MapLocation> getMapLocations(@RequestParam(required = false) Boolean all) {
        if (Boolean.TRUE.equals(all)) {
            return mapLocationRepository.findAll();
        }
        return mapLocationRepository.findByIsActiveTrue();
    }

    @PostMapping
    public ResponseEntity<?> createMapLocation(@RequestBody MapLocationRequestDto dto) {
        MapLocation loc = new MapLocation();
        loc.setName(dto.getName());
        loc.setCategory(dto.getCategory() != null ? dto.getCategory() : "VENUE");
        loc.setDescription(dto.getDescription());
        loc.setPosX(dto.getPosX() != null ? dto.getPosX() : 50.0);
        loc.setPosY(dto.getPosY() != null ? dto.getPosY() : 50.0);
        loc.setIcon(dto.getIcon() != null ? dto.getIcon() : "stage");
        loc.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        if (dto.getVenueId() != null) {
            venueRepository.findById(dto.getVenueId()).ifPresent(loc::setVenue);
        }

        MapLocation saved = mapLocationRepository.save(loc);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMapLocation(@PathVariable Long id, @RequestBody MapLocationRequestDto dto) {
        Optional<MapLocation> opt = mapLocationRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        MapLocation loc = opt.get();
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) loc.setName(dto.getName());
        if (dto.getCategory() != null) loc.setCategory(dto.getCategory());
        if (dto.getDescription() != null) loc.setDescription(dto.getDescription());
        if (dto.getPosX() != null) loc.setPosX(dto.getPosX());
        if (dto.getPosY() != null) loc.setPosY(dto.getPosY());
        if (dto.getIcon() != null) loc.setIcon(dto.getIcon());
        if (dto.getIsActive() != null) loc.setIsActive(dto.getIsActive());

        if (dto.getVenueId() != null) {
            venueRepository.findById(dto.getVenueId()).ifPresent(loc::setVenue);
        }

        MapLocation saved = mapLocationRepository.save(loc);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMapLocation(@PathVariable Long id) {
        if (!mapLocationRepository.existsById(id)) return ResponseEntity.notFound().build();
        mapLocationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
