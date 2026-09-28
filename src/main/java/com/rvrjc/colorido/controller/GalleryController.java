package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.entity.GalleryImage;
import com.rvrjc.colorido.repository.GalleryImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    @Autowired
    private GalleryImageRepository galleryImageRepository;

    @GetMapping
    public List<GalleryImage> getGalleryImages(@RequestParam(required = false) String category,
                                              @RequestParam(required = false) Boolean featured) {
        if (category != null && !category.equalsIgnoreCase("ALL")) {
            return galleryImageRepository.findByCategory(category.toUpperCase());
        }
        if (Boolean.TRUE.equals(featured)) {
            return galleryImageRepository.findByIsFeaturedTrue();
        }
        return galleryImageRepository.findAllByOrderByDisplayOrderAsc();
    }

    @PostMapping
    public ResponseEntity<GalleryImage> addGalleryImage(@RequestBody GalleryImage image) {
        if (image.getThumbnailUrl() == null || image.getThumbnailUrl().isEmpty()) {
            image.setThumbnailUrl(image.getImageUrl());
        }
        GalleryImage saved = galleryImageRepository.save(image);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GalleryImage> updateGalleryImage(@PathVariable Long id, @RequestBody GalleryImage updated) {
        Optional<GalleryImage> opt = galleryImageRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        GalleryImage img = opt.get();
        if (updated.getTitle() != null && !updated.getTitle().trim().isEmpty()) img.setTitle(updated.getTitle());
        if (updated.getCategory() != null) img.setCategory(updated.getCategory());
        if (updated.getImageUrl() != null && !updated.getImageUrl().trim().isEmpty()) img.setImageUrl(updated.getImageUrl());
        if (updated.getThumbnailUrl() != null) img.setThumbnailUrl(updated.getThumbnailUrl());
        if (updated.getDescription() != null) img.setDescription(updated.getDescription());
        if (updated.getYear() != null) img.setYear(updated.getYear());
        if (updated.getIsFeatured() != null) img.setIsFeatured(updated.getIsFeatured());
        if (updated.getDisplayOrder() != null) img.setDisplayOrder(updated.getDisplayOrder());

        return ResponseEntity.ok(galleryImageRepository.save(img));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGalleryImage(@PathVariable Long id) {
        if (!galleryImageRepository.existsById(id)) return ResponseEntity.notFound().build();
        galleryImageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
