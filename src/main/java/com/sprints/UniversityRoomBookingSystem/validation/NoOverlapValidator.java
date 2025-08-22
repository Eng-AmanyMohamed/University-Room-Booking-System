package com.sprints.UniversityRoomBookingSystem.validation;

import com.sprints.UniversityRoomBookingSystem.dto.request.BookingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.model.Booking;
import com.sprints.UniversityRoomBookingSystem.repository.BookingRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NoOverlapValidator implements ConstraintValidator<NoOverlap, BookingCreateDTO> {

    @Autowired
    private BookingRepository bookingRepository; // Inject the BookingRepository

    @Override
    public boolean isValid(BookingCreateDTO bookingCreateDTO, ConstraintValidatorContext context) {
        if (bookingCreateDTO == null ||
                bookingCreateDTO.getRoom_id() == null ||
                bookingCreateDTO.getStart_time() == null ||
                bookingCreateDTO.getEnd_time() == null) {
            return true;

        }
        if (!bookingCreateDTO.getStart_time().isBefore(bookingCreateDTO.getEnd_time())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start time must be before end time")
                    .addPropertyNode("start_time")
                    .addConstraintViolation();
            return false;
        }

        // Fetch overlapping bookings
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                bookingCreateDTO.getRoom_id(),
                bookingCreateDTO.getStart_time(),
                bookingCreateDTO.getEnd_time()
        );

        if (!overlappingBookings.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Time slot conflicts with existing booking(s): ");
            for (int i = 0; i < overlappingBookings.size(); i++) {
                Booking booking = overlappingBookings.get(i);
                errorMessage.append(String.format("ID %d (%s to %s)",
                        booking.getBooking_id(),
                        booking.getStart_time().toString(),
                        booking.getEnd_time().toString()));
                if (i < overlappingBookings.size() - 1) {
                    errorMessage.append(", ");
                }
            }

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage.toString())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    }

