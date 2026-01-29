package my.photoLibrary.utils.metaDataParser;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
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
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.util.Strings;

@UtilityClass
@Log4j2
public class MetadataParser {

	/**
	 * Extracts and parses the metadata from the specified photo file.
	 * This includes information such as the image dimensions, creation date,
	 * and camera details. Returns a {@code Metadata} object containing
	 * all available metadata.
	 *
	 * @param photoPath the path to the photo file
	 * @return a {@code Metadata} instance with the extracted metadata
	 */
	public static Metadata parseMetadata(@NonNull Path photoPath) throws MetadataParserException {
		try {
			var imageMetaData = ImageMetadataReader.readMetadata(photoPath.toFile());

			var photoHeight = getPhotoHeightFromMetaData(imageMetaData);
			var photoWidth = getPhotoWidthFromMetaData(imageMetaData);
			var cameraModel = getCameraModelFromMetaData(imageMetaData);
			var creationDate = getCreationDateFromMetaData(imageMetaData);
			var gpdLongitude = getGpsLongitude(imageMetaData);
			var gpdLatitude = getGpsLatitude(imageMetaData);

			var metaData = new Metadata(photoWidth, photoHeight, cameraModel, creationDate, gpdLongitude, gpdLatitude);
			log.info("parsed metadata: {}", metaData);

			return metaData;

		} catch (ImageProcessingException | IOException e) {
			log.error("an exception occurred while reading meta data of {}", photoPath, e);
			throw new MetadataParserException(e);
		}
	}

	private static int getPhotoHeightFromMetaData(@NonNull com.drew.metadata.Metadata metadata) {
		var photoHeight = 0;

		var jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
		if (jpegDirectory != null) {
			photoHeight = jpegDirectory.getInteger(JpegDirectory.TAG_IMAGE_HEIGHT);
		}

		var webpDirectory = metadata.getFirstDirectoryOfType(WebpDirectory.class);
		if (webpDirectory != null) {
			photoHeight = webpDirectory.getInteger(WebpDirectory.TAG_IMAGE_HEIGHT);
		}

		log.debug("photo height: {}", photoHeight);
		return photoHeight;
	}

	private static int getPhotoWidthFromMetaData(@NonNull com.drew.metadata.Metadata metadata) {
		var photoWidth = 0;

		var jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
		if (jpegDirectory != null) {
			photoWidth = jpegDirectory.getInteger(JpegDirectory.TAG_IMAGE_WIDTH);
		}

		var webpDirectory = metadata.getFirstDirectoryOfType(WebpDirectory.class);
		if (webpDirectory != null) {
			photoWidth = webpDirectory.getInteger(WebpDirectory.TAG_IMAGE_WIDTH);
		}

		log.debug("photo width: {}", photoWidth);
		return photoWidth;
	}

	private static String getCameraModelFromMetaData(@NonNull com.drew.metadata.Metadata metadata) {
		String cameraModel = Strings.EMPTY;

		var exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		if (exifIFD0Directory != null) {
			cameraModel = exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL);
		}

		log.debug("camera model: {}", cameraModel);
		return cameraModel;
	}

	private static LocalDate getCreationDateFromMetaData(@NonNull com.drew.metadata.Metadata metadata) {
		LocalDate creationDate = null;

		var exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		if (exifSubIFDDirectory != null) {
			var creationDateText = exifSubIFDDirectory.getDateOriginal()
					.toString();

			try {
				creationDate = DateUtils.parseDate(creationDateText, Locale.ENGLISH, "EEE MMM dd HH:mm:ss zzz yyyy")
						.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate();
			} catch (ParseException e) {
				log.warn("could not parse creation date: {}", creationDateText, e);
			}
		}

		log.debug("creation date: {}", creationDate);
		return creationDate;
	}

	private static double getGpsLongitude(@NonNull com.drew.metadata.Metadata metadata) {
		var gpsLongitude = 0.0;

		var gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
		if (gpsDirectory != null) {
			var gpsGeoLocation = gpsDirectory.getGeoLocation();
			if (gpsGeoLocation != null) {
				gpsLongitude = gpsGeoLocation.getLongitude();
			}
		}

		log.debug("gps longitude: {}", gpsLongitude);
		return gpsLongitude;
	}

	private static double getGpsLatitude(@NonNull com.drew.metadata.Metadata metadata) {
		var gpsLatitude = 0.0d;

		var gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
		if (gpsDirectory != null) {
			var gpsGeoLocation = gpsDirectory.getGeoLocation();
			if (gpsGeoLocation != null) {
				gpsLatitude = gpsGeoLocation.getLatitude();
			}
		}

		log.debug("gps latitude: {}", gpsLatitude);
		return gpsLatitude;
	}
}
