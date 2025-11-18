package my.photomanager;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import my.photomanager.metadata.PhotoMetadata;
import my.photomanager.photo.Photo;
import my.photomanager.photo.album.PhotoAlbum;
import my.photomanager.photo.cameraSettings.CameraSettings;
import my.photomanager.photo.category.PhotoCategory;
import my.photomanager.photo.location.PhotoLocation;

public class TestDataBuilder {

	public static Path TestFilePath = Path.of("src", "test", "resources", "Testdata");

	// example 001
	public static Path EXAMPLE_001_PATH = TestFilePath.resolve("example_001.jpg");
	public static Integer EXAMPLE_001_HEIGHT = 750;
	public static Integer EXAMPLE_001_WIDTH = 1000;
	public static LocalDate EXAMPLE_001_CREATION_DATE = LocalDate.of(2024, 1, 1);
	public static String EXAMPLE_001_CAMERA_MODEL = "iPhone 14 Pro";
	public static Double EXAMPLE_001_LONGITUDE = -80.128525;
	public static Double EXAMPLE_001_LATITUDE = 25.7862;
	public static String EXAMPLE_001_COUNTRY = "Vereinigte Staaten von Amerika";
	public static String EXAMPLE_001_CITY = "Miami Beach";

	// example 002
	public static Path EXAMPLE_002_PATH = TestFilePath.resolve("example_002.jpg");
	public static Integer EXAMPLE_002_HEIGHT = 693;
	public static Integer EXAMPLE_002_WIDTH = 1000;
	public static LocalDate EXAMPLE_002_CREATION_DATE = LocalDate.of(2024, 1, 1);
	public static String EXAMPLE_002_CAMERA_MODEL = "Canon EOS-1D Mark IV";
	public static Double EXAMPLE_002_LONGITUDE = 13.376194444444446;
	public static Double EXAMPLE_002_LATITUDE = 52.518680555555555;
	public static String EXAMPLE_002_COUNTRY = "Deutschland";
	public static String EXAMPLE_002_CITY = "Berlin";

	// example 003
	public static Path EXAMPLE_003_PATH = TestFilePath.resolve("example_003.jpg");
	public static Integer EXAMPLE_003_HEIGHT = 750;
	public static Integer EXAMPLE_003_WIDTH = 1000;
	public static LocalDate EXAMPLE_003_CREATION_DATE = LocalDate.of(2025, 1, 1);
	public static String EXAMPLE_003_CAMERA_MODEL = "iPhone 14 Pro";
	public static Double EXAMPLE_003_LONGITUDE = -80.128525;
	public static Double EXAMPLE_003_LATITUDE = 25.7862;
	public static String EXAMPLE_003_COUNTRY = "Vereinigte Staaten von Amerika";
	public static String EXAMPLE_003_CITY = "Miami Beach";

	// example 004
	public static Path EXAMPLE_004_PATH = TestFilePath.resolve("example_004.webp");
	public static Integer EXAMPLE_004_HEIGHT = 768;
	public static Integer EXAMPLE_004_WIDTH = 1024;
	public static LocalDate EXAMPLE_004_CREATION_DATE = LocalDate.of(2025, 1, 1);
	public static String EXAMPLE_004_CAMERA_MODEL = "iPhone 14 Pro";
	public static Double EXAMPLE_004_LONGITUDE = 13.376194444444446;
	public static Double EXAMPLE_004_LATITUDE = 52.518680555555555;
	public static String EXAMPLE_004_COUNTRY = "Deutschland";
	public static String EXAMPLE_004_CITY = "Berlin";

	public static final String TEST_PHOTO_ALBUM_NAME = "TestPhotoAlbum";
	public static final String TEST_CAMERA_MODEL_NAME = "TestCameraModel";
	public static final String TEST_PHOTO_CATEGORY_NAME = "TestPhotoCategory";
	public static final String TEST_PHOTO_LOCATION_COUNTRY = "TestPhotoLocationCountry";
	public static final String TEST_PHOTO_LOCATION_CITY = "TestPhotoLocationCity";
	public static final String TEST_PHOTO_HASH_VALUE = "TestPhotoHashValue";
	public static final String TEST_PHOTO_FILE_NAME = "TestPhotoFileName";
	public static final Integer TEST_PHOTO_WIDTH = 1000;
	public static final Integer TEST_PHOTO_HEIGHT = 750;
	public static final LocalDate TEST_PHOTO_CREATION_DATE = LocalDate.of(2024, 1, 1);
	public static final Double TEST_PHOTO_LONGITUDE = -80.128525;
	public static final Double TEST_PHOTO_LATITUDE = 25.7862;

	public static class TestPhotoAlbumBuilder {

		public static PhotoAlbum build() {
			return build(TEST_PHOTO_ALBUM_NAME);
		}

		public static PhotoAlbum build(String name) {
			return PhotoAlbum.builder()
					.withName(name)
					.build();
		}
	}

	public static class TestCameraSettingsBuilder {

		public static CameraSettings build() {
			return build(TEST_CAMERA_MODEL_NAME);
		}

		public static CameraSettings build(String modelName) {
			return CameraSettings.builder()
					.withCameraModelName(modelName)
					.build();
		}
	}

	public static class TestPhotoCategoryBuilder {

		public static PhotoCategory build() {
			return build(TEST_PHOTO_CATEGORY_NAME);
		}

		public static PhotoCategory build(String name) {
			return PhotoCategory.builder()
					.withName(name)
					.build();
		}
	}

	public static class TestPhotoLocationBuilder {

		public static PhotoLocation build() {
			return build(TEST_PHOTO_LOCATION_COUNTRY, TEST_PHOTO_LOCATION_CITY);
		}

		public static PhotoLocation build(String country, String city) {
			return PhotoLocation.builder()
					.withCountry(country)
					.withCity(city)
					.build();
		}
	}

	public static class TestPhotoBuilder {

		public static Photo build() {
			return build(TEST_PHOTO_HASH_VALUE, TEST_PHOTO_FILE_NAME, TEST_PHOTO_HEIGHT, TEST_PHOTO_WIDTH, TEST_PHOTO_CREATION_DATE);
		}

		public static Photo build(String hashValue, String fileName, int height, int width, LocalDate creationDate) {
			return Photo.builder()
					.withHashValue(hashValue)
					.withFileName(fileName)
					.withWidth(TEST_PHOTO_WIDTH)
					.withHeight(TEST_PHOTO_HEIGHT)
					.withCreationDate(TEST_PHOTO_CREATION_DATE)
					.build();
		}
	}

	public static class PhotoMetadataBuilder {

		public static PhotoMetadata build() {
			return build(TEST_PHOTO_WIDTH, TEST_PHOTO_HEIGHT, TEST_CAMERA_MODEL_NAME, TEST_PHOTO_CREATION_DATE, TEST_PHOTO_LONGITUDE, TEST_PHOTO_LATITUDE);
		}

		public static PhotoMetadata build(Integer photoWidth, Integer photoHeight, String photoCameraModel, LocalDate photoCreationDate, Double photoLongitude,
				Double photoLatitude) {
			return PhotoMetadata.builder()
					.photoWidth(Optional.ofNullable(photoWidth))
					.photoHeight(Optional.ofNullable(photoHeight))
					.cameraModel(Optional.ofNullable(photoCameraModel))
					.creationDate(Optional.ofNullable(photoCreationDate))
					.gpsLongitude(Optional.ofNullable(photoLongitude))
					.gpsLatitude(Optional.ofNullable(photoLatitude))
					.build();
		}
	}
}
