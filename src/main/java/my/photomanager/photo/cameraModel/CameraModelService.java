package my.photomanager.photo.cameraModel;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class CameraModelService {

	private final CameraModelRepository repository;

	/**
	 * Saves the given {@link CameraModel} if no camera model with the same name exists,
	 * or returns the existing one.
	 *
	 * @param cameraModel the {@link CameraModel} to save or retrieve
	 * @return the existing or newly saved {@link CameraModel}
	 */
	public CameraModel saveOrGetCameraModel(@NonNull CameraModel cameraModel) {
		return repository.findByName(cameraModel.getName())
				.orElseGet(() -> {
					var savedCameraModel = repository.saveAndFlush(cameraModel);
					log.info("saved camera model with name {} successfully", cameraModel.getName());

					return savedCameraModel;
				});
	}
}
