package my.photomanager.photo.cameraSettings;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestDataBuilder;
import my.photomanager.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@Import(TestUtils.PhotoIndexerMock.class)
class CameraSettingsRepositoryTest {

	@Autowired
	private CameraSettingsRepository cameraSettingsRepository;

	@Test
	@DisplayName("should enforce unique name constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var cameraSettings1 = TestDataBuilder.TestCameraSettingsBuilder.build();
		var cameraSettings2 = TestDataBuilder.TestCameraSettingsBuilder.build();
		cameraSettingsRepository.saveAndFlush(cameraSettings1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> cameraSettingsRepository.saveAndFlush(cameraSettings2));
	}
}