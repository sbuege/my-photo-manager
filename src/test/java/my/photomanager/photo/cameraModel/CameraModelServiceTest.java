package my.photomanager.photo.cameraModel;

import static my.photomanager.TestDataBuilder.TEST_CAMERA_MODEL_NAME;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CameraModelServiceTest {

	// TEST DATA
	final CameraModel TEST_CAMERA_MODEL = TestDataBuilder.TestCameraModelBuilder.build();

	@Mock
	private CameraModelRepository repository;

	@InjectMocks
	private CameraModelService service;

	@Test
	@DisplayName("should call saveAndFlush when camera model does not exist")
	void shouldCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByName(TEST_CAMERA_MODEL_NAME)).thenReturn(Optional.empty());

		// --- WHEN ---
		service.saveOrGetCameraModel(TEST_CAMERA_MODEL);

		// --- THEN ---
		verify(repository).saveAndFlush(TEST_CAMERA_MODEL);
	}

	@Test
	@DisplayName("should not call saveAndFlush when camera model already exists")
	void shouldNotCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByName(TEST_CAMERA_MODEL_NAME)).thenReturn(Optional.of(TEST_CAMERA_MODEL));

		// --- WHEN ---
		service.saveOrGetCameraModel(TEST_CAMERA_MODEL);

		// --- THEN ---
		verify(repository, never()).saveAndFlush(TEST_CAMERA_MODEL);
	}
}