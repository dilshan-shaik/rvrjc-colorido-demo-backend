package com.rvrjc.colorido.service;

import com.rvrjc.colorido.entity.Event;
import com.rvrjc.colorido.entity.Result;
import com.rvrjc.colorido.repository.EventRepository;
import com.rvrjc.colorido.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final EventRepository eventRepository;

    public ResultService(
            ResultRepository resultRepository,
            EventRepository eventRepository
    ) {
        this.resultRepository = resultRepository;
        this.eventRepository = eventRepository;
    }

    // Get all results - Admin
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    // Get published results - Public
    public List<Result> getPublishedResults() {
        return resultRepository.findByPublishedTrue();
    }

    // Get results for a particular event - Admin
    public List<Result> getResultsByEvent(Long eventId) {
        return resultRepository.findByEventIdOrderByPositionAsc(eventId);
    }

    // Get published results for a particular event - Public
    public List<Result> getPublishedResultsByEvent(Long eventId) {
        return resultRepository
                .findByEventIdAndPublishedTrueOrderByPositionAsc(eventId);
    }

    // Create result
    public Result createResult(Result result, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException("Event not found with id: " + eventId)
                );

        result.setEvent(event);

        return resultRepository.save(result);
    }

    // Update result
    public Result updateResult(Long id, Result updatedResult, Long eventId) {

        Result existingResult = resultRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Result not found with id: " + id)
                );

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException("Event not found with id: " + eventId)
                );

        existingResult.setEvent(event);
        existingResult.setPosition(updatedResult.getPosition());
        existingResult.setParticipantName(updatedResult.getParticipantName());
        existingResult.setTeamName(updatedResult.getTeamName());
        existingResult.setCollegeName(updatedResult.getCollegeName());
        existingResult.setScore(updatedResult.getScore());
        existingResult.setRemarks(updatedResult.getRemarks());
        existingResult.setPublished(updatedResult.isPublished());

        return resultRepository.save(existingResult);
    }

    // Delete result
    public void deleteResult(Long id) {

        if (!resultRepository.existsById(id)) {
            throw new RuntimeException("Result not found with id: " + id);
        }

        resultRepository.deleteById(id);
    }

    // Publish / Unpublish result
    public Result togglePublish(Long id) {

        Result result = resultRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Result not found with id: " + id)
                );

        result.setPublished(!result.isPublished());

        return resultRepository.save(result);
    }
}