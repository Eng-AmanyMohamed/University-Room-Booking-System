package com.sprints.UniversityRoomBookingSystem.integration;

import com.sprints.UniversityRoomBookingSystem.model.*;
import com.sprints.UniversityRoomBookingSystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BookingHistoryRepositoryIntegrationTest {

    @Autowired
    private BookingHistoryRepository bookingHistoryRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    private Room room;
    private User user;       // Booking user
    private User adminUser;  // changedBy user
    private Booking booking;
    private BookingHistory history1;
    private BookingHistory history2;

    @BeforeEach
    void setUp() {
        // Persist Room
        room = new Room();
        room.setName("Room A");
        room.setCapacity(20);
        room = roomRepository.save(room);

        // Persist Booking user
        user = new User();
        user.setUsername("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("alice123");
        user = userRepository.save(user);

        // Persist Admin user (changedBy)
        adminUser = new User();
        adminUser.setUsername("AdminUser");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("admin123");
        adminUser = userRepository.save(adminUser);

        // Persist Booking
        booking = new Booking();
        booking.setPurpose("Lecture");
        booking.setStartTime(LocalDateTime.of(2025, 9, 10, 10, 0));
        booking.setEndTime(LocalDateTime.of(2025, 9, 12, 10, 0));
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        // Booking histories
        history1 = BookingHistory.builder()
                .booking(booking)
                .previousStatus(BookingStatus.PENDING)
                .newStatus(BookingStatus.APPROVED)
                .action(BookingAction.APPROVE)
                .changedAt(Instant.now().minusSeconds(3600))
                .changedBy(adminUser)
                .reason("Approved")
                .build();

        history2 = BookingHistory.builder()
                .booking(booking)
                .previousStatus(BookingStatus.APPROVED)
                .newStatus(BookingStatus.CANCELLED)
                .action(BookingAction.CANCEL)
                .changedAt(Instant.now())
                .changedBy(adminUser)
                .reason("Cancelled by user")
                .build();

        // Save histories
        bookingHistoryRepository.save(history1);
        bookingHistoryRepository.save(history2);
    }

    @Test
    void testFindByBookingBookingIdOrderByChangedAtDesc() {
        List<BookingHistory> histories = bookingHistoryRepository
                .findByBookingBookingIdOrderByChangedAtDesc(booking.getBookingId());

        assertThat(histories).hasSize(2);
           assertThat(histories.get(0).getNewStatus()).isEqualTo(BookingStatus.CANCELLED);
//        assertThat(histories.get(0).getChangedBy().getUsername()).isEqualTo("AdminUser");
        assertThat(histories.get(1).getNewStatus()).isEqualTo(BookingStatus.APPROVED);
//        assertThat(histories.get(1).getChangedBy().getUsername()).isEqualTo("AdminUser");
    }

    @Test
    void testSaveAndDeleteBookingHistory() {
        BookingHistory newHistory = BookingHistory.builder()
                .booking(booking)
                .previousStatus(BookingStatus.PENDING)
                .newStatus(BookingStatus.REJECTED)
                .action(BookingAction.REJECT)
                .changedAt(Instant.now())
                .changedBy(adminUser)
                .reason("Rejected")
                .build();

        BookingHistory saved = bookingHistoryRepository.save(newHistory);
        assertThat(saved.getHistory_id()).isNotNull();
//        assertThat(saved.getChangedBy().getUsername()).isEqualTo("AdminUser");

        bookingHistoryRepository.delete(saved);
        assertThat(bookingHistoryRepository.findById(saved.getHistory_id())).isEmpty();
    }
}
