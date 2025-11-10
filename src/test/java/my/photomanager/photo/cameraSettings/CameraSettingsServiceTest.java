package my.photomanager.photo.cameraSettings;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CameraSettingsServiceTest {

	// TEST DATA
	final String TEST_CAMERA_MODEL_NAME = "TestCameraModel";
	final CameraSettings TEST_CAMERA_SETTINGS = new CameraSettings(TEST_CAMERA_MODEL_NAME);

	@Mock
	private CameraSettingsRepository cameraSettingsRepository;

	@InjectMocks
	private CameraSettingsService cameraSettingsService;

	@Test
	@DisplayName("should call saveAndFlush when camera settings does not exist")
	void shouldCallSaveAndFlush() {
		// given
		when(cameraSettingsRepository.findByCameraModelName(TEST_CAMERA_MODEL_NAME)).thenReturn(Optional.empty());

		// when
		cameraSettingsService.saveOrGetCameraSettings(TEST_CAMERA_SETTINGS);

		// then
		verify(cameraSettingsRepository).saveAndFlush(TEST_CAMERA_SETTINGS);
	}

	@Test
	@DisplayName("should never all saveAndFlush when camera settings exists already")
	void shouldNeveCallSaveAndFlush() {
		// given
		when(cameraSettingsRepository.findByCameraModelName(TEST_CAMERA_MODEL_NAME)).thenReturn(Optional.of(TEST_CAMERA_SETTINGS));

		// when
		cameraSettingsService.saveOrGetCameraSettings(TEST_CAMERA_SETTINGS);

		// then
		verify(cameraSettingsRepository, never()).saveAndFlush(TEST_CAMERA_SETTINGS);
	}
}