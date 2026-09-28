package com.rvrjc.colorido.repository;

import com.rvrjc.colorido.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findByName(String name);
}
