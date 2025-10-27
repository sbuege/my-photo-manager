package my.photomanager.photo.cameraSettings;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@Import(TestUtils.PhotoIndexerTestConfig.class)
class CameraSettingsRepositoryTest {

	@Autowired
	private CameraSettingsRepository cameraSettingsRepository;

	@Test
	void shouldThrowExceptionWhenSavingCameraSettingsWithDuplicateModelName() {
		// given
		final var cameraModelName = "TestCameraModel";
		var cameraSettings1 = new CameraSettings(cameraModelName);
		var cameraSettings2 = new CameraSettings(cameraModelName);
		cameraSettingsRepository.saveAndFlush(cameraSettings1);

		// when / then
		assertThrows(DataIntegrityViolationException.class, () -> cameraSettingsRepository.saveAndFlush(cameraSettings2));
	}
}