package my.photomanager.photo.cameraModel;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class CameraModelRepositoryTest {

	@Autowired
	private CameraModelRepository repository;

	@Test
	@DisplayName("should enforce unique name constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var cameraModel1 = TestDataBuilder.TestCameraModelBuilder.build();
		var cameraModel2 = TestDataBuilder.TestCameraModelBuilder.build();
		repository.saveAndFlush(cameraModel1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(cameraModel2));
	}
}