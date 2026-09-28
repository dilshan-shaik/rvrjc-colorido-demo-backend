package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.entity.FestInfo;
import com.rvrjc.colorido.repository.FestInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/fest")
public class FestInfoController {

    @Autowired
    private FestInfoRepository festInfoRepository;

    @GetMapping
    public ResponseEntity<FestInfo> getFestInfo() {
        Optional<FestInfo> festOpt = festInfoRepository.findAll().stream().findFirst();
        return festOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<FestInfo> updateFestInfo(@RequestBody FestInfo updated) {
        Optional<FestInfo> festOpt = festInfoRepository.findAll().stream().findFirst();
        FestInfo fest = festOpt.orElseGet(FestInfo::new);

        if (updated.getFestName() != null && !updated.getFestName().trim().isEmpty()) {
            fest.setFestName(updated.getFestName());
        }
        if (updated.getCollegeName() != null && !updated.getCollegeName().trim().isEmpty()) {
            fest.setCollegeName(updated.getCollegeName());
        }
        if (updated.getCollegeLocation() != null) fest.setCollegeLocation(updated.getCollegeLocation());
        if (updated.getTagline() != null && !updated.getTagline().trim().isEmpty()) {
            fest.setTagline(updated.getTagline());
        }
        if (updated.getStartDate() != null && !updated.getStartDate().trim().isEmpty()) {
            fest.setStartDate(updated.getStartDate());
        }
        if (updated.getEndDate() != null && !updated.getEndDate().trim().isEmpty()) {
            fest.setEndDate(updated.getEndDate());
        }
        if (updated.getEdition() != null) fest.setEdition(updated.getEdition());
        if (updated.getDescription() != null && !updated.getDescription().trim().isEmpty()) {
            fest.setDescription(updated.getDescription());
        }
        if (updated.getHistory() != null && !updated.getHistory().trim().isEmpty()) {
            fest.setHistory(updated.getHistory());
        }
        if (updated.getAboutContent() != null && !updated.getAboutContent().trim().isEmpty()) {
            fest.setAboutContent(updated.getAboutContent());
        }
        if (updated.getImportantNotice() != null && !updated.getImportantNotice().trim().isEmpty()) {
            fest.setImportantNotice(updated.getImportantNotice());
        }
        if (updated.getHeroBannerUrl() != null && !updated.getHeroBannerUrl().trim().isEmpty()) {
            fest.setHeroBannerUrl(updated.getHeroBannerUrl());
        }
        if (updated.getHeroVideoUrl() != null) fest.setHeroVideoUrl(updated.getHeroVideoUrl());
        if (updated.getThemeColor() != null) fest.setThemeColor(updated.getThemeColor());

        FestInfo saved = festInfoRepository.save(fest);
        return ResponseEntity.ok(saved);
    }
}
