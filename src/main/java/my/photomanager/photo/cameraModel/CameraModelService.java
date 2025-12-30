package my.photomanager.photo.cameraModel;

import jakarta.persistence.EntityNotFoundException;
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

	protected void addCameraModel(@NonNull String name) {
		var cameraModel = new CameraModel(name);
		log.info("created new camera model {}", cameraModel);

		saveCameraModelIfNotExists(cameraModel);
	}

	protected void editCameraModel(long id, @NonNull String name) {
		var cameraModel = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("no camera model found with id " + id));
		log.info("found camera model {} to edit", cameraModel);

		cameraModel.setName(name);
		log.info("updated camera model {} successfully", cameraModel);
	}

	protected void deleteCameraModel(long id) {
		var cameraModel = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("no camera model found with id " + id));
		log.info("found camera model {} to delete", cameraModel);

		repository.delete(cameraModel);
		log.info("deleted camera model {} successfully", cameraModel);
	}

	private CameraModel saveCameraModelIfNotExists(@NonNull CameraModel cameraModel) {
		return null;
	}


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
