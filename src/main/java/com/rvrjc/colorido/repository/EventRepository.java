package com.rvrjc.colorido.repository;

import com.rvrjc.colorido.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCategoryId(Long categoryId);

    List<Event> findByIsFeaturedTrue();

    boolean existsByCategoryId(Long categoryId);
}