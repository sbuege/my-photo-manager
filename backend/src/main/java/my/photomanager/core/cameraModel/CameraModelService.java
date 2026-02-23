package my.photomanager.core.cameraModel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.utils.metaDataParser.Metadata;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * Creates and saves a new camera model using the camera model name extracted
     * from the provided metadata. If the camera model name exists in the repository,
     * the existing entity is returned. If the name is blank, the operation is skipped,
     * and an empty {@code Optional} is returned.
     *
     * @param metaData the metadata containing the camera model name; must not be null
     * @return an {@code Optional} containing the saved or existing {@code CameraModel}
     *         object, or an empty {@code Optional} if the camera model name is blank
     */
    public Optional<CameraModel> createAndSaveCameraModel(@NonNull Metadata metaData) {
        return createAndSaveCameraModel(metaData.cameraModel());
    }

    /**
     * Creates and saves a new camera model with the specified name.
     * If a camera model with the same name already exists in the repository,
     * it retrieves the existing model. If the provided name is blank, the
     * operation is aborted, and an empty {@code Optional} is returned.
     *
     * @param name the name of the camera model to be created; must not be blank
     * @return an {@code Optional} containing the saved or existing {@code CameraModel}
     *         object, or an empty {@code Optional} if the name is blank
     */
    public Optional<CameraModel> createAndSaveCameraModel(@NonNull String name) {
        if (name.isBlank()) {
            log.warn("camera model name cannot be blank");
            return Optional.empty();
        }

        log.debug("creating new camera model with name {}", name);
        var cameraModel = new CameraModel(name);
        log.info("created new  camera model: {}", cameraModel);

        return Optional.of(saveOrGetCameraModel(cameraModel));
    }

    /**
     * Updates the name of an existing camera model identified by its unique ID.
     * If the provided name is blank, the operation is aborted, and an empty {@code Optional} is returned.
     * If no camera model is found with the specified ID, an exception is thrown.
     *
     * @param id   the unique identifier of the camera model to be updated
     * @param name the new name to assign to the camera model; must not be blank
     * @return an {@code Optional} containing the updated camera model object,
     *         or an empty {@code Optional} if the name is blank
     * @throws CameraModelServiceException if no camera model is found with the given ID
     */
    public Optional<Object> editCameraModel(long id, @NonNull String name) {
        if (name.isBlank()) {
            log.warn("camera model name cannot be blank");
            return Optional.empty();
        }

        var cameraModel = repository.findById(id)
                .orElseThrow(() -> new CameraModelServiceException("no camera model found with id " + id));
        log.debug("found camera model {} to edit", cameraModel);

        log.debug("updating camera model {} with name {}", cameraModel, name);
        cameraModel.setName(name);
        log.info("updated camera model {} successfully", cameraModel);

        return Optional.of(saveOrGetCameraModel(cameraModel));
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

        log.debug("deleting camera model {}", cameraModel);
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
