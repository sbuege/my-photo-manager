package my.photoLibrary.core.cameraModel;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photoLibrary.utils.metaDataParser.Metadata;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class CameraModelService {

	private final CameraModelRepository repository;

	/**
	 * Retrieves all camera model entities from the repository.
	 *
	 * @return a list of all {@code CameraModel} objects available in the repository.
	 */
	public List<CameraModel> getAllCameraModels() {
		return repository.findAll();
	}

	/**
	 * Creates and saves a new camera model based on the metadata provided.
	 * If a camera model with the same name already exists in the repository, it retrieves the existing model.
	 *
	 * @param metaData the metadata containing details about the camera model
	 * @return the saved or already existing {@code CameraModel} object
	 */
	public CameraModel createAndSaveCameraModel(@NonNull Metadata metaData) {
		return createAndSaveCameraModel(metaData.cameraModel());
	}

	/**
	 * Creates a new {@code CameraModel} entity with the specified name and attempts to save it to the repository.
	 * If a camera model with the same name already exists, the existing one is retrieved instead.
	 *
	 * @param name the name of the camera model to be created and saved; must not be null
	 * @return the saved or retrieved {@code CameraModel} object
	 */
	public CameraModel createAndSaveCameraModel(@NonNull String name) {
		var cameraModel = new CameraModel(name);
		log.debug("created new  camera model: {}", cameraModel);

		return saveOrGetCameraModel(cameraModel);
	}

	/**
	 * Updates the name of an existing camera model identified by the specified ID.
	 * If no camera model is found with the given ID, an exception is thrown.
	 *
	 * @param id the unique identifier of the camera model to be updated
	 * @param name the new name for the camera model; must not be null
	 * @return the updated {@code CameraModel} object
	 * @throws CameraModelServiceException if no camera model is found with the specified ID
	 */
	public CameraModel editCameraModel(long id, @NonNull String name) {
		var cameraModel = repository.findById(id)
				.orElseThrow(() -> new CameraModelServiceException("no camera model found with id " + id));
		log.debug("found camera model {} to edit", cameraModel);

		cameraModel.setName(name);
		log.info("updated camera model {} successfully", cameraModel);

		return saveOrGetCameraModel(cameraModel);
	}


	/**
	 * Deletes an existing camera model identified by the provided ID.
	 * If no camera model is found with the given ID, an exception is thrown.
	 *
	 * @param id the unique identifier of the camera model to be deleted
	 * @throws CameraModelServiceException if no camera model is found with the specified ID
	 */
	public void deleteCameraModel(long id) {
		var cameraModel = repository.findById(id)
				.orElseThrow(() -> new CameraModelServiceException("no camera model found with id " + id));
		log.debug("found camera model {} to delete", cameraModel);

		repository.deleteById(id);
		log.info("deleted camera model {} successfully", cameraModel);
	}

	private CameraModel saveOrGetCameraModel(@NonNull CameraModel cameraModel) {
		CameraModel savedCameraModel;

		if (repository.existsByName(cameraModel.getName())) {
			savedCameraModel = repository.findByName(cameraModel.getName())
					.orElseThrow(() -> new CameraModelServiceException("no camera model found with name " + cameraModel.getName()));
			log.debug("cameraModel {} already exists", savedCameraModel);
		} else {
			savedCameraModel = repository.saveAndFlush(cameraModel);
			log.info("saved cameraModel {} successfully", savedCameraModel);
		}

		return savedCameraModel;
	}
}
