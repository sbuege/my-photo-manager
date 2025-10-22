package my.photomanager.photo.cameraSettings;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CameraSettingsServiceTest {

	private CameraSettingsService cameraSettingsService;

	@Mock
	private CameraSettingsRepository cameraSettingsRepository;

	@BeforeEach
	void setUp() {
		cameraSettingsService = new CameraSettingsService(cameraSettingsRepository);
	}

	@Test
	void shouldSaveCameraSettingsWhenNotExisting() {
		// given
		final var cameraModelName = "TestCameraModel";
		var cameraSettings = new CameraSettings(cameraModelName);
		when(cameraSettingsRepository.findByCameraModelName(cameraModelName)).thenReturn(Optional.empty());

		// when
		cameraSettingsService.saveOrGetCameraSettings(cameraSettings);

		// then
		verify(cameraSettingsRepository).saveAndFlush(cameraSettings);
	}

	@Test
	void shouldReturnExistingCameraSettingsWhenAlreadyExists() {
		// given
		final var cameraModelName = "TestCameraModel";
		var cameraSettings = new CameraSettings(cameraModelName);
		when(cameraSettingsRepository.findByCameraModelName(cameraModelName)).thenReturn(Optional.of(cameraSettings));

		// when
		cameraSettingsService.saveOrGetCameraSettings(cameraSettings);

		// then
		verify(cameraSettingsRepository, never()).saveAndFlush(cameraSettings);
	}

	@Test
	void shouldReturnPhotoCameraSettingDTOs() {
		// when
		cameraSettingsService.getCameraSettingsDTOs();

		// then
		verify(cameraSettingsRepository).findAll();
	}
}