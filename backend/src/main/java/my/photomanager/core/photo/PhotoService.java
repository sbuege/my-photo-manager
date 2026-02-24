package my.photomanager.core.photo;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.cameraModel.CameraModelService;
import my.photomanager.core.location.LocationService;
import my.photomanager.core.orientation.OrientationService;
import my.photomanager.utils.gpsResolver.GpsResolverException;
import my.photomanager.utils.metaDataParser.MetadataParser;
import my.photomanager.utils.metaDataParser.MetadataParserException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@RequiredArgsConstructor
@Service
@Log4j2
public class PhotoService {

	private final PhotoRepository repository;
	private final LocationService locationService;
	private final CameraModelService cameraModelService;
	private final OrientationService orientationService;

	/**
	 * Retrieves a {@link Photo} entity by its unique identifier. If no photo is found with the
	 * given ID, an exception is thrown.
	 *
	 * @param id the unique identifier of the photo to be retrieved
	 * @return the {@link Photo} entity associated with the specified ID
	 * @throws java.util.NoSuchElementException if no photo is found with the given ID
	 */
	public Photo findById(long id) {
		return repository.findById(id)
				.orElseThrow(() -> new PhotoServiceException("no photo found with id " + id));
	}

	/**
	 * Retrieves a {@link Photo} entity by its external identifier. If no photo is found
	 * with the given external ID, an exception is thrown.
	 *
	 * @param externalId the unique external identifier of the photo to be retrieved
	 * @return the {@link Photo} entity associated with the specified external ID
	 * @throws PhotoServiceException if no photo is found with the given external ID
	 */
	public Photo findByExternalId(String externalId) {
		return repository.findByExternalId(externalId)
				.orElseThrow(() -> new PhotoServiceException("no photo found with external id " + externalId));

	}


	/**
	 * Creates a new {@link Photo} from the specified file path, extracts metadata, and saves
	 * the photo entity into the repository. If a photo with the same hash value already exists
	 * in the repository, the existing photo entity is returned.
	 *
	 * @param photoPath the absolute file path to the photo to be processed
	 * @return the saved {@link Photo} entity, or the existing {@link Photo} if it already exists
	 * @throws IOException if an I/O error occurs during file processing
	 * @throws MetadataParserException if an error occurs during metadata extraction from the photo
	 * @throws GpsResolverException if an error occurs while resolving GPS-related location metadata
	 */
	public Photo createAndSavePhoto(@NonNull Path photoPath)
			throws IOException, MetadataParserException, GpsResolverException {
		log.info("processing photo {}", photoPath);

		Photo savedPhoto;
		var hashValue = DigestUtils.md5DigestAsHex(new FileInputStream(photoPath.toFile()));

		if (repository.existsByHashValue(hashValue)) {
			savedPhoto = repository.findByHashValue(hashValue)
					.orElseThrow(() -> new PhotoServiceException("no photo found with hash value " + hashValue));
			log.debug("photo {} already exists", savedPhoto);
		} else {
			var metaData = MetadataParser.parseMetadata(photoPath);

			var photo = new Photo(hashValue, photoPath.toAbsolutePath()
					.toString(), metaData.photoHeight(), metaData.photoWidth());

			if (metaData.creationDate() != null){
				photo = photo.toBuilder().withCreationDate(metaData.creationDate()).build();
			}

			var location = locationService.createAndSaveLocation(metaData);
			if (location.isPresent()){
				photo = photo.toBuilder().withLocation(location.get()).build();
			}

			var cameraModel = cameraModelService.createAndSaveCameraModel(metaData);
			if (cameraModel.isPresent()){
				photo = photo.toBuilder().withCameraModel(cameraModel.get()).build();
			}

			var orientation = orientationService.createAndSaveOrientation(metaData);
			if (orientation.isPresent()){
				photo = photo.toBuilder().withOrientation(orientation.get()).build();
			}

			savedPhoto = repository.saveAndFlush(photo);
			log.info("saved photo {} successfully", savedPhoto);
		}

		return savedPhoto;
	}
}
