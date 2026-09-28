package com.rvrjc.colorido.repository;

import com.rvrjc.colorido.entity.Result;
import com.rvrjc.colorido.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByEventIdOrderByPositionAsc(Long eventId);

    List<Result> findByPublishedTrue();

    List<Result> findByEventIdAndPublishedTrueOrderByPositionAsc(Long eventId);

    List<Result> findByPosition(Position position);
}