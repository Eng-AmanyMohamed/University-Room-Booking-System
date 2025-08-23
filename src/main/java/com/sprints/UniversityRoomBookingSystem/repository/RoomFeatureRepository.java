package com.sprints.UniversityRoomBookingSystem.repository;

import com.sprints.UniversityRoomBookingSystem.model.RoomFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoomFeatureRepository extends JpaRepository<RoomFeature, Long> {
   boolean existsByFeatureNameIgnoreCase(String featureName);
   RoomFeature findByFeatureNameIgnoreCase(String featureName);



}
