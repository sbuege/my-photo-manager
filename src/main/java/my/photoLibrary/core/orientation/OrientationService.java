package my.photoLibrary.core.orientation;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photoLibrary.core.location.Location;
import my.photoLibrary.utils.metaDataParser.Metadata;
import org.springframework.stereotype.Service;

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
	 * Creates a new {@code Orientation} instance based on the provided metadata and saves it to
	 * the repository if it does not already exist. Determines the orientation based on the photo's
	 * width and height (e.g., landscape, portrait, or square).
	 *
	 * @param metaData the metadata containing details about the photo such as width and height;
	 *                 must not be null.
	 * @return the saved {@code Orientation} instance from the repository.
	 * @throws OrientationServiceException if the photo's width or height is zero or below.
	 */
	public Orientation createAndSaveOrientation(@NonNull Metadata metaData) {
		var photoWidth = metaData.photoWidth();
		if (photoWidth <= 0) {
			throw new OrientationServiceException("photo width cannot be zero");
		}

		var photoHeight = metaData.photoHeight();
		if (photoHeight <= 0) {
			throw new OrientationServiceException("photo height cannot be zero");
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

		return saveOrGetOrientation(orientation);
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
