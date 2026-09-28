package com.rvrjc.colorido.repository;

import com.rvrjc.colorido.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FestInfoRepository extends JpaRepository<FestInfo, Long> {
}
