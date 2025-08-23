package com.sprints.UniversityRoomBookingSystem.service.BookhingHistory;

import com.sprints.UniversityRoomBookingSystem.dto.response.BookingHistoryResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.*;

import java.util.List;

public interface BookingHistoryService {
    void logCreation(Booking booking, User actor,String reason);
    void logAction(Booking booking, BookingStatus previous, BookingStatus next,BookingAction action,User actor,String reason);
    List<BookingHistoryResponseDTO>getHistoryForBooking(Long id);

}
