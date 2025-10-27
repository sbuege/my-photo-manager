package my.photomanager.metadata;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.webp.WebpDirectory;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.util.Strings;

@Log4j2
public class PhotoMetadataReader {

	private final static int DEFAULT_WIDTH = 0;
	private final static int DEFAULT_HEIGHT = 0;
	private final static String DEFAULT_CAMERA_MODEL = Strings.EMPTY;
	private final static LocalDate DEFAULT_CREATION_DATE = LocalDate.of(1800, 1, 1);
	private final static double DEFAULT_LONGITUDE = 0.0;
	private final static double DEFAULT_LATITUDE = 0.0;

	/**
	 * Extracts and parses the metadata from the specified photo file.
	 * This includes information such as the image dimensions, creation date,
	 * and camera details. Returns a {@code PhotoMetadata} object containing
	 * all available metadata.
	 *
	 * @param photoPath the path to the photo file
	 * @return a {@code PhotoMetadata} instance with the extracted metadata
	 */
	public static PhotoMetadata readPhotoMetadata(@NonNull Path photoPath) throws PhotoMetadataReaderException {
		log.info("reading photo meta data of {} ", photoPath.toAbsolutePath());

		try {
			var metadata = ImageMetadataReader.readMetadata(photoPath.toFile());

			var photoHeight = getPhotoHeightFromMetaData(metadata);
			var photoWidth = getPhotoWidthFromMetaData(metadata);
			var cameraModel = getCameraModelFromMetaData(metadata);
			var creationDate = getCreationDateFromMetaData(metadata);
			var gpdLongitude = getGpsLongitude(metadata);
			var gpdLatitude = getGpsLatitude(metadata);

			return new PhotoMetadata(photoWidth, photoHeight, cameraModel, creationDate, gpdLongitude, gpdLatitude);

		} catch (ImageProcessingException | IOException e) {
			log.error("an exception occurred while reading photo meta data of {}", photoPath, e);
			throw new PhotoMetadataReaderException(e);
		}
	}

	private static Optional<Integer> getPhotoHeightFromMetaData(@NonNull Metadata metadata) {
		log.debug("get photo height from meta data");

		Optional<Integer> photoHeight = Optional.empty();

		var jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
		if (jpegDirectory != null) {
			photoHeight = Optional.ofNullable(jpegDirectory.getInteger(JpegDirectory.TAG_IMAGE_HEIGHT));
		}

		var webpDirectory = metadata.getFirstDirectoryOfType(WebpDirectory.class);
		if (webpDirectory != null) {
			photoHeight = Optional.ofNullable(webpDirectory.getInteger(WebpDirectory.TAG_IMAGE_HEIGHT));
		}

		photoHeight.ifPresent(height -> log.debug("photo height: {}", height));
		return photoHeight;
	}

	private static Optional<Integer> getPhotoWidthFromMetaData(@NonNull Metadata metadata) {
		log.debug("get photo width from meta data");

		Optional<Integer> photoWidth = Optional.empty();

		var jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
		if (jpegDirectory != null) {
			photoWidth = Optional.ofNullable(jpegDirectory.getInteger(JpegDirectory.TAG_IMAGE_WIDTH));
		}

		var webpDirectory = metadata.getFirstDirectoryOfType(WebpDirectory.class);
		if (webpDirectory != null) {
			photoWidth = Optional.ofNullable(webpDirectory.getInteger(WebpDirectory.TAG_IMAGE_WIDTH));
		}

		photoWidth.ifPresent(width -> log.debug("photo width: {}", width));
		return photoWidth;
	}

	private static Optional<String> getCameraModelFromMetaData(@NonNull Metadata metadata) {
		log.debug("get camera model from meta data");

		Optional<String> cameraModel = Optional.empty();

		var exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		if (exifIFD0Directory != null) {
			cameraModel = Optional.ofNullable(exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL));
		}

		cameraModel.ifPresent(model -> log.debug("camera model: {}", model));
		return cameraModel;
	}

	private static Optional<LocalDate> getCreationDateFromMetaData(@NonNull Metadata metadata) {
		log.debug("get creation date from meta data");

		Optional<LocalDate> creationDate = Optional.empty();

		var exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		if (exifSubIFDDirectory != null) {
			var creationDateText = exifSubIFDDirectory.getDateOriginal()
					.toString();

			try {
				creationDate = Optional.ofNullable(DateUtils.parseDate(creationDateText, Locale.ENGLISH, "EEE MMM dd HH:mm:ss zzz yyyy")
						.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate());
			} catch (ParseException e) {
				log.warn("could not parse creation date: {}", creationDateText, e);
			}
		}

		creationDate.ifPresent(date -> log.debug("creation date: {}", date));
		return creationDate;
	}

	private static Optional<Double> getGpsLongitude(@NonNull Metadata metadata) {
		log.debug("get gps longitude from meta data");

		Optional<Double> gpsLongitude = Optional.empty();

		var gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
		if (gpsDirectory != null) {
			var gpsGeoLocation = gpsDirectory.getGeoLocation();
			if (gpsGeoLocation != null) {
				gpsLongitude = Optional.of(gpsGeoLocation.getLongitude());
			}
		}
		gpsLongitude.ifPresent(longitude -> log.debug("gps longitude: {}", longitude));
		return gpsLongitude;
	}

	private static Optional<Double> getGpsLatitude(@NonNull Metadata metadata) {
		log.debug("get gps latitude from meta data");

		Optional<Double> gpsLatitude = Optional.empty();

		var gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
		if (gpsDirectory != null) {
			var gpsGeoLocation = gpsDirectory.getGeoLocation();
			if (gpsGeoLocation != null) {
				gpsLatitude = Optional.of(gpsGeoLocation.getLatitude());
			}
		}

		gpsLatitude.ifPresent(latitude -> log.debug("gps latitude: {}", latitude));
		return gpsLatitude;
	}
}
