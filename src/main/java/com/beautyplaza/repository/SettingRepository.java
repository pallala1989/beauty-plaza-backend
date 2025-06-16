// repository/SettingRepository.java (NEW)
package com.beautyplaza.repository;

import com.beautyplaza.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findBySettingKey(String settingKey);
    List<Setting> findByCategory(String category);
}