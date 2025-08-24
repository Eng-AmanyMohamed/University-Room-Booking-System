package com.sprints.UniversityRoomBookingSystem.service.Booking;

import com.sprints.UniversityRoomBookingSystem.Exception.EntityNotFoundException;
import com.sprints.UniversityRoomBookingSystem.dto.request.BookingCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.BookingResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.*;
import com.sprints.UniversityRoomBookingSystem.repository.BookingHistoryRepository;
import com.sprints.UniversityRoomBookingSystem.repository.BookingRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoomRepository;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingHistoryRepository bookingHistoryRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking booking;
    private Room room;
    private User user;
    private BookingCreateDTO bookingCreateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        room = new Room();
        room.setRoomId(1L);

        user = new User();
        user.setUserId(1L);

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setPurpose("Study Group");
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(2));
        booking.setStatus(BookingStatus.PENDING);
        booking.setRoom(room);
        booking.setUser(user);

        bookingCreateDTO = new BookingCreateDTO();
        bookingCreateDTO.setPurpose("Study Group");
        bookingCreateDTO.setStart_time(booking.getStartTime());
        bookingCreateDTO.setEnd_time(booking.getEndTime());
        bookingCreateDTO.setRoom_id(1L);
        bookingCreateDTO.setUser_id(1L);
    }

    @Test
    void createBooking_ShouldSaveAndReturnDTO() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDTO result = bookingService.createBooking(bookingCreateDTO);

        assertNotNull(result);
        assertEquals("Study Group", result.getPurpose());
        assertEquals(BookingStatus.PENDING, result.getStatus());
        verify(bookingHistoryRepository).save(any(BookingHistory.class));
    }

    @Test
    void createBooking_ShouldThrow_WhenRoomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(bookingCreateDTO));
    }

    @Test
    void createBooking_ShouldThrow_WhenUserNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(bookingCreateDTO));
    }

    @Test
    void getAllBookings_ShouldReturnList() {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(1, result.size());
        assertEquals("Study Group", result.get(0).getPurpose());
    }

    @Test
    void getBookingById_ShouldReturnBooking_WhenFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(1L);

        assertNotNull(result);
        assertEquals("Study Group", result.getPurpose());
    }

    @Test
    void getBookingById_ShouldReturnNull_WhenNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        Booking result = bookingService.getBookingById(1L);

        assertNull(result);
    }

    @Test
    void updateBooking_ShouldUpdateAndReturnDTO_WhenNoOverlap() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.findOverlappingBookingsExcluding(anyLong(), any(), any(), anyLong()))
                .thenReturn(List.of()); // no overlaps
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingCreateDTO updateDTO = new BookingCreateDTO();
        updateDTO.setPurpose("Updated Purpose");
        updateDTO.setStart_time(booking.getStartTime().plusHours(1));
        updateDTO.setEnd_time(booking.getEndTime().plusHours(1));

        BookingResponseDTO result = bookingService.updateBooking(1L, updateDTO);

        assertEquals("Updated Purpose", result.getPurpose());
        verify(bookingHistoryRepository).save(any(BookingHistory.class));
    }

    @Test
    void updateBooking_ShouldThrow_WhenOverlappingExists() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.findOverlappingBookingsExcluding(anyLong(), any(), any(), anyLong()))
                .thenReturn(List.of(new Booking())); // overlap found

        BookingCreateDTO updateDTO = new BookingCreateDTO();
        updateDTO.setPurpose("Conflict");
        updateDTO.setStart_time(booking.getStartTime());
        updateDTO.setEnd_time(booking.getEndTime());

        assertThrows(RuntimeException.class,
                () -> bookingService.updateBooking(1L, updateDTO));
    }

    @Test
    void cancelBooking_ShouldSetStatusCancelled_WhenValid() {
        booking.setStatus(BookingStatus.PENDING);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        bookingService.cancelBooking(1L);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(bookingHistoryRepository).save(any(BookingHistory.class));
    }
}
