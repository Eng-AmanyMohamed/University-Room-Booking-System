package com.sprints.UniversityRoomBookingSystem.service.Booking;

import com.sprints.UniversityRoomBookingSystem.Exception.EntityNotFoundException;
import com.sprints.UniversityRoomBookingSystem.dto.request.BookingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BookingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Booking;
import com.sprints.UniversityRoomBookingSystem.model.BookingStatus;
import com.sprints.UniversityRoomBookingSystem.model.Room;
import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.repository.BookingRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoomRepository;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository; // Inject RoomRepository
    @Autowired
    private UserRepository userRepository;
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
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + bookingCreateDTO.getRoom_id()));
        booking.setRoom(room); // Set the fetched Room entity
        // Fetch User entity by ID
        User user = userRepository.findById(bookingCreateDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User  not found with ID: " + bookingCreateDTO.getUser_id()));
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
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // Check if the booking can be updated (e.g., it should not be CANCELLED)
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking cannot be updated as it is already cancelled.");
        }

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
            throw new RuntimeException("Booking times overlap with an existing booking.");
        }


        Booking updatedBooking = bookingRepository.save(booking);

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
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.APPROVED) {
            throw new EntityNotFoundException("Booking cannot be canceled as it is in status: " + booking.getStatus());
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

}
