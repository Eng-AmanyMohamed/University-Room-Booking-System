package com.sprints.UniversityRoomBookingSystem.service.Booking;

import com.sprints.UniversityRoomBookingSystem.Exception.DataNotFoundException;
import com.sprints.UniversityRoomBookingSystem.Exception.EntityNotFoundException;
import com.sprints.UniversityRoomBookingSystem.Exception.InvalidRequestException;
import com.sprints.UniversityRoomBookingSystem.Exception.OverlappingException;
import com.sprints.UniversityRoomBookingSystem.dto.request.BookingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BookingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.*;
import com.sprints.UniversityRoomBookingSystem.repository.BookingHistoryRepository;
import com.sprints.UniversityRoomBookingSystem.repository.BookingRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoomRepository;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository; // Inject RoomRepository
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingHistoryRepository bookingHistoryRepository;
    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingCreateDTO bookingCreateDTO) {
        // Create a new Booking entity from the DTO
        Booking booking = new Booking();
        booking.setPurpose(bookingCreateDTO.getPurpose());
        booking.setStartTime(bookingCreateDTO.getStart_time());
        booking.setEndTime(bookingCreateDTO.getEnd_time());
        booking.setStatus(BookingStatus.PENDING);


        // Fetch Room entity by ID

        Room room = roomRepository.findById(bookingCreateDTO.getRoom_id())
                .orElseThrow(() -> new DataNotFoundException("Room not found with ID: " + bookingCreateDTO.getRoom_id()));
        booking.setRoom(room); // Set the fetched Room entity
        // Fetch User entity by ID
        User user = userRepository.findById(bookingCreateDTO.getUser_id())
                .orElseThrow(() -> new DataNotFoundException("User  not found with ID: " + bookingCreateDTO.getUser_id()));
        booking.setUser (user);

        // Save the booking
        Booking savedBooking = bookingRepository.save(booking);

        // Convert to response DTO
        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setBooking_id(savedBooking.getBookingId());
        responseDTO.setPurpose(savedBooking.getPurpose());
        responseDTO.setStart_time(savedBooking.getStartTime());
        responseDTO.setEnd_time(savedBooking.getEndTime());
        responseDTO.setStatus(savedBooking.getStatus());

        saveHistory(savedBooking, null, BookingStatus.PENDING, BookingAction.CREATE, user, "New booking requested");


        return responseDTO;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.orElse(null);
    }
    @Override
    @Transactional
    public BookingResponseDTO updateBooking(Long bookingId, BookingCreateDTO bookingUpdateDTO) {
        // Fetch the existing booking by ID
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Booking not found with ID: " + bookingId));

        // Check if the booking can be updated (e.g., it should not be CANCELLED)
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidRequestException("Booking cannot be updated as it is already cancelled.");
        }


        BookingStatus oldStatus = booking.getStatus();

        // Update booking details
        booking.setPurpose(bookingUpdateDTO.getPurpose());
        booking.setStartTime(bookingUpdateDTO.getStart_time());
        booking.setEndTime(bookingUpdateDTO.getEnd_time());


        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookingsExcluding(
                booking.getRoom().getRoomId(),
                bookingUpdateDTO.getStart_time(),
                bookingUpdateDTO.getEnd_time(),
                bookingId
        );

        if (!overlappingBookings.isEmpty()) {
            throw new OverlappingException("Booking times overlap with an existing booking.");
        }


        Booking updatedBooking = bookingRepository.save(booking);

        saveHistory(updatedBooking, oldStatus, updatedBooking.getStatus(), BookingAction.UPDATE, updatedBooking.getUser(), "Booking updated");

        // Convert to response DTO
        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setBooking_id(updatedBooking.getBookingId());
        responseDTO.setPurpose(updatedBooking.getPurpose());
        responseDTO.setStart_time(updatedBooking.getStartTime());
        responseDTO.setEnd_time(updatedBooking.getEndTime());
        responseDTO.setStatus(updatedBooking.getStatus());



        return responseDTO;
    }


    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Booking not found with ID: " + bookingId));
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.APPROVED) {
            throw new InvalidRequestException("Booking cannot be canceled as it is in status: " + booking.getStatus());
        }
        BookingStatus oldStatus = booking.getStatus();
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        saveHistory(booking, oldStatus, BookingStatus.CANCELLED, BookingAction.CANCEL, booking.getUser(), "Booking cancelled by user");
    }

    private void saveHistory(Booking booking, BookingStatus oldStatus, BookingStatus newStatus, BookingAction action, User changedBy,String reason){
        BookingHistory history=new BookingHistory();
        history.setBooking(booking);
        history.setPreviousStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setAction(action);
        history.setChangedBy(changedBy);
        history.setReason(reason);
        history.setChangedAt(java.time.Instant.now());
        bookingHistoryRepository.save(history);


    }
    public List<Map<String, LocalDateTime>> findAvailableTimeSlots(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> overlappingBookings = bookingRepository.findBookingsForAvailabilityCheck(roomId, startTime, endTime);

        List<Map<String, LocalDateTime>> availableSlots = new ArrayList<>();
        LocalDateTime currentTime = startTime;

        for (Booking booking : overlappingBookings) {
            if (currentTime.isBefore(booking.getStartTime())) {
                Map<String, LocalDateTime> slot = new HashMap<>();
                slot.put("start", currentTime);
                slot.put("end", booking.getStartTime());
                availableSlots.add(slot);
            }

            if (booking.getEndTime().isAfter(currentTime)) {
                currentTime = booking.getEndTime();
            }
        }

        if (currentTime.isBefore(endTime)) {
            Map<String, LocalDateTime> slot = new HashMap<>();
            slot.put("start", currentTime);
            slot.put("end", endTime);
            availableSlots.add(slot);
        }

        return availableSlots;
    }

}
