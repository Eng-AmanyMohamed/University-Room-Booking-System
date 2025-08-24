package com.sprints.UniversityRoomBookingSystem.service.BookingService;

import com.sprints.UniversityRoomBookingSystem.dto.response.BookingHistoryResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.*;
import com.sprints.UniversityRoomBookingSystem.modelmapper.BookingHistoryMapper;
import com.sprints.UniversityRoomBookingSystem.repository.BookingHistoryRepository;
import com.sprints.UniversityRoomBookingSystem.service.BookhingHistory.BookingHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingHistoryServiceImplTest {

    @Mock
    private BookingHistoryRepository historyRepository;

    @Mock
    private BookingHistoryMapper historyMapper;

    @InjectMocks
    private BookingHistoryServiceImpl bookingHistoryService;

    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fixedClock = Clock.fixed(Instant.parse("2025-08-24T10:00:00Z"), ZoneOffset.UTC);
        bookingHistoryService = new BookingHistoryServiceImpl(historyRepository, historyMapper, fixedClock);
    }

    @Test
    void logCreation_ShouldSaveHistoryWithCreateAction() {
        // Arrange
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);

        User actor = new User();
        actor.setUserId(1L);

        // Act
        bookingHistoryService.logCreation(booking, actor, "Initial creation");

        // Assert
        ArgumentCaptor<BookingHistory> captor = ArgumentCaptor.forClass(BookingHistory.class);
        verify(historyRepository, times(1)).save(captor.capture());

        BookingHistory saved = captor.getValue();
        assertThat(saved.getAction()).isEqualTo(BookingAction.CREATE);
        assertThat(saved.getNewStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(saved.getChangedBy()).isEqualTo(actor);
        assertThat(saved.getChangedAt()).isEqualTo(Instant.parse("2025-08-24T10:00:00Z"));
        assertThat(saved.getReason()).isEqualTo("Initial creation");
    }

    @Test
    void logAction_ShouldSaveHistoryWithCorrectStatusesAndReason() {
        // Arrange
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);

        User actor = new User();
        actor.setUserId(2L);

        // Act
        bookingHistoryService.logAction(
                booking,
                BookingStatus.PENDING,
                BookingStatus.APPROVED,
                BookingAction.APPROVE,
                actor,
                "Approved by admin"
        );

        // Assert
        ArgumentCaptor<BookingHistory> captor = ArgumentCaptor.forClass(BookingHistory.class);
        verify(historyRepository).save(captor.capture());

        BookingHistory saved = captor.getValue();
        assertThat(saved.getPreviousStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(saved.getNewStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(saved.getAction()).isEqualTo(BookingAction.APPROVE);
        assertThat(saved.getReason()).isEqualTo("Approved by admin");
    }

    @Test
    void getHistoryForBooking_ShouldReturnMappedDTOs() {
        // Arrange
        Long bookingId = 100L;

        Booking booking = new Booking();
        booking.setBookingId(bookingId);

        BookingHistory history = new BookingHistory();
        history.setBooking(booking);

        BookingHistoryResponseDTO dto = new BookingHistoryResponseDTO();

        when(historyRepository.findByBookingBookingIdOrderByChangedAtDesc(bookingId))
                .thenReturn(List.of(history));
        when(historyMapper.toDto(history)).thenReturn(dto);

        // Act
        List<BookingHistoryResponseDTO> result = bookingHistoryService.getHistoryForBooking(bookingId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(dto);
        verify(historyRepository).findByBookingBookingIdOrderByChangedAtDesc(bookingId);
        verify(historyMapper).toDto(history);
    }
}
