package my.photomanager.core.photo;

import static my.photomanager.TestDataBuilder.TestPhotoCreationDate;
import static my.photomanager.TestDataBuilder.TestPhotoFileName;
import static my.photomanager.TestDataBuilder.TestPhotoHashValue;
import static my.photomanager.TestDataBuilder.TestPhotoHeight;
import static my.photomanager.TestDataBuilder.TestPhotoWidth;
import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestDataBuilder;
import my.photomanager.core.cameraModel.CameraModelRepository;
import my.photomanager.core.location.LocationRepository;
import my.photomanager.core.orientation.OrientationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

@DataJpaTest
class PhotoRepositoryTest {

	@Autowired
	private PhotoRepository repository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CameraModelRepository cameraModelRepository;

	@Autowired
	private OrientationRepository orientationRepository;

	@Test
	@DisplayName("should enforce unique hash value constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---

		var location = locationRepository.saveAndFlush(TestDataBuilder.buildLocation());
		var cameraModel = cameraModelRepository.saveAndFlush(TestDataBuilder.buildCameraModel());
		var orientation = orientationRepository.saveAndFlush(TestDataBuilder.buildOrientation());
		var photo1 = TestDataBuilder.buildPhoto(UUID.randomUUID().toString(),TestPhotoHashValue, TestPhotoFileName, TestPhotoHeight, TestPhotoWidth, orientation, cameraModel, location,
				TestPhotoCreationDate);
		var photo2 = TestDataBuilder.buildPhoto(UUID.randomUUID().toString(),TestPhotoHashValue, TestPhotoFileName, TestPhotoHeight, TestPhotoWidth, orientation, cameraModel, location,
				TestPhotoCreationDate);
		repository.saveAndFlush(photo1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(photo2));
	}
}