package my.photomanager.photo.cameraSettings;

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
class CameraSettingsServiceTest {

	// TEST DATA
	final CameraSettings TEST_CAMERA_SETTINGS = TestDataBuilder.TestCameraSettingsBuilder.build();

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
	@DisplayName("should not call saveAndFlush when camera settings already exists")
	void shouldNotCallSaveAndFlush() {
		// given
		when(cameraSettingsRepository.findByCameraModelName(TEST_CAMERA_MODEL_NAME)).thenReturn(Optional.of(TEST_CAMERA_SETTINGS));

		// when
		cameraSettingsService.saveOrGetCameraSettings(TEST_CAMERA_SETTINGS);

		// then
		verify(cameraSettingsRepository, never()).saveAndFlush(TEST_CAMERA_SETTINGS);
	}
}