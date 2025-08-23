package com.sprints.UniversityRoomBookingSystem.repository;

import com.sprints.UniversityRoomBookingSystem.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday,Long>{

}
