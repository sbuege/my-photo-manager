package my.photomanager.photo;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import my.photomanager.geoLocationResolver.GeoLocationResolver;
import my.photomanager.geoLocationResolver.GeoLocationResolverException;
import my.photomanager.metadata.PhotoMetadataReader;
import my.photomanager.metadata.PhotoMetadataReaderException;
import my.photomanager.photo.cameraSettings.CameraSettings;
import my.photomanager.photo.cameraSettings.CameraSettingsService;
import my.photomanager.photo.location.PhotoLocation;
import my.photomanager.photo.location.PhotoLocationService;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
@Log4j2
public class PhotoBuilder {

	private final PhotoLocationService photoLocationService;
	private final CameraSettingsService cameraSettingsService;

	protected PhotoBuilder(PhotoLocationService photoLocationService, CameraSettingsService cameraSettingsService) {
		this.cameraSettingsService = cameraSettingsService;
		this.photoLocationService = photoLocationService;
	}

	/**
	 * Builds a {@link Photo} object from the given photo file path by reading metadata,
	 * resolving geographic information, persisting related entities, and computing
	 * an MD5 hash of the file.
	 *
	 * @param photoPath the path to the photo file to be processed; must not be {@code null}
	 * @return a fully initialized {@link Photo} object containing all extracted and linked information
	 */
	public Photo buildPhoto(@NonNull Path photoPath) throws PhotoMetadataReaderException, GeoLocationResolverException, IOException, PhotoBuilderException {
		log.info("building photo of {}", photoPath.toAbsolutePath());

		var photoMetadata = PhotoMetadataReader.readPhotoMetadata(photoPath);

		var hashValue = DigestUtils.md5DigestAsHex(new FileInputStream(photoPath.toFile()));

		var photoHeight = photoMetadata.photoHeight()
				.orElseThrow(() -> new PhotoBuilderException("photo height cannot be empty"));
		if (photoHeight <= 0) {
			throw new PhotoBuilderException("photo height cannot be zero");
		}

		var photoWidth = photoMetadata.photoWidth()
				.orElseThrow(() -> new PhotoBuilderException("photo width cannot be empty"));
		if (photoWidth <= 0) {
			throw new PhotoBuilderException("photo width cannot be zero");
		}

		var photo = Photo.builder()
				.withHashValue(hashValue)
				.withFileName(photoPath.toAbsolutePath()
						.toString())
				.withHeight(photoHeight)
				.withWidth(photoWidth)
				.build();

		if (photoMetadata.creationDate()
				.isPresent()) {
			var creationDate = photoMetadata.creationDate()
					.get();

			photo = photo.toBuilder()
					.withCreationDate(creationDate)
					.build();
		}

		if (photoMetadata.gpsLatitude()
				.isPresent() && photoMetadata.gpsLongitude()
				.isPresent()) {
			var locationInfo = GeoLocationResolver.resolveLongitudeLatitude(photoMetadata.gpsLongitude()
					.get(), photoMetadata.gpsLatitude()
					.get());
			var photoLocation = new PhotoLocation(locationInfo.country(), locationInfo.city());
			photoLocation = photoLocationService.saveOrGetPhotoLocation(photoLocation);

			photo = photo.toBuilder()
					.withLocation(photoLocation)
					.build();
		}

		if (photoMetadata.cameraModel()
				.isPresent()) {
			var cameraSettings = new CameraSettings(photoMetadata.cameraModel()
					.get());
			cameraSettings = cameraSettingsService.saveOrGetCameraSettings(cameraSettings);

			photo = photo.toBuilder()
					.withCameraSettings(cameraSettings)
					.build();
		}

		log.info("photo : {}", photo);
		return photo;
	}
}
