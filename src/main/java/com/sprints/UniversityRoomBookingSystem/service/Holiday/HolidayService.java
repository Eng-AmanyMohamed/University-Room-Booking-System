package com.sprints.UniversityRoomBookingSystem.service.Holiday;

import com.sprints.UniversityRoomBookingSystem.Exception.DataNotFoundException;
import com.sprints.UniversityRoomBookingSystem.dto.request.HolidayCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.HolidayResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.Holiday;
import com.sprints.UniversityRoomBookingSystem.modelmapper.HolidayMapper;
import com.sprints.UniversityRoomBookingSystem.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class HolidayService implements IHolidayService{
    private final HolidayRepository holidayRepository;
    private final HolidayMapper holidayMapper;

    @Autowired
   public HolidayService(HolidayRepository holidayRepository,HolidayMapper holidayMapper)
    {
        this.holidayRepository = holidayRepository;
        this.holidayMapper = holidayMapper;
    }

    @Override
    public HolidayResponseDTO createHoliday(HolidayCreateDTO holidayCreateDTO) {
        Holiday holiday = holidayMapper.toEntity(holidayCreateDTO);
        Holiday saved = holidayRepository.save(holiday);
        return holidayMapper.toResponseDTO(saved);
    }

    @Override
    public List<HolidayResponseDTO> getAllHolidays() {
        return holidayRepository.findAll()
                .stream()
                .map(holidayMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HolidayResponseDTO getHolidayById(Long id) {
        return holidayRepository.findById(id)
                .map(holidayMapper::toResponseDTO)
                .orElseThrow(()->new DataNotFoundException("Holiday not found with id " + id));
    }

    @Override
    public HolidayResponseDTO updateHoliday(Long id, HolidayCreateDTO holidayCreateDTO) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Holiday not found with id: " + id));

        // Update fields
        holiday.setDescription(holidayCreateDTO.getDescription());
        holiday.setStartDate(holidayCreateDTO.getStartDate());
        holiday.setEndDate(holidayCreateDTO.getEndDate());

        Holiday updatedHoliday = holidayRepository.save(holiday);
        return holidayMapper.toResponseDTO(updatedHoliday);

    }

    @Override
    public void deleteHoliday(Long id) {
        if (!holidayRepository.existsById(id)) {
            throw new DataNotFoundException("Holiday not found with id " + id);
        }
        holidayRepository.deleteById(id);
    }

}
