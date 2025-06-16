package com.beautyplaza.repository;

import com.beautyplaza.model.BeautyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeautyServiceRepository extends JpaRepository<BeautyService, Long> {
    List<BeautyService> findByIsActiveTrue();
}