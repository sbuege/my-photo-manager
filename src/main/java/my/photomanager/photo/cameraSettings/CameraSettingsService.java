package my.photomanager.photo.cameraSettings;

import java.util.Collection;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CameraSettingsService {

	private final CameraSettingsRepository cameraSettingsRepository;

	protected CameraSettingsService(@NonNull CameraSettingsRepository cameraSettingsRepository) {
		this.cameraSettingsRepository = cameraSettingsRepository;
	}

	/**
	 * Saves the given {@link CameraSettings} if no camera settings with the same model name exists,
	 * or returns the existing one.
	 *
	 * @param cameraSettings the {@link CameraSettings} to save or retrieve
	 * @return the existing or newly saved {@link CameraSettings}
	 */
	public CameraSettings saveOrGetCameraSettings(@NonNull CameraSettings cameraSettings) {
		return cameraSettingsRepository.findByCameraModelName(cameraSettings.getCameraModelName())
				.orElseGet(() -> {
					var savedCameraSettings = cameraSettingsRepository.saveAndFlush(cameraSettings);
					log.info("saved camera settings with model name {} successfully", cameraSettings.getCameraModelName());

					return savedCameraSettings;
				});
	}

	public Collection<CameraSettingsDTO> getCameraSettingsDTOs() {
		return cameraSettingsRepository.findAll()
				.stream()
				.map(cameraSettings -> new CameraSettingsDTO(cameraSettings.getId(), cameraSettings.getCameraModelName()))
				.toList();
	}


}
