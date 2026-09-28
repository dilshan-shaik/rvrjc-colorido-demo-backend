package com.rvrjc.colorido.repository;

import com.rvrjc.colorido.entity.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Long> {
    List<ScheduleItem> findByDayNumberOrderByStartTimeAsc(Integer dayNumber);
    List<ScheduleItem> findAllByOrderByDayNumberAscStartTimeAsc();
    void deleteByEventId(Long eventId);
}
