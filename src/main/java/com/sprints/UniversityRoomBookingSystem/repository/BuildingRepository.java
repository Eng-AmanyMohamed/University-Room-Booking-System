package com.sprints.UniversityRoomBookingSystem.repository;

import com.sprints.UniversityRoomBookingSystem.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

        List<Building> findAll();

//    @Query("SELECT b FROM Building b WHERE b.isDeleted = false")
//    List<Building> findAllActive();
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Building b SET b.isDeleted = true WHERE b.id = :id")
//    void softDeleteById(Long id);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Building b SET b.isDeleted = false WHERE b.id = :id")
//    void restoreById(Long id);
}
