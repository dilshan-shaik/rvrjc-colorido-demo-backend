package com.rvrjc.colorido.repository;

import com.rvrjc.colorido.entity.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    List<EventCategory> findAllByOrderByDisplayOrderAsc();
}
