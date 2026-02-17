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
	 * Creates and saves a photo entity using the provided file path. If a photo with the same
	 * hash value already exists, it retrieves the existing photo. Otherwise, it parses metadata
	 * from the photo file, creates associated entities (location, camera model, orientation),
	 * and stores the new photo.
	 *
	 * @param photoPath the file system path of the photo to be processed and saved. Must be non-null.
	 * @return the saved or existing {@link Photo} entity.
	 * @throws IOException             if an I/O error occurs while reading the photo file.
	 * @throws MetadataParserException if there is an error while parsing the photo metadata.
	 * @throws GpsResolverException    if there is an error resolving the photo's GPS metadata.
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

			var location = locationService.createAndSaveLocation(metaData);
			var cameraModel = cameraModelService.createAndSaveCameraModel(metaData);
			var orientation = orientationService.createAndSaveOrientation(metaData);

			var photo = new Photo(hashValue, photoPath.toAbsolutePath()
					.toString(), metaData.photoHeight(), metaData.photoWidth(), orientation,
					cameraModel, location, metaData.creationDate());
			log.debug("created new photo {}", photo);

			savedPhoto = repository.saveAndFlush(photo);
			log.info("saved photo {} successfully", savedPhoto);
		}

		return savedPhoto;
	}
}
