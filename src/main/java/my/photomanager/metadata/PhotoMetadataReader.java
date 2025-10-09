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

		var photoMetaData = new PhotoMetadata(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_CAMERA_MODEL, DEFAULT_CREATION_DATE, DEFAULT_LONGITUDE, DEFAULT_LATITUDE);

		try {
			var metadata = ImageMetadataReader.readMetadata(photoPath.toFile());

			var photoHeight = getPhotoHeightFromMetaData(metadata);
			var photoWidth = getPhotoWidthFromMetaData(metadata);
			var cameraModel = getCameraModelFromMetaData(metadata);
			var creationDate = getCreationDateFromMetaData(metadata);
			var gpdLongitude = getGpsLongitude(metadata);
			var gpdLatitude = getGpsLatitude(metadata);
			photoMetaData = new PhotoMetadata(photoWidth, photoHeight, cameraModel, creationDate, gpdLongitude, gpdLatitude);

		} catch (ImageProcessingException | IOException e) {
			log.error("an exception occurred while reading photo meta data of {}", photoPath, e);
			throw new PhotoMetadataReaderException(e);
		}

		return photoMetaData;
	}

	private static int getPhotoHeightFromMetaData(@NonNull Metadata metadata) {
		log.debug("get photo height from meta data");

		var photoHeight = DEFAULT_HEIGHT;

		var jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
		photoHeight = jpegDirectory != null ? jpegDirectory.getInteger(JpegDirectory.TAG_IMAGE_HEIGHT) : photoHeight;

		var webpDirectory = metadata.getFirstDirectoryOfType(WebpDirectory.class);
		photoHeight = webpDirectory != null ? webpDirectory.getInteger(WebpDirectory.TAG_IMAGE_HEIGHT) : photoHeight;

		log.debug("photo height: {}", photoHeight);
		return photoHeight;
	}

	private static int getPhotoWidthFromMetaData(@NonNull Metadata metadata) {
		log.debug("get photo width from meta data");

		var photoWidth = DEFAULT_WIDTH;

		var jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
		photoWidth = jpegDirectory != null ? jpegDirectory.getInteger(JpegDirectory.TAG_IMAGE_WIDTH) : photoWidth;

		var webpDirectory = metadata.getFirstDirectoryOfType(WebpDirectory.class);
		photoWidth = webpDirectory != null ? webpDirectory.getInteger(WebpDirectory.TAG_IMAGE_WIDTH) : photoWidth;

		log.debug("photo width: {}", photoWidth);
		return photoWidth;
	}

	private static String getCameraModelFromMetaData(@NonNull Metadata metadata) {
		log.debug("get camera model from meta data");

		var cameraModel = DEFAULT_CAMERA_MODEL;

		var exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		cameraModel = exifIFD0Directory != null ? exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL) : DEFAULT_CAMERA_MODEL;
		cameraModel = cameraModel == null ? DEFAULT_CAMERA_MODEL : cameraModel;

		log.debug("camera model: {}", cameraModel);
		return cameraModel;
	}

	private static LocalDate getCreationDateFromMetaData(@NonNull Metadata metadata) {
		log.debug("get creation date from meta data");

		var creationDate = DEFAULT_CREATION_DATE;

		var exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		var creationDateText = exifSubIFDDirectory != null ? exifSubIFDDirectory.getDateOriginal()
				.toString() : Strings.EMPTY;
		try {
			creationDate = DateUtils.parseDate(creationDateText, Locale.ENGLISH, "EEE MMM dd HH:mm:ss zzz yyyy")
					.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate();
		} catch (ParseException e) {
			log.warn("could not parse creation date: {}", creationDateText, e);
		}

		log.debug("creation date: {}", creationDate);
		return creationDate;
	}

	private static double getGpsLongitude(@NonNull Metadata metadata) {
		log.debug("get gps longitude from meta data");

		var gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
		var geoLocation = gpsDirectory != null ? gpsDirectory.getGeoLocation() : null;
		var gpsLongitude = geoLocation != null ? geoLocation.getLongitude() : DEFAULT_LONGITUDE;

		log.debug("gps longitude: {}", gpsLongitude);
		return gpsLongitude;
	}

	private static double getGpsLatitude(@NonNull Metadata metadata) {
		log.debug("get gps latitude from meta data");

		var gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
		var geoLocation = gpsDirectory != null ? gpsDirectory.getGeoLocation() : null;
		var gpsLatitude = geoLocation != null ? geoLocation.getLatitude() : DEFAULT_LATITUDE;

		log.debug("gps latitude: {}", gpsLatitude);
		return gpsLatitude;
	}
}
