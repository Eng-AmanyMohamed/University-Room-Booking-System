package com.sprints.UniversityRoomBookingSystem.service.Booking;
import com.sprints.UniversityRoomBookingSystem.dto.request.BookingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BookingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Booking;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
public interface BookingService {
    BookingResponseDTO createBooking(BookingCreateDTO bookingCreateDTO);
    List<Booking> getAllBookings();
    Booking getBookingById(Long bookingId);
    @Transactional
    BookingResponseDTO updateBooking(Long bookingId, BookingCreateDTO bookingUpdateDTO);

    void cancelBooking(Long bookingId);

}
