package com.sprints.UniversityRoomBookingSystem.modelmapper;


import com.sprints.UniversityRoomBookingSystem.dto.response.BookingHistoryResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.BookingHistory;
import org.springframework.stereotype.Component;

@Component
public class BookingHistoryMapper {

    public BookingHistoryResponseDTO toDto(BookingHistory history) {
        return BookingHistoryResponseDTO.builder()
                .previousStatus(history.getPreviousStatus())
                .newStatus(history.getNewStatus())
                .action(history.getAction())
                .changedBy(history.getChangedBy() != null ? history.getChangedBy().getUsername() : null)
                .changedAt(history.getChangedAt())
                .reason(history.getReason())
                .build();
    }
}
