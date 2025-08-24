package com.sprints.UniversityRoomBookingSystem.service.Room;

import com.sprints.UniversityRoomBookingSystem.dto.request.RoomCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.*;
import com.sprints.UniversityRoomBookingSystem.modelmapper.RoomMapper;
import com.sprints.UniversityRoomBookingSystem.repository.BuildingRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoomFeatureRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomFeatureRepository roomFeatureRepository;

    @Mock
    private RoomMapper roomMapper;

    @Mock
    private BuildingRepository buildingRepository;

    @InjectMocks
    private RoomService roomService;

    private RoomCreateDTO roomCreateDTO;
    private Room room;
    private RoomResponseDTO roomResponseDTO;
    private Building building;
    private RoomFeature roomFeature;
    private List<String> featureNames;

    @BeforeEach
    void setUp() {
        // Setup test data
        featureNames = Arrays.asList("Projector", "Whiteboard");

        roomCreateDTO = new RoomCreateDTO();
        roomCreateDTO.setName("Test Room");
        roomCreateDTO.setCapacity(50);
        roomCreateDTO.setBuildingId(1L);
        roomCreateDTO.setFeatureNames(featureNames);

        building = new Building();
        building.setBuildingId(1L);
        building.setBuilding_name("Test Building");

        roomFeature = new RoomFeature();
        roomFeature.setFeatureId(1L);
        roomFeature.setFeatureName("Projector");

        room = new Room();
        room.setRoomId(1L);
        room.setName("Test Room");
        room.setCapacity(50);
        room.setBuilding(building);
        room.setFeatures(Arrays.asList(roomFeature));

        roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setRoomId(1L);
        roomResponseDTO.setName("Test Room");
        roomResponseDTO.setCapacity(50);
    }

    @Test
    void createRoom_Success() {
        // Arrange
        when(roomRepository.existsByNameIgnoreCase("Test Room")).thenReturn(false);
        when(buildingRepository.findById(1L)).thenReturn(Optional.of(building));
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase("Projector")).thenReturn(true);
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase("Whiteboard")).thenReturn(true);
        when(roomFeatureRepository.findByFeatureNameIgnoreCase("Projector")).thenReturn(roomFeature);
        when(roomFeatureRepository.findByFeatureNameIgnoreCase("Whiteboard")).thenReturn(roomFeature);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toRoomResponseDTO(any(Room.class))).thenReturn(roomResponseDTO);

        // Act
        RoomResponseDTO result = roomService.createRoom(roomCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(roomResponseDTO.getRoomId(), result.getRoomId());
        assertEquals(roomResponseDTO.getName(), result.getName());
        verify(roomRepository).save(any(Room.class));
        verify(roomMapper).toRoomResponseDTO(any(Room.class));
    }

    @Test
    void createRoom_RoomNameAlreadyExists_ThrowsException() {
        // Arrange
        when(roomRepository.existsByNameIgnoreCase("Test Room")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomService.createRoom(roomCreateDTO));

        assertEquals("A Room with that name already exists", exception.getMessage());
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void createRoom_BuildingNotFound_ThrowsException() {
        // Arrange
        when(roomRepository.existsByNameIgnoreCase("Test Room")).thenReturn(false);
        when(buildingRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomService.createRoom(roomCreateDTO));

        assertEquals("Building Not Found", exception.getMessage());
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void createRoom_RoomFeatureNotFound_ThrowsException() {
        // Arrange
        when(roomRepository.existsByNameIgnoreCase("Test Room")).thenReturn(false);
        when(buildingRepository.findById(1L)).thenReturn(Optional.of(building));
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase("Projector")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomService.createRoom(roomCreateDTO));

        assertEquals("Room Feature Not Found", exception.getMessage());
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void getRoom_Success() {
        // Arrange
        Long roomId = 1L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomMapper.toRoomResponseDTO(room)).thenReturn(roomResponseDTO);

        // Act
        RoomResponseDTO result = roomService.getRoom(roomId);

        // Assert
        assertNotNull(result);
        assertEquals(roomResponseDTO.getRoomId(), result.getRoomId());
        assertEquals(roomResponseDTO.getName(), result.getName());
        verify(roomRepository).findById(roomId);
        verify(roomMapper).toRoomResponseDTO(room);
    }

    @Test
    void getRoom_RoomNotFound_ThrowsException() {
        // Arrange
        Long roomId = 999L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomService.getRoom(roomId));

        assertEquals("Room with id " + roomId + " not found", exception.getMessage());
        verify(roomMapper, never()).toRoomResponseDTO(any(Room.class));
    }

    @Test
    void getRooms_Success() {
        // Arrange
        List<Room> rooms = Arrays.asList(room, room);
        when(roomRepository.findAll()).thenReturn(rooms);
        when(roomMapper.toRoomResponseDTO(any(Room.class))).thenReturn(roomResponseDTO);

        // Act
        List<RoomResponseDTO> result = roomService.getRooms();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roomRepository).findAll();
        verify(roomMapper, times(2)).toRoomResponseDTO(any(Room.class));
    }

    @Test
    void getRooms_EmptyList_ReturnsEmptyList() {
        // Arrange
        when(roomRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<RoomResponseDTO> result = roomService.getRooms();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roomRepository).findAll();
        verify(roomMapper, never()).toRoomResponseDTO(any(Room.class));
    }

    @Test
    void updateRoom_Success() {
        // Arrange
        Long roomId = 1L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(buildingRepository.findById(1L)).thenReturn(Optional.of(building));
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase("Projector")).thenReturn(true);
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase("Whiteboard")).thenReturn(true);
        when(roomFeatureRepository.findByFeatureNameIgnoreCase("Projector")).thenReturn(roomFeature);
        when(roomFeatureRepository.findByFeatureNameIgnoreCase("Whiteboard")).thenReturn(roomFeature);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toRoomResponseDTO(any(Room.class))).thenReturn(roomResponseDTO);

        // Act
        RoomResponseDTO result = roomService.updateRoom(roomId, roomCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(roomResponseDTO.getRoomId(), result.getRoomId());
        verify(roomRepository).findById(roomId);
        verify(roomRepository).save(any(Room.class));
        verify(roomMapper).toRoomResponseDTO(any(Room.class));
    }

    @Test
    void updateRoom_RoomNotFound_ThrowsException() {
        // Arrange
        Long roomId = 999L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomService.updateRoom(roomId, roomCreateDTO));

        assertEquals("Room with id " + roomId + " not found", exception.getMessage());
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void updateRoom_BuildingNotFound_ThrowsException() {
        // Arrange
        Long roomId = 1L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(buildingRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomService.updateRoom(roomId, roomCreateDTO));

        assertEquals("Building Not Found", exception.getMessage());
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void updateRoom_RoomFeatureNotFound_ThrowsException() {
        // Arrange
        Long roomId = 1L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(buildingRepository.findById(1L)).thenReturn(Optional.of(building));
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase("Projector")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomService.updateRoom(roomId, roomCreateDTO));

        assertEquals("Room Feature Not Found", exception.getMessage());
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void deleteRoom_Success() {
        // Arrange
        Long roomId = 1L;
        room.setBookingList(new ArrayList<>());
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // Act
        roomService.deleteRoom(roomId);

        // Assert
        verify(roomRepository).findById(roomId);
        verify(roomRepository).delete(room);
    }

    @Test
    void deleteRoom_RoomNotFound_ThrowsException() {
        // Arrange
        Long roomId = 999L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomService.deleteRoom(roomId));

        assertEquals("Room not Found", exception.getMessage());
        verify(roomRepository, never()).delete(any(Room.class));
    }

    @Test
    void deleteRoom_WithApprovedFutureBooking_ThrowsException() {
        // Arrange
        Long roomId = 1L;

        Booking approvedBooking = new Booking();
        approvedBooking.setBookingId(1L);
        approvedBooking.setStatus(BookingStatus.APPROVED);
        approvedBooking.setEndTime(LocalDateTime.now().plusHours(2)); // Future booking

        List<Booking> bookings = Arrays.asList(approvedBooking);
        room.setBookingList(bookings);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomService.deleteRoom(roomId));

        assertEquals("Booking has already been approved", exception.getMessage());
        verify(roomRepository, never()).delete(any(Room.class));
    }

    @Test
    void deleteRoom_WithApprovedPastBooking_Success() {
        // Arrange
        Long roomId = 1L;

        Booking pastApprovedBooking = new Booking();
        pastApprovedBooking.setBookingId(1L);
        pastApprovedBooking.setStatus(BookingStatus.APPROVED);
        pastApprovedBooking.setEndTime(LocalDateTime.now().minusHours(2)); // Past booking

        List<Booking> bookings = Arrays.asList(pastApprovedBooking);
        room.setBookingList(bookings);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // Act
        roomService.deleteRoom(roomId);

        // Assert
        verify(roomRepository).findById(roomId);
        verify(roomRepository).delete(room);
    }

    @Test
    void deleteRoom_WithPendingBooking_Success() {
        // Arrange
        Long roomId = 1L;

        Booking pendingBooking = new Booking();
        pendingBooking.setBookingId(1L);
        pendingBooking.setStatus(BookingStatus.PENDING);
        pendingBooking.setEndTime(LocalDateTime.now().plusHours(2));

        List<Booking> bookings = Arrays.asList(pendingBooking);
        room.setBookingList(bookings);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // Act
        roomService.deleteRoom(roomId);

        // Assert
        verify(roomRepository).findById(roomId);
        verify(roomRepository).delete(room);
    }

    @Test
    void deleteRoom_WithRejectedBooking_Success() {
        // Arrange
        Long roomId = 1L;

        Booking rejectedBooking = new Booking();
        rejectedBooking.setBookingId(1L);
        rejectedBooking.setStatus(BookingStatus.REJECTED);
        rejectedBooking.setEndTime(LocalDateTime.now().plusHours(2));

        List<Booking> bookings = Arrays.asList(rejectedBooking);
        room.setBookingList(bookings);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // Act
        roomService.deleteRoom(roomId);

        // Assert
        verify(roomRepository).findById(roomId);
        verify(roomRepository).delete(room);
    }
}