package com.sprints.UniversityRoomBookingSystem.modelmapper;

import com.sprints.UniversityRoomBookingSystem.dto.request.HolidayCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.HolidayResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Holiday;
import org.springframework.stereotype.Component;

@Component
public class HolidayMapper {


    // Convert CreateDTO -> Entity
    public  Holiday toEntity(HolidayCreateDTO holidayCreateDTO) {
        Holiday holiday = new Holiday();
        holiday.setStartDate(holidayCreateDTO.getStartDate());
        holiday.setEndDate(holidayCreateDTO.getEndDate());
        holiday.setDescription(holidayCreateDTO.getDescription());
        return holiday;
    }

    // Convert Entity -> ResponseDTO
    public  HolidayResponseDTO toResponseDTO(Holiday holiday) {
        return new HolidayResponseDTO(
                holiday.getHolidayId(),
                holiday.getStartDate(),
                holiday.getEndDate(),
                holiday.getDescription()
        );
    }
}
