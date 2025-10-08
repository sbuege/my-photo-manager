package my.photomanager.photo.cameraSettings;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class CameraSettingsRepositoryTest {

	@Autowired
	private CameraSettingsRepository cameraSettingsRepository;

	@Test
	void shouldThrowExceptionWhenSavingCameraSettingsWithDuplicateModelName() {
		// given
		final var cameraModelName = "TestCameraModel";
		var cameraSettings1 = new CameraSettings(cameraModelName);
		cameraSettingsRepository.saveAndFlush(cameraSettings1);

		// when
		var cameraSettings2 = new CameraSettings(cameraModelName);
		assertThrows(DataIntegrityViolationException.class, () -> cameraSettingsRepository.saveAndFlush(cameraSettings2));
	}
}