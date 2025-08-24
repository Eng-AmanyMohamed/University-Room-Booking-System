package com.sprints.UniversityRoomBookingSystem.integration;

import com.sprints.UniversityRoomBookingSystem.model.RoomFeature;
import com.sprints.UniversityRoomBookingSystem.repository.RoomFeatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RoomFeatureRepositoryIntegrationTest {

    @Autowired
    private RoomFeatureRepository roomFeatureRepository;

    private RoomFeature feature;

    @BeforeEach
    void setUp() {
        feature = new RoomFeature();
        feature.setFeatureName("Projector");
        feature.setFeatureDescription("HD Projector for presentations");
        roomFeatureRepository.save(feature);
    }

    @Test
    void testExistsByFeatureNameIgnoreCase_True() {
        boolean exists = roomFeatureRepository.existsByFeatureNameIgnoreCase("projector"); // different case
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByFeatureNameIgnoreCase_False() {
        boolean exists = roomFeatureRepository.existsByFeatureNameIgnoreCase("Whiteboard");
        assertThat(exists).isFalse();
    }

    @Test
    void testFindByFeatureNameIgnoreCase() {
        RoomFeature found = roomFeatureRepository.findByFeatureNameIgnoreCase("PROJECTOR"); // uppercase
        assertThat(found).isNotNull();
        assertThat(found.getFeatureDescription()).isEqualTo("HD Projector for presentations");
    }

    @Test
    void testSaveRoomFeature() {
        RoomFeature newFeature = new RoomFeature();
        newFeature.setFeatureName("Whiteboard");
        newFeature.setFeatureDescription("Large whiteboard");
        RoomFeature saved = roomFeatureRepository.save(newFeature);

        assertThat(saved.getFeatureId()).isNotNull();
        assertThat(saved.getFeatureName()).isEqualTo("Whiteboard");
    }

    @Test
    void testDeleteRoomFeature() {
        roomFeatureRepository.delete(feature);
        assertThat(roomFeatureRepository.existsById(feature.getFeatureId())).isFalse();
    }
}
