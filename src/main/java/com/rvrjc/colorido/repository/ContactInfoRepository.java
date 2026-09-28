package com.rvrjc.colorido.repository;

import com.rvrjc.colorido.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
    List<ContactInfo> findAllByOrderByDisplayOrderAsc();
    List<ContactInfo> findByTypeOrderByDisplayOrderAsc(String type);
}
