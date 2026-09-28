package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.dto.ResultResponse;
import com.rvrjc.colorido.entity.Result;
import com.rvrjc.colorido.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "http://localhost:5173")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    // =========================
    // GET ALL RESULTS - ADMIN
    // =========================
    @GetMapping
    public ResponseEntity<List<ResultResponse>> getAllResults() {

        List<ResultResponse> results =
                resultService.getAllResults()
                        .stream()
                        .map(ResultResponse::new)
                        .toList();

        return ResponseEntity.ok(results);
    }

    // =========================
    // GET PUBLISHED RESULTS
    // =========================
    @GetMapping("/published")
    public ResponseEntity<List<ResultResponse>> getPublishedResults() {

        List<ResultResponse> results =
                resultService.getPublishedResults()
                        .stream()
                        .map(ResultResponse::new)
                        .toList();

        return ResponseEntity.ok(results);
    }

    // =========================
    // GET RESULTS BY EVENT
    // =========================
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<ResultResponse>> getResultsByEvent(
            @PathVariable Long eventId
    ) {

        List<ResultResponse> results =
                resultService.getResultsByEvent(eventId)
                        .stream()
                        .map(ResultResponse::new)
                        .toList();

        return ResponseEntity.ok(results);
    }

    // =========================
    // GET PUBLISHED RESULTS BY EVENT
    // =========================
    @GetMapping("/event/{eventId}/published")
    public ResponseEntity<List<ResultResponse>> getPublishedResultsByEvent(
            @PathVariable Long eventId
    ) {

        List<ResultResponse> results =
                resultService.getPublishedResultsByEvent(eventId)
                        .stream()
                        .map(ResultResponse::new)
                        .toList();

        return ResponseEntity.ok(results);
    }

    // =========================
    // CREATE RESULT
    // =========================
    @PostMapping("/event/{eventId}")
    public ResponseEntity<ResultResponse> createResult(
            @PathVariable Long eventId,
            @RequestBody Result result
    ) {

        Result savedResult =
                resultService.createResult(result, eventId);

        return ResponseEntity.ok(
                new ResultResponse(savedResult)
        );
    }

    // =========================
    // UPDATE RESULT
    // =========================
    @PutMapping("/{id}/event/{eventId}")
    public ResponseEntity<ResultResponse> updateResult(
            @PathVariable Long id,
            @PathVariable Long eventId,
            @RequestBody Result result
    ) {

        Result updatedResult =
                resultService.updateResult(
                        id,
                        result,
                        eventId
                );

        return ResponseEntity.ok(
                new ResultResponse(updatedResult)
        );
    }

    // =========================
    // DELETE RESULT
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(
            @PathVariable Long id
    ) {

        resultService.deleteResult(id);

        return ResponseEntity.noContent().build();
    }

    // =========================
    // PUBLISH / UNPUBLISH
    // =========================
    @PatchMapping("/{id}/toggle-publish")
    public ResponseEntity<ResultResponse> togglePublish(
            @PathVariable Long id
    ) {

        Result result =
                resultService.togglePublish(id);

        return ResponseEntity.ok(
                new ResultResponse(result)
        );
    }
}