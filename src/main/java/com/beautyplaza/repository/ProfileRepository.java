package com.beautyplaza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.beautyplaza.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // JPA Repository for Profile
}
