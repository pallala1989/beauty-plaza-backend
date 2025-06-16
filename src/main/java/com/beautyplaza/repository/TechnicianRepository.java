// repository/TechnicianRepository.java
package com.beautyplaza.repository;

import com.beautyplaza.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {
}