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

    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId " +
            "AND b.status IN (:PENDING, :APPROVED) " +
            "AND ((b.startTime < :endTime) AND (b.endTime > :startTime))")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId " +
            "AND b.bookingId != :bookingId " +
            "AND b.status IN (:PENDING, :APPROVED) " +            "AND ((b.startTime < :endTime) AND (b.endTime > :startTime))")
    List<Booking> findOverlappingBookingsExcluding(@Param("roomId") Long roomId,
                                                   @Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("bookingId") Long bookingId);

    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId " +
            "AND b.status IN (:PENDING, :APPROVED) " +            "AND b.startTime >= :startDate AND b.endTime <= :endDate " +
            "ORDER BY b.startTime")
    List<Booking> findBookingsInDateRange(@Param("roomId") Long roomId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT b FROM Booking b WHERE b.user.userId = :userId " +
            "AND b.status IN (:PENDING, :APPROVED) " +            "AND b.startTime > :currentTime")
    List<Booking> findCancellableBookingsByUser(@Param("userId") Long userId,
                                                @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId " +
            "AND b.status = 'APPROVED' " +
            "AND b.startTime > :currentTime")
    List<Booking> findFutureApprovedBookingsForRoom(@Param("roomId") Long roomId,
                                                    @Param("currentTime") LocalDateTime currentTime);

    @Modifying
    @Query("UPDATE Booking b SET b.status = 'CANCELLED' WHERE b.bookingId = :bookingId " +
            "AND b.status IN (:PENDING, :APPROVED) " +            "AND b.startTime > :currentTime")
    int cancelBooking(@Param("bookingId") Long bookingId,
                      @Param("currentTime") LocalDateTime currentTime);

    @Modifying
    @Query("UPDATE Booking b SET b.status = :status WHERE b.bookingId = :bookingId")
    int updateBookingStatus(@Param("bookingId") Long bookingId,
                            @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.status = 'PENDING' ORDER BY b.startTime")
    List<Booking> findPendingBookings();

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.userId = :userId " +
            "AND b.startTime >= :startPeriod AND b.startTime <= :endPeriod")
    Long countUserBookingsInPeriod(@Param("userId") Long userId,
                                   @Param("startPeriod") LocalDateTime startPeriod,
                                   @Param("endPeriod") LocalDateTime endPeriod);

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.user " +
            "LEFT JOIN FETCH b.room " +
            "WHERE b.bookingId = :bookingId")
    Optional<Booking> findBookingWithDetails(@Param("bookingId") Long bookingId);


}
