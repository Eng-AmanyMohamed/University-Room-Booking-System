package com.sprints.UniversityRoomBookingSystem.repository;

import com.sprints.UniversityRoomBookingSystem.model.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface BookingHistoryRepository extends JpaRepository<BookingHistory,Long> {
    List<BookingHistory> findByBookingIdOrderByChangedAtDesc(Long id);
}
