package com.sprints.UniversityRoomBookingSystem.service.RoomFeature;

import com.sprints.UniversityRoomBookingSystem.dto.request.RoomFeatureCreateDTO;
import com.sprints.UniversityRoomBookingSystem.dto.response.RoomFeatureResponseDTO;
import com.sprints.UniversityRoomBookingSystem.model.RoomFeature;
import com.sprints.UniversityRoomBookingSystem.modelmapper.RoomFeatureMapper;
import com.sprints.UniversityRoomBookingSystem.repository.RoomFeatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomFeatureServiceTest {

    @Mock
    private RoomFeatureRepository roomFeatureRepository;

    @Mock
    private RoomFeatureMapper roomFeatureMapper;

    @InjectMocks
    private RoomFeatureService roomFeatureService;

    private RoomFeatureCreateDTO createDTO;
    private RoomFeature roomFeature;
    private RoomFeatureResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        createDTO = new RoomFeatureCreateDTO();
        createDTO.setFeatureName("Projector");
        createDTO.setFeatureDescription("Digital projector for presentations");

        roomFeature = new RoomFeature();
        roomFeature.setFeatureId(1L);
        roomFeature.setFeatureName("Projector");
        roomFeature.setFeatureDescription("Digital projector for presentations");

        responseDTO = new RoomFeatureResponseDTO();
        responseDTO.setFeature_id(1L);
        responseDTO.setFeatureName("Projector");
        responseDTO.setFeatureDescription("Digital projector for presentations");
    }

    @Test
    void createRoomFeature_Success() {
        // Given
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase(createDTO.getFeatureName())).thenReturn(false);
        when(roomFeatureRepository.save(any(RoomFeature.class))).thenReturn(roomFeature);
        when(roomFeatureMapper.toRoomFeatureResponseDTO(any(RoomFeature.class))).thenReturn(responseDTO);

        // When
        RoomFeatureResponseDTO result = roomFeatureService.createRoomFeature(createDTO);

        // Then
        assertNotNull(result);
        assertEquals(responseDTO.getFeature_id(), result.getFeature_id());
        assertEquals(responseDTO.getFeatureName(), result.getFeatureName());
        assertEquals(responseDTO.getFeatureDescription(), result.getFeatureDescription());

        verify(roomFeatureRepository).existsByFeatureNameIgnoreCase(createDTO.getFeatureName());
        verify(roomFeatureRepository).save(any(RoomFeature.class));
        verify(roomFeatureMapper).toRoomFeatureResponseDTO(any(RoomFeature.class));
    }

    @Test
    void createRoomFeature_ThrowsException_WhenFeatureAlreadyExists() {
        // Given
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase(createDTO.getFeatureName())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomFeatureService.createRoomFeature(createDTO));

        assertEquals("Room Feature already exists", exception.getMessage());

        verify(roomFeatureRepository).existsByFeatureNameIgnoreCase(createDTO.getFeatureName());
        verify(roomFeatureRepository, never()).save(any(RoomFeature.class));
        verify(roomFeatureMapper, never()).toRoomFeatureResponseDTO(any(RoomFeature.class));
    }

    @Test
    void createRoomFeature_WithNullFeatureName() {
        // Given
        createDTO.setFeatureName(null);
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase(null)).thenReturn(false);
        when(roomFeatureRepository.save(any(RoomFeature.class))).thenReturn(roomFeature);
        when(roomFeatureMapper.toRoomFeatureResponseDTO(any(RoomFeature.class))).thenReturn(responseDTO);

        // When
        RoomFeatureResponseDTO result = roomFeatureService.createRoomFeature(createDTO);

        // Then
        assertNotNull(result);
        verify(roomFeatureRepository).existsByFeatureNameIgnoreCase(null);
        verify(roomFeatureRepository).save(any(RoomFeature.class));
    }

    @Test
    void getRoomFeatures_Success() {
        // Given
        RoomFeature feature1 = new RoomFeature();
        feature1.setFeatureId(1L);
        feature1.setFeatureName("Projector");

        RoomFeature feature2 = new RoomFeature();
        feature2.setFeatureId(2L);
        feature2.setFeatureName("Whiteboard");

        RoomFeatureResponseDTO response1 = new RoomFeatureResponseDTO();
        response1.setFeature_id(1L);
        response1.setFeatureName("Projector");

        RoomFeatureResponseDTO response2 = new RoomFeatureResponseDTO();
        response2.setFeature_id(2L);
        response2.setFeatureName("Whiteboard");

        List<RoomFeature> features = Arrays.asList(feature1, feature2);

        when(roomFeatureRepository.findAll()).thenReturn(features);
        when(roomFeatureMapper.toRoomFeatureResponseDTO(feature1)).thenReturn(response1);
        when(roomFeatureMapper.toRoomFeatureResponseDTO(feature2)).thenReturn(response2);

        // When
        List<RoomFeatureResponseDTO> result = roomFeatureService.getRoomFeatures();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Projector", result.get(0).getFeatureName());
        assertEquals("Whiteboard", result.get(1).getFeatureName());

        verify(roomFeatureRepository).findAll();
        verify(roomFeatureMapper, times(2)).toRoomFeatureResponseDTO(any(RoomFeature.class));
    }

    @Test
    void getRoomFeatures_EmptyList() {
        // Given
        when(roomFeatureRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<RoomFeatureResponseDTO> result = roomFeatureService.getRoomFeatures();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(roomFeatureRepository).findAll();
        verify(roomFeatureMapper, never()).toRoomFeatureResponseDTO(any(RoomFeature.class));
    }

    @Test
    void updateRoom_Success() {
        // Given
        Long featureId = 1L;
        RoomFeatureCreateDTO updateDTO = new RoomFeatureCreateDTO();
        updateDTO.setFeatureName("Updated Projector");
        updateDTO.setFeatureDescription("Updated description");

        RoomFeature existingFeature = new RoomFeature();
        existingFeature.setFeatureId(featureId);
        existingFeature.setFeatureName("Old Projector");
        existingFeature.setFeatureDescription("Old description");

        RoomFeature updatedFeature = new RoomFeature();
        updatedFeature.setFeatureId(featureId);
        updatedFeature.setFeatureName("Updated Projector");
        updatedFeature.setFeatureDescription("Updated description");

        RoomFeatureResponseDTO updatedResponseDTO = new RoomFeatureResponseDTO();
        updatedResponseDTO.setFeature_id(featureId);
        updatedResponseDTO.setFeatureName("Updated Projector");
        updatedResponseDTO.setFeatureDescription("Updated description");

        when(roomFeatureRepository.findById(featureId)).thenReturn(Optional.of(existingFeature));
        when(roomFeatureRepository.save(any(RoomFeature.class))).thenReturn(updatedFeature);
        when(roomFeatureMapper.toRoomFeatureResponseDTO(any(RoomFeature.class))).thenReturn(updatedResponseDTO);

        // When
        RoomFeatureResponseDTO result = roomFeatureService.updateRoom(featureId, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Updated Projector", result.getFeatureName());
        assertEquals("Updated description", result.getFeatureDescription());

        verify(roomFeatureRepository).findById(featureId);
        verify(roomFeatureRepository).save(existingFeature);
        verify(roomFeatureMapper).toRoomFeatureResponseDTO(existingFeature);

        // Verify that the feature was actually updated
        assertEquals("Updated Projector", existingFeature.getFeatureName());
        assertEquals("Updated description", existingFeature.getFeatureDescription());
    }

    @Test
    void updateRoom_ThrowsException_WhenFeatureNotFound() {
        // Given
        Long nonExistentId = 999L;
        when(roomFeatureRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomFeatureService.updateRoom(nonExistentId, createDTO));

        assertEquals("Room Feature not Found", exception.getMessage());

        verify(roomFeatureRepository).findById(nonExistentId);
        verify(roomFeatureRepository, never()).save(any(RoomFeature.class));
        verify(roomFeatureMapper, never()).toRoomFeatureResponseDTO(any(RoomFeature.class));
    }

    @Test
    void updateRoom_WithNullValues() {
        // Given
        Long featureId = 1L;
        RoomFeatureCreateDTO updateDTO = new RoomFeatureCreateDTO();
        updateDTO.setFeatureName(null);
        updateDTO.setFeatureDescription(null);

        RoomFeature existingFeature = new RoomFeature();
        existingFeature.setFeatureId(featureId);

        when(roomFeatureRepository.findById(featureId)).thenReturn(Optional.of(existingFeature));
        when(roomFeatureRepository.save(any(RoomFeature.class))).thenReturn(existingFeature);
        when(roomFeatureMapper.toRoomFeatureResponseDTO(any(RoomFeature.class))).thenReturn(responseDTO);

        // When
        RoomFeatureResponseDTO result = roomFeatureService.updateRoom(featureId, updateDTO);

        // Then
        assertNotNull(result);
        verify(roomFeatureRepository).findById(featureId);
        verify(roomFeatureRepository).save(existingFeature);
        assertNull(existingFeature.getFeatureName());
        assertNull(existingFeature.getFeatureDescription());
    }

    @Test
    void deleteRoomFeature_Success() {
        // Given
        Long featureId = 1L;
        when(roomFeatureRepository.findById(featureId)).thenReturn(Optional.of(roomFeature));

        // When
        assertDoesNotThrow(() -> roomFeatureService.deleteRoomFeature(featureId));

        // Then
        verify(roomFeatureRepository).findById(featureId);
        verify(roomFeatureRepository).delete(roomFeature);
    }

    @Test
    void deleteRoomFeature_ThrowsException_WhenFeatureNotFound() {
        // Given
        Long nonExistentId = 999L;
        when(roomFeatureRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomFeatureService.deleteRoomFeature(nonExistentId));

        assertEquals("Room Feature not Found", exception.getMessage());

        verify(roomFeatureRepository).findById(nonExistentId);
        verify(roomFeatureRepository, never()).delete(any(RoomFeature.class));
    }

    @Test
    void deleteRoomFeature_WithNullId() {
        // Given
        when(roomFeatureRepository.findById(null)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomFeatureService.deleteRoomFeature(null));

        assertEquals("Room Feature not Found", exception.getMessage());

        verify(roomFeatureRepository).findById(null);
        verify(roomFeatureRepository, never()).delete(any(RoomFeature.class));
    }

    // Additional edge case tests
    @Test
    void createRoomFeature_CaseInsensitiveCheck() {
        // Given
        createDTO.setFeatureName("PROJECTOR");
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase("PROJECTOR")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomFeatureService.createRoomFeature(createDTO));

        assertEquals("Room Feature already exists", exception.getMessage());
        verify(roomFeatureRepository).existsByFeatureNameIgnoreCase("PROJECTOR");
    }

    @Test
    void createRoomFeature_RepositorySaveThrowsException() {
        // Given
        when(roomFeatureRepository.existsByFeatureNameIgnoreCase(createDTO.getFeatureName())).thenReturn(false);
        when(roomFeatureRepository.save(any(RoomFeature.class))).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomFeatureService.createRoomFeature(createDTO));

        assertEquals("Database error", exception.getMessage());
        verify(roomFeatureRepository).save(any(RoomFeature.class));
    }

    @Test
    void getRoomFeatures_RepositoryThrowsException() {
        // Given
        when(roomFeatureRepository.findAll()).thenThrow(new RuntimeException("Database connection error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roomFeatureService.getRoomFeatures());

        assertEquals("Database connection error", exception.getMessage());
        verify(roomFeatureRepository).findAll();
    }
}