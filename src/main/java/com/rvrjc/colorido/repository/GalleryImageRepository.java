package com.rvrjc.colorido.repository;

import com.rvrjc.colorido.entity.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {
    List<GalleryImage> findByCategory(String category);
    List<GalleryImage> findByIsFeaturedTrue();
    List<GalleryImage> findAllByOrderByDisplayOrderAsc();
}
