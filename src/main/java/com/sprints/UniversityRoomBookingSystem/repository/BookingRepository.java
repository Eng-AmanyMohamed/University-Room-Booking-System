package com.sprints.UniversityRoomBookingSystem.repository;

import com.sprints.UniversityRoomBookingSystem.model.Booking;
import com.sprints.UniversityRoomBookingSystem.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser_UserId(Long userId);

    List<Booking> findByRoom_RoomId(Long roomId);

    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.room.room_id = :roomId " +
            "AND b.status IN (:PENDING, :APPROVED) " +
            "AND ((b.start_time < :endTime) AND (b.end_time > :startTime))")

    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    @Query("SELECT b FROM Booking b WHERE b.room.room_id = :roomId " +
            "AND b.booking_id != :bookingId " +
            "AND b.status IN (:PENDING, :APPROVED) " +
            "AND ((b.start_time < :endTime) AND (b.end_time > :startTime))")

    List<Booking> findOverlappingBookingsExcluding(@Param("roomId") Long roomId,
                                                   @Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("bookingId") Long bookingId);

    @Query("SELECT b FROM Booking b WHERE b.room.room_id = :roomId " +
            "AND b.status IN (:PENDING, :APPROVED) " +
            "AND b.start_time >= :startDate AND b.end_time <= :endDate " +
            "ORDER BY b.start_time")
    List<Booking> findBookingsInDateRange(@Param("roomId") Long roomId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT b FROM Booking b WHERE b.user.user_id = :userId " +
            "AND b.status IN (:PENDING, :APPROVED) " +
            "AND b.start_time > :currentTime")
    List<Booking> findCancellableBookingsByUser(@Param("userId") Long userId,
                                                @Param("currentTime") LocalDateTime currentTime);


    @Query("SELECT b FROM Booking b WHERE b.room.room_id = :roomId " +
            "AND b.status = 'APPROVED' " +
            "AND b.start_time > :currentTime")
    List<Booking> findFutureApprovedBookingsForRoom(@Param("roomId") Long roomId,
                                                    @Param("currentTime") LocalDateTime currentTime);


    @Modifying
    @Query("UPDATE Booking b SET b.status = 'CANCELLED' WHERE b.booking_id = :bookingId " +
            "AND b.status IN (:PENDING, :APPROVED) " +
            "AND b.start_time > :currentTime")
    int cancelBooking(@Param("bookingId") Long bookingId,
                      @Param("currentTime") LocalDateTime currentTime);

  //APPROVED OR REJECTED
    @Modifying
    @Query("UPDATE Booking b SET b.status = :status WHERE b.booking_id = :bookingId")
    int updateBookingStatus(@Param("bookingId") Long bookingId,
                            @Param("status") BookingStatus status);



    @Query("SELECT b FROM Booking b WHERE b.status = 'PENDING' ORDER BY b.start_time")
    List<Booking> findPendingBookings();


    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.user_id = :userId " +
            "AND b.start_time >= :startPeriod AND b.start_time <= :endPeriod")
    Long countUserBookingsInPeriod(@Param("userId") Long userId,
                                   @Param("startPeriod") LocalDateTime startPeriod,
                                   @Param("endPeriod") LocalDateTime endPeriod);

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.user " +
            "LEFT JOIN FETCH b.room " +
            "WHERE b.booking_id = :bookingId")
    Optional<Booking> findBookingWithDetails(@Param("bookingId") Long bookingId);
}