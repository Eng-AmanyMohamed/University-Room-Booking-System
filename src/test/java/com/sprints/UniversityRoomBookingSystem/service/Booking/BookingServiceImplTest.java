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
import java.util.Map;
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
    @Test
    void findAvailableTimeSlots_ShouldReturnSingleSlot_WhenNoBookings() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);

        when(bookingRepository.findBookingsForAvailabilityCheck(1L, start, end))
                .thenReturn(List.of()); // no bookings

        List<Map<String, LocalDateTime>> result =
                bookingService.findAvailableTimeSlots(1L, start, end);

        assertEquals(1, result.size());
        assertEquals(start, result.get(0).get("start"));
        assertEquals(end, result.get(0).get("end"));
    }

    @Test
    void findAvailableTimeSlots_ShouldReturnGapBeforeBooking() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);

        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.of(2025, 1, 1, 9, 0));
        booking.setEndTime(LocalDateTime.of(2025, 1, 1, 10, 0));

        when(bookingRepository.findBookingsForAvailabilityCheck(1L, start, end))
                .thenReturn(List.of(booking));

        List<Map<String, LocalDateTime>> result =
                bookingService.findAvailableTimeSlots(1L, start, end);

        assertEquals(2, result.size());
        assertEquals(start, result.get(0).get("start")); // slot before booking
        assertEquals(booking.getStartTime(), result.get(0).get("end"));
        assertEquals(booking.getEndTime(), result.get(1).get("start")); // slot after booking
        assertEquals(end, result.get(1).get("end"));
    }

    @Test
    void findAvailableTimeSlots_ShouldReturnGapBetweenTwoBookings() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 14, 0);

        Booking booking1 = new Booking();
        booking1.setStartTime(LocalDateTime.of(2025, 1, 1, 9, 0));
        booking1.setEndTime(LocalDateTime.of(2025, 1, 1, 10, 0));

        Booking booking2 = new Booking();
        booking2.setStartTime(LocalDateTime.of(2025, 1, 1, 11, 0));
        booking2.setEndTime(LocalDateTime.of(2025, 1, 1, 12, 0));

        when(bookingRepository.findBookingsForAvailabilityCheck(1L, start, end))
                .thenReturn(List.of(booking1, booking2));

        List<Map<String, LocalDateTime>> result =
                bookingService.findAvailableTimeSlots(1L, start, end);

        assertEquals(3, result.size());
        assertEquals(start, result.get(0).get("start")); // before booking1
        assertEquals(booking1.getStartTime(), result.get(0).get("end"));
        assertEquals(booking1.getEndTime(), result.get(1).get("start")); // between bookings
        assertEquals(booking2.getStartTime(), result.get(1).get("end"));
        assertEquals(booking2.getEndTime(), result.get(2).get("start")); // after booking2
        assertEquals(end, result.get(2).get("end"));
    }

    @Test
    void findAvailableTimeSlots_ShouldSkipOverlappingBookings() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 14, 0);

        Booking booking1 = new Booking();
        booking1.setStartTime(LocalDateTime.of(2025, 1, 1, 9, 0));
        booking1.setEndTime(LocalDateTime.of(2025, 1, 1, 11, 0));

        Booking booking2 = new Booking();
        booking2.setStartTime(LocalDateTime.of(2025, 1, 1, 10, 30)); // overlaps booking1
        booking2.setEndTime(LocalDateTime.of(2025, 1, 1, 12, 0));

        when(bookingRepository.findBookingsForAvailabilityCheck(1L, start, end))
                .thenReturn(List.of(booking1, booking2));

        List<Map<String, LocalDateTime>> result =
                bookingService.findAvailableTimeSlots(1L, start, end);

        assertEquals(2, result.size());
        assertEquals(start, result.get(0).get("start"));
        assertEquals(booking1.getStartTime(), result.get(0).get("end"));
        assertEquals(booking2.getEndTime(), result.get(1).get("start")); // after merged overlap
        assertEquals(end, result.get(1).get("end"));
    }

    @Test
    void findAvailableTimeSlots_ShouldReturnEmptyList_WhenBookingsCoverEntireRange() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);

        Booking booking = new Booking();
        booking.setStartTime(start);
        booking.setEndTime(end);

        when(bookingRepository.findBookingsForAvailabilityCheck(1L, start, end))
                .thenReturn(List.of(booking));

        List<Map<String, LocalDateTime>> result =
                bookingService.findAvailableTimeSlots(1L, start, end);

        assertTrue(result.isEmpty());
    }

}
