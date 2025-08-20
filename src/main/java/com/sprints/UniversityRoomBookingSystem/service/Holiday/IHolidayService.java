package com.sprints.UniversityRoomBookingSystem.service.Holiday;

import com.sprints.UniversityRoomBookingSystem.dto.request.HolidayCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.HolidayResponseDTO;

import java.util.List;

public interface IHolidayService {

    HolidayResponseDTO createHoliday(HolidayCreateDTO holidayCreateDTO);
    List<HolidayResponseDTO> getAllHolidays();
    HolidayResponseDTO getHolidayById(Long id);
    HolidayResponseDTO updateHoliday(Long id, HolidayCreateDTO holidayCreateDto);
    void deleteHoliday(Long id);

}
