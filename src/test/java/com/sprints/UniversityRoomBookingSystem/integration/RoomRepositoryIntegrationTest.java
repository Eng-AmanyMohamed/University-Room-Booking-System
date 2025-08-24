package com.sprints.UniversityRoomBookingSystem.integration;

import com.sprints.UniversityRoomBookingSystem.model.Building;
import com.sprints.UniversityRoomBookingSystem.model.Room;
import com.sprints.UniversityRoomBookingSystem.repository.BuildingRepository;
import com.sprints.UniversityRoomBookingSystem.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RoomRepositoryIntegrationTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    private Room room;
    private Building building;

    @BeforeEach
    void setUp() {
        building = new Building();
        building.setBuilding_name("Main Building");
        buildingRepository.save(building);

        room = new Room();
        room.setName("Room A");
        room.setCapacity(50);
        room.setBuilding(building);
        roomRepository.save(room);
    }

    @Test
    void testExistsByNameIgnoreCase_True() {
        boolean exists = roomRepository.existsByNameIgnoreCase("room a"); // different case
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByNameIgnoreCase_False() {
        boolean exists = roomRepository.existsByNameIgnoreCase("Room B");
        assertThat(exists).isFalse();
    }

    @Test
    void testFindById() {
        Room found = roomRepository.findById(room.getRoomId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Room A");
    }

    @Test
    void testSaveRoom() {
        Room newRoom = new Room();
        newRoom.setName("Room B");
        newRoom.setCapacity(30);
        newRoom.setBuilding(building);

        Room saved = roomRepository.save(newRoom);
        assertThat(saved.getRoomId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Room B");
    }

    @Test
    void testDeleteRoom() {
        roomRepository.delete(room);
        assertThat(roomRepository.existsById(room.getRoomId())).isFalse();
    }
}
