package my.photomanager.core.orientation;

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
public class OrientationService {

    private final OrientationRepository repository;

    /**
     * Retrieves a list of all available orientations from the repository.
     *
     * @return a list of all {@code Orientation} entities stored in the repository.
     */
    public List<Orientation> getAllOrientations() {
        return repository.findAll();
    }


    /**
     * Creates and saves an {@code Orientation} entity based on the dimensions provided in the given metadata.
     * The method determines the orientation of a photo (e.g., SQUARE, PORTRAIT, or LANDSCAPE)
     * based on its width and height. If the width or height is invalid (less than or equal to zero),
     * the method logs a warning and returns an empty {@code Optional}.
     * If an orientation matching the calculated name already exists, it retrieves and returns it;
     * otherwise, a new orientation is created, saved, and returned.
     *
     * @param metaData the metadata object containing photo dimensions and other properties; cannot be null.
     *                 The dimensions are evaluated to determine the photo's orientation.
     * @return an {@code Optional} containing the saved or retrieved {@code Orientation} object,
     * or an empty {@code Optional} if the metadata contains invalid dimensions.
     */
    public Optional<Orientation> createAndSaveOrientation(@NonNull Metadata metaData) {
        var photoWidth = metaData.photoWidth();
        if (photoWidth <= 0) {
            log.warn("photo width cannot be zero");
            return Optional.empty();
        }

        var photoHeight = metaData.photoHeight();
        if (photoHeight <= 0) {
            log.warn("photo height cannot be zero");
            return Optional.empty();
        }

        var photoOrientationName = OrientationName.SQUARE;
        if (photoHeight > photoWidth) {
            photoOrientationName = OrientationName.PORTRAIT;
        }
        if (photoWidth > photoHeight) {
            photoOrientationName = OrientationName.LANDSCAPE;
        }

        var orientation = new Orientation(photoOrientationName.getName());
        log.debug("created new orientation {}", orientation);

        return Optional.of(saveOrGetOrientation(orientation));
    }

    private Orientation saveOrGetOrientation(@NonNull Orientation orientation) {
        Orientation savedOrientation;

        if (repository.existsByName(orientation.getName())) {
            savedOrientation = repository.findByName(orientation.getName())
                    .orElseThrow(() -> new OrientationServiceException("no orientation found with name " + orientation.getName()));
            log.debug("orientation {} already exists", savedOrientation);
        } else {
            savedOrientation = repository.saveAndFlush(orientation);
            log.info("saved orientation {} successfully", savedOrientation);
        }

        return savedOrientation;
    }
}
