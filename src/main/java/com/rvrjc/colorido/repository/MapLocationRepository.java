package com.rvrjc.colorido.repository;

import com.rvrjc.colorido.entity.MapLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MapLocationRepository extends JpaRepository<MapLocation, Long> {
    List<MapLocation> findByIsActiveTrue();
    void deleteByVenueId(Long venueId);
}
