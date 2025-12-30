package my.photomanager;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import my.photomanager.photo.Photo;
import my.photomanager.photo.album.Album;
import my.photomanager.photo.cameraModel.CameraModel;
import my.photomanager.photo.category.Category;
import my.photomanager.photo.location.Location;
import my.photomanager.photo.orientation.Orientation;
import my.photomanager.photo.orientation.OrientationName;
import my.photomanager.photo.orientation.OrientationRepository;
import my.photomanager.utils.metaDataParser.Metadata;

public class TestDataBuilder {

	public static Path TestFilePath = Path.of("src", "test", "resources", "Testdata");

	// example 001
	public static Path EXAMPLE_001_PATH = TestFilePath.resolve("example_001.jpg");
	public static Integer EXAMPLE_001_HEIGHT = 750;
	public static Integer EXAMPLE_001_WIDTH = 1000;
	public static Orientation EXAMPLE_001_ORIENTATION = TestOrientationBuilder.build(OrientationName.LANDSCAPE);
	public static LocalDate EXAMPLE_001_CREATION_DATE = LocalDate.of(2024, 1, 1);
	public static String EXAMPLE_001_CAMERA_MODEL = "iPhone 14 Pro";
	public static Double EXAMPLE_001_LONGITUDE = -80.128525;
	public static Double EXAMPLE_001_LATITUDE = 25.7862;
	public static String EXAMPLE_001_COUNTRY = "Vereinigte Staaten von Amerika";
	public static String EXAMPLE_001_CITY = "Miami Beach";
	public static Photo EXAMPLE_001_PHOTO = Photo.builder()
			.withHashValue("123456789")
			.withFileName(EXAMPLE_001_PATH.toFile()
					.getAbsolutePath())
			.withWidth(EXAMPLE_001_WIDTH)
			.withHeight(EXAMPLE_001_HEIGHT)
			.withOrientation(EXAMPLE_001_ORIENTATION)
			.withCreationDate(EXAMPLE_001_CREATION_DATE)
			.withCameraModel(TestCameraModelBuilder.build(EXAMPLE_001_CAMERA_MODEL))
			.withLocation(TestPhotoLocationBuilder.build(EXAMPLE_001_COUNTRY, EXAMPLE_001_CITY))
			.build();

	// example 002
	public static Path EXAMPLE_002_PATH = TestFilePath.resolve("example_002.jpg");
	public static Integer EXAMPLE_002_HEIGHT = 693;
	public static Integer EXAMPLE_002_WIDTH = 1000;
	public static Orientation EXAMPLE_002_ORIENTATION = TestOrientationBuilder.build(OrientationName.LANDSCAPE);
	public static LocalDate EXAMPLE_002_CREATION_DATE = LocalDate.of(2024, 1, 1);
	public static String EXAMPLE_002_CAMERA_MODEL = "Canon EOS-1D Mark IV";
	public static Double EXAMPLE_002_LONGITUDE = 13.376194444444446;
	public static Double EXAMPLE_002_LATITUDE = 52.518680555555555;
	public static String EXAMPLE_002_COUNTRY = "Deutschland";
	public static String EXAMPLE_002_CITY = "Berlin";
	public static Photo EXAMPLE_002_PHOTO = Photo.builder()
			.withHashValue("123456789")
			.withFileName(EXAMPLE_002_PATH.toFile()
					.getAbsolutePath())
			.withWidth(EXAMPLE_002_WIDTH)
			.withHeight(EXAMPLE_002_HEIGHT)
			.withOrientation(EXAMPLE_002_ORIENTATION)
			.withCreationDate(EXAMPLE_002_CREATION_DATE)
			.withCameraModel(TestCameraModelBuilder.build(EXAMPLE_002_CAMERA_MODEL))
			.withLocation(TestPhotoLocationBuilder.build(EXAMPLE_002_COUNTRY, EXAMPLE_002_CITY))
			.build();

	// example 003
	public static Path EXAMPLE_003_PATH = TestFilePath.resolve("example_003.jpg");
	public static Integer EXAMPLE_003_HEIGHT = 750;
	public static Integer EXAMPLE_003_WIDTH = 1000;
	public static Orientation EXAMPLE_003_ORIENTATION = TestOrientationBuilder.build(OrientationName.LANDSCAPE);
	public static LocalDate EXAMPLE_003_CREATION_DATE = LocalDate.of(2025, 1, 1);
	public static String EXAMPLE_003_CAMERA_MODEL = "iPhone 14 Pro";
	public static Double EXAMPLE_003_LONGITUDE = -80.128525;
	public static Double EXAMPLE_003_LATITUDE = 25.7862;
	public static String EXAMPLE_003_COUNTRY = "Vereinigte Staaten von Amerika";
	public static String EXAMPLE_003_CITY = "Miami Beach";
	public static Photo EXAMPLE_003_PHOTO = Photo.builder()
			.withHashValue("123456789")
			.withFileName(EXAMPLE_003_PATH.toFile()
					.getAbsolutePath())
			.withWidth(EXAMPLE_003_WIDTH)
			.withHeight(EXAMPLE_003_HEIGHT)
			.withOrientation(EXAMPLE_003_ORIENTATION)
			.withCreationDate(EXAMPLE_003_CREATION_DATE)
			.withCameraModel(TestCameraModelBuilder.build(EXAMPLE_003_CAMERA_MODEL))
			.withLocation(TestPhotoLocationBuilder.build(EXAMPLE_003_COUNTRY, EXAMPLE_003_CITY))
			.build();

	// example 004
	public static Path EXAMPLE_004_PATH = TestFilePath.resolve("example_004.webp");
	public static Integer EXAMPLE_004_HEIGHT = 768;
	public static Integer EXAMPLE_004_WIDTH = 1024;
	public static Orientation EXAMPLE_004_ORIENTATION = TestOrientationBuilder.build(OrientationName.LANDSCAPE);
	public static LocalDate EXAMPLE_004_CREATION_DATE = LocalDate.of(2025, 1, 1);
	public static String EXAMPLE_004_CAMERA_MODEL = "iPhone 14 Pro";
	public static Double EXAMPLE_004_LONGITUDE = 13.376194444444446;
	public static Double EXAMPLE_004_LATITUDE = 52.518680555555555;
	public static String EXAMPLE_004_COUNTRY = "Deutschland";
	public static String EXAMPLE_004_CITY = "Berlin";
	public static Photo EXAMPLE_004_PHOTO = Photo.builder()
			.withHashValue("123456789")
			.withFileName(EXAMPLE_004_PATH.toFile()
					.getAbsolutePath())
			.withWidth(EXAMPLE_004_WIDTH)
			.withHeight(EXAMPLE_004_HEIGHT)
			.withOrientation(EXAMPLE_004_ORIENTATION)
			.withCreationDate(EXAMPLE_004_CREATION_DATE)
			.withCameraModel(TestCameraModelBuilder.build(EXAMPLE_004_CAMERA_MODEL))
			.withLocation(TestPhotoLocationBuilder.build(EXAMPLE_004_COUNTRY, EXAMPLE_004_CITY))
			.build();

	public static final String TEST_ALBUM_NAME = "TestAlbum";
	public static final String TEST_CAMERA_MODEL_NAME = "TestCameraModel";
	public static final String TEST_CATEGORY_NAME = "TestCategory";
	public static final String TEST_LOCATION_COUNTRY = "TestLocationCountry";
	public static final String TEST_LOCATION_CITY = "TestLocationCity";
	public static final String TEST_PHOTO_HASH_VALUE = "TestPhotoHashValue";
	public static final String TEST_PHOTO_FILE_NAME = "TestPhotoFileName";
	public static final Integer TEST_PHOTO_WIDTH = 1000;
	public static final Integer TEST_PHOTO_HEIGHT = 750;
	public static final OrientationName TEST_PHOTO_ORIENTATION = OrientationName.LANDSCAPE;
	public static final LocalDate TEST_PHOTO_CREATION_DATE = LocalDate.of(2024, 1, 1);
	public static final Double TEST_PHOTO_LONGITUDE = -80.128525;
	public static final Double TEST_PHOTO_LATITUDE = 25.7862;

	public static class TestPhotoAlbumBuilder {

		public static Album build() {
			return build(TEST_ALBUM_NAME);
		}

		public static Album build(String name) {
			return new Album(name);
		}
	}

	public static class TestCameraModelBuilder {

		public static CameraModel build() {
			return build(TEST_CAMERA_MODEL_NAME);
		}

		public static CameraModel build(String modelName) {
			return new CameraModel(modelName);
		}
	}

	public static class TestPhotoCategoryBuilder {

		public static Category build() {
			return build(TEST_CATEGORY_NAME);
		}

		public static Category build(String name) {
			return new Category(name);
		}
	}

	public static class TestPhotoLocationBuilder {

		public static Location build() {
			return build(TEST_LOCATION_COUNTRY, TEST_LOCATION_CITY);
		}

		public static Location build(String country, String city) {
			return new Location(country, city);
		}
	}

	public static class TestOrientationBuilder {

		public static Orientation build() {
			return build(TEST_PHOTO_ORIENTATION);
		}

		public static Orientation build(OrientationName name) {
			return new Orientation(name.getName());
		}

		public static Orientation buildAndSave(OrientationRepository repository) {
			return buildAndSave(repository, TEST_PHOTO_ORIENTATION);
		}

		public static Orientation buildAndSave(OrientationRepository repository, OrientationName name) {
			return repository.save(build(name));
		}
	}

	public static class TestPhotoBuilder {

		public static Photo build() {
			var orientation = TestOrientationBuilder.build();
			return build(TEST_PHOTO_HASH_VALUE, TEST_PHOTO_FILE_NAME, TEST_PHOTO_HEIGHT, TEST_PHOTO_WIDTH, orientation, TEST_PHOTO_CREATION_DATE);
		}

		public static Photo build(String hashValue, String fileName, int height, int width, Orientation orientation, LocalDate creationDate) {
			return Photo.builder()
					.withHashValue(hashValue)
					.withFileName(fileName)
					.withWidth(width)
					.withHeight(height)
					.withOrientation(orientation)
					.withCreationDate(creationDate)
					.build();
		}
	}

	public static class PhotoMetadataBuilder {

		public static Metadata build() {
			return build(TEST_PHOTO_WIDTH, TEST_PHOTO_HEIGHT, TEST_CAMERA_MODEL_NAME, TEST_PHOTO_CREATION_DATE, TEST_PHOTO_LONGITUDE, TEST_PHOTO_LATITUDE);
		}

		public static Metadata build(Integer photoWidth, Integer photoHeight, String photoCameraModel, LocalDate photoCreationDate, Double photoLongitude,
				Double photoLatitude) {
			return Metadata.builder()
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
