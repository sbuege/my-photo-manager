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
	public Photo buildPhoto(@NonNull Path photoPath) throws PhotoMetadataReaderException, GeoLocationResolverException, IOException {
		log.info("building photo of {}", photoPath.toAbsolutePath());

		var photoMetadata = PhotoMetadataReader.readPhotoMetadata(photoPath);

		var locationInfo = GeoLocationResolver.resolveLongitudeLatitude(photoMetadata.gpsLongitude(), photoMetadata.gpsLatitude());
		var photoLocation = new PhotoLocation(locationInfo.country(), locationInfo.city());
		photoLocation = photoLocationService.saveOrGetPhotoLocation(photoLocation);

		var cameraSettings = new CameraSettings(photoMetadata.cameraModel());
		cameraSettings = cameraSettingsService.saveOrGetCameraSettings(cameraSettings);

		var hashValue = DigestUtils.md5DigestAsHex(new FileInputStream(photoPath.toFile()));
		var photo = Photo.builder()
				.withHashValue(hashValue)
				.withFileName(photoPath.toAbsolutePath()
						.toString())
				.withHeight(photoMetadata.photoHeight())
				.withWidth(photoMetadata.photoWidth())
				.withCreationDate(photoMetadata.creationDate())
				.withLocation(photoLocation)
				.withCameraSettings(cameraSettings)
				.build();

		log.info("photo : {}", photo);
		return photo;
	}
}
