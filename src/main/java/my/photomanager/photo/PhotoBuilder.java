package my.photomanager.photo;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.utils.gpsResolver.GpsResolver;
import my.photomanager.utils.gpsResolver.GpsResolverException;
import my.photomanager.metadata.MetadataParser;
import my.photomanager.metadata.MetadataParserException;
import my.photomanager.photo.cameraModel.CameraModel;
import my.photomanager.photo.cameraModel.CameraModelService;
import my.photomanager.photo.location.Location;
import my.photomanager.photo.location.LocationService;
import my.photomanager.photo.orientation.Orientation;
import my.photomanager.photo.orientation.OrientationName;
import my.photomanager.photo.orientation.OrientationService;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class PhotoBuilder {

	private final LocationService photoLocationService;
	private final CameraModelService cameraModelService;
	private final OrientationService orientationService;

	/**
	 * Builds a {@link Photo} object from the given photo file path by reading metadata,
	 * resolving geographic information, persisting related entities, and computing
	 * an MD5 hash of the file.
	 *
	 * @param photoPath the path to the photo file to be processed; must not be {@code null}
	 * @return a fully initialized {@link Photo} object containing all extracted and linked information
	 */
	public Photo buildPhoto(@NonNull Path photoPath) throws MetadataParserException, GpsResolverException, IOException, PhotoBuilderException {
		log.info("building photo of {}", photoPath.toAbsolutePath());

		var photoMetadata = MetadataParser.parseMetadata(photoPath);

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

		var photoOrientationName = OrientationName.SQUARE;
		if (photoHeight > photoWidth) {
			photoOrientationName = OrientationName.PORTRAIT;
		}
		if (photoWidth > photoHeight) {
			photoOrientationName = OrientationName.LANDSCAPE;
		}
		var orientation = orientationService.saveOrGetOrientation(Orientation.builder()
				.withName(photoOrientationName.getName())
				.build());


		var photo = Photo.builder()
				.withHashValue(hashValue)
				.withFileName(photoPath.toAbsolutePath()
						.toString())
				.withHeight(photoHeight)
				.withWidth(photoWidth)
				.withOrientation(orientation)
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
			var locationInfo = GpsResolver.resolveLongitudeLatitude(photoMetadata.gpsLongitude()
					.get(), photoMetadata.gpsLatitude()
					.get());
			var photoLocation = Location.builder()
					.withCountry(locationInfo.country())
					.withCity(locationInfo.city())
					.build();
			photoLocation = photoLocationService.saveOrGetLocation(photoLocation);

			photo = photo.toBuilder()
					.withLocation(photoLocation)
					.build();
		}

		if (photoMetadata.cameraModel()
				.isPresent()) {
			var cameraSettings = CameraModel.builder()
					.withName(photoMetadata.cameraModel()
							.get())
					.build();
			cameraSettings = cameraModelService.saveOrGetCameraModel(cameraSettings);

			photo = photo.toBuilder()
					.withCameraModel(cameraSettings)
					.build();
		}

		log.info("photo : {}", photo);
		return photo;
	}
}
