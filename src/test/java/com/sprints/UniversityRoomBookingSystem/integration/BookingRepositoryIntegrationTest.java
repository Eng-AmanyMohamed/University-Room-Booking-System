package com.sprints.UniversityRoomBookingSystem.integration;

import com.sprints.UniversityRoomBookingSystem.model.Booking;
import com.sprints.UniversityRoomBookingSystem.model.BookingStatus;
import com.sprints.UniversityRoomBookingSystem.model.Room;
import com.sprints.UniversityRoomBookingSystem.model.User;
import com.sprints.UniversityRoomBookingSystem.repository.BookingRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoomRepository;
import com.sprints.UniversityRoomBookingSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookingRepositoryIntegrationTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    private Room room;
    private User user;
    private Booking booking;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setName("Room A");
        room.setCapacity(20);
        roomRepository.save(room);

        user = new User();
        user.setUsername("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("alice123");
        userRepository.save(user);

        booking = new Booking();
        booking.setPurpose("Lecture");
        booking.setStartTime(LocalDateTime.of(2025, 9, 10, 10, 0));
        booking.setEndTime(LocalDateTime.of(2025, 9, 12, 10, 0));
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);
    }

    @Test
    void testFindByUserId() {
        List<Booking> bookings = bookingRepository.findByUser_UserId(user.getUserId());
        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getPurpose()).isEqualTo("Lecture");
    }

    @Test
    void testFindOverlappingBookings() {
        LocalDateTime overlapStart = LocalDateTime.of(2025, 9, 11, 9, 0);
        LocalDateTime overlapEnd = LocalDateTime.of(2025, 9, 11, 11, 0);

        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                room.getRoomId(), overlapStart, overlapEnd
        );

        assertThat(overlapping).hasSize(1);
        assertThat(overlapping.get(0).getBookingId()).isEqualTo(booking.getBookingId());
    }

}
