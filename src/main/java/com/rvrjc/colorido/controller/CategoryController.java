package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.entity.EventCategory;
import com.rvrjc.colorido.repository.EventCategoryRepository;
import com.rvrjc.colorido.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private EventCategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;


    // =========================
    // GET ALL CATEGORIES
    // =========================
    @GetMapping
    public List<EventCategory> getAllCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAsc();
    }


    // =========================
    // CREATE CATEGORY
    // =========================
    @PostMapping
    public ResponseEntity<EventCategory> createCategory(
            @RequestBody EventCategory category) {

        if (category.getSlug() == null ||
                category.getSlug().isEmpty()) {

            category.setSlug(
                    category.getName()
                            .toLowerCase()
                            .replaceAll("[^a-z0-9]+", "-")
            );
        }

        EventCategory saved =
                categoryRepository.save(category);

        return ResponseEntity.ok(saved);
    }


    // =========================
    // UPDATE CATEGORY
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<EventCategory> updateCategory(
            @PathVariable Long id,
            @RequestBody EventCategory updated) {

        Optional<EventCategory> opt =
                categoryRepository.findById(id);

        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EventCategory cat = opt.get();


        if (updated.getName() != null &&
                !updated.getName().trim().isEmpty()) {

            cat.setName(updated.getName());
        }


        if (updated.getSlug() != null &&
                !updated.getSlug().trim().isEmpty()) {

            cat.setSlug(updated.getSlug());
        }


        if (updated.getDescription() != null) {
            cat.setDescription(
                    updated.getDescription()
            );
        }


        if (updated.getIcon() != null) {
            cat.setIcon(updated.getIcon());
        }


        if (updated.getDisplayOrder() != null) {
            cat.setDisplayOrder(
                    updated.getDisplayOrder()
            );
        }


        return ResponseEntity.ok(
                categoryRepository.save(cat)
        );
    }


    // =========================
    // DELETE CATEGORY
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id) {

        // Check category exists
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }


        // Check whether any events use this category
        if (eventRepository.existsByCategoryId(id)) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                            "Cannot delete this category because " +
                            "one or more events are assigned to it. " +
                            "Change or delete those events first."
                    );
        }


        // Safe to delete
        categoryRepository.deleteById(id);


        return ResponseEntity
                .noContent()
                .build();
    }
}