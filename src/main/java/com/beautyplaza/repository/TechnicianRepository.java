// repository/TechnicianRepository.java
package com.beautyplaza.repository;

import com.beautyplaza.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TechnicianRepository extends JpaRepository<Technician, String> {

    /**
     * Finds a list of Technicians by their availability status.
     * Spring Data JPA automatically generates the query based on the method name.
     * @param isAvailable A boolean indicating whether to search for available (true) or unavailable (false) technicians.
     * @return A list of Technicians matching the availability status.
     */
    List<Technician> findByIsAvailable(Boolean isAvailable);
}