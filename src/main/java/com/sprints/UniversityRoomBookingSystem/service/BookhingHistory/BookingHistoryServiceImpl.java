package com.sprints.UniversityRoomBookingSystem.service.BookhingHistory;


import com.sprints.UniversityRoomBookingSystem.dto.response.BookingHistoryResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.*;
import com.sprints.UniversityRoomBookingSystem.modelmapper.BookingHistoryMapper;
import com.sprints.UniversityRoomBookingSystem.repository.BookingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingHistoryServiceImpl implements BookingHistoryService {

    private final BookingHistoryRepository historyRepository;
    private final BookingHistoryMapper historyMapper;
    private final Clock clock;

    @Override
    public void logCreation(Booking booking, User actor, String reason) {
        logAction(booking, null, booking.getStatus(),
                BookingAction.CREATE, actor, reason);
    }

    @Override
    public void logAction(Booking booking,
                          BookingStatus previous,
                          BookingStatus next,
                          BookingAction action,
                          User actor,
                          String reason) {

        BookingHistory history = BookingHistory.builder()
                .booking(booking)
                .previousStatus(previous)
                .newStatus(next)
                .action(action)
                .changedBy(actor)
                .changedAt(Instant.now(clock))
                .reason(reason)
                .build();

        historyRepository.save(history);
    }

    @Override
    public List<BookingHistoryResponseDTO> getHistoryForBooking(Long bookingId) {
        return historyRepository.findByBookingIdOrderByChangedAtDesc(bookingId)
                .stream()
                .map(historyMapper::toDto)
                .toList();
    }
}
