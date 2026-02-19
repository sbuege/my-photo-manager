package my.photomanager;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;

import my.photomanager.core.album.Album;
import my.photomanager.core.cameraModel.CameraModel;
import my.photomanager.core.category.Category;
import my.photomanager.core.library.Library;
import my.photomanager.core.location.Location;
import my.photomanager.core.orientation.Orientation;
import my.photomanager.core.orientation.OrientationName;
import my.photomanager.core.photo.Photo;
import my.photomanager.utils.metaDataParser.Metadata;
import org.apache.logging.log4j.util.Strings;

public class TestDataBuilder {

	public static Stream<String> invalidNamesProvider() {
		return Stream.of(Strings.EMPTY, " ");
	}

	// Album Test Data
	public static final long TestAlbumId = 1L;
	public static final String TestAlbumName = "TestAlbum";

	public static Album buildAlbum() {
		return buildAlbum(TestAlbumName);
	}

	public static Album buildAlbum(String name) {
		return new Album(name);
	}

	// CameraModel Test Data
	public static final long TestCameraModelId = 1L;
	public static final String TestCameraModelName = "TestCameraModel";

	public static CameraModel buildCameraModel() {
		return buildCameraModel(TestCameraModelName);
	}

	public static CameraModel buildCameraModel(String modelName) {
		return new CameraModel(modelName);
	}

	// Category Test Data
	public static final long TestCategoryId = 1L;
	public static final String TestCategoryName = "TestCategory";

	public static Category buildCategory() {
		return buildCategory(TestCategoryName);
	}

	public static Category buildCategory(String modelName) {
		return new Category(modelName);
	}

	// Library Test Data
	public static final long TestLibraryId = 1L;
	public static final String TestLibraryPath = "TestLibraryPath";
	public static final String TestLibraryName = "TestLibraryName";

	public static Library buildLibrary() {
		return buildLibrary(TestLibraryPath, TestLibraryName);
	}

	public static Library buildLibrary(String path, String name) {
		return new Library(path, name);
	}

	// Location Test Data
	public static final long TestLocationId = 1L;
	public static final String TestLocationCountry = "TestLocationCountry";
	public static final String TestLocationCity = "TestLocationCity";
	public static final Double TestLocationLongitude = -80.128525;
	public static final Double TestLocationLatitude = 25.7862;

	public static Location buildLocation() {
		return buildLocation(TestLocationCountry, TestLocationCity);
	}

	public static Location buildLocation(String country, String city) {
		return new Location(country, city);
	}

	// Orientation Test Data
	public static final long TestOrientationId = 1L;
	public static final OrientationName TEST_PHOTO_ORIENTATION = OrientationName.LANDSCAPE;

	public static Orientation buildOrientation() {
		return buildOrientation(TEST_PHOTO_ORIENTATION.getName());
	}

	public static Orientation buildOrientation(String name) {
		return new Orientation(name);
	}

	// PhotoTest Data
	public static final long TestPhotoId = 1L;
	public static final String TestPhotoHashValue = "TestPhotoHashValue";
	public static final String TestPhotoFileName = "TestPhotoFileName";
	public static final LocalDate TestPhotoCreationDate = LocalDate.of(2024, 1, 1);
	public static final int TestPhotoWidth = 1024;
	public static final int TestPhotoHeight = 768;

	public static Photo buildPhoto() {
		var orientation = buildOrientation();
		return buildPhoto(TestPhotoHashValue, TestPhotoFileName, TestPhotoHeight, TestPhotoWidth, orientation, buildCameraModel(), buildLocation(),
				TestPhotoCreationDate);
	}

	public static Photo buildPhoto(String hashValue, String fileName, int height, int width, Orientation orientation, CameraModel cameraModel,
			Location location, LocalDate creationDate) {
		return new Photo(hashValue, fileName, height, width, orientation, cameraModel, location, creationDate);
	}

	public static Path createTestPhotoFile() throws IOException {
		return Files.createTempFile("test-photo-", ".jpg");
	}

	// Metadata
	public static Metadata buildMetadata() {
		return new Metadata(TestPhotoWidth, TestPhotoHeight, TestCameraModelName, TestPhotoCreationDate, TestLocationLongitude, TestLocationLatitude);
	}

	// Test Photos Data
	public static Path TestFilePath = Path.of("src", "test", "resources", "Testdata");

	// example 001
	public static Path TestPhoto001Path = TestFilePath.resolve("example_001.jpg");
	public static Integer TestPhoto001PathHeight = 750;
	public static Integer TestPhoto001PathWidth = 1000;
	public static Orientation TestPhoto001Orientation = buildOrientation(String.valueOf(OrientationName.LANDSCAPE));
	public static LocalDate TestPhoto001CreationDate = LocalDate.of(2024, 1, 1);
	public static String TestPhoto001CameraModel = "iPhone 14 Pro";
	public static Double TestPhoto001GPSLongitude = -80.128525;
	public static Double TestPhoto001GPSLatitude = 25.7862;
	public static String TestPhoto001LocationCountry = "Vereinigte Staaten von Amerika";
	public static String TestPhoto001LocationCity = "Miami Beach";

	public static Photo buildPhoto001() {
		return buildPhoto(TestPhotoHashValue, TestPhoto001Path.toFile()
						.getAbsolutePath(), TestPhoto001PathHeight, TestPhoto001PathWidth, TestPhoto001Orientation, buildCameraModel(TestPhoto001CameraModel),
				buildLocation(TestPhoto001LocationCountry, TestPhoto001LocationCity),
				TestPhoto001CreationDate);
	}

	// example 002
	public static Path TestPhoto002Path = TestFilePath.resolve("example_002.jpg");
	public static Integer TestPhoto002PathHeight = 693;
	public static Integer TestPhoto002PathWidth = 1000;
	public static Orientation TestPhoto002Orientation = buildOrientation(String.valueOf(OrientationName.LANDSCAPE));
	public static LocalDate TestPhoto002CreationDate = LocalDate.of(2024, 1, 1);
	public static String TestPhoto002CameraModel = "Canon EOS-1D Mark IV";
	public static Double TestPhoto002GPSLongitude = 13.376194444444446;
	public static Double TestPhoto002GPSLatitude = 52.518680555555555;
	public static String TestPhoto002LocationCountry = "Deutschland";
	public static String TestPhoto002LocationCity = "Berlin";

	// example 003
	public static Path TestPhoto003Path = TestFilePath.resolve("example_003.jpg");
	public static Integer TestPhoto003PathHeight = 750;
	public static Integer TestPhoto003PathWidth = 1000;
	public static Orientation TestPhoto003Orientation = buildOrientation(String.valueOf(OrientationName.LANDSCAPE));
	public static LocalDate TestPhoto003CreationDate = LocalDate.of(2025, 1, 1);
	public static String TestPhoto003CameraModel = "iPhone 14 Pro";
	public static Double TestPhoto003GPSLongitude = -80.128525;
	public static Double TestPhoto003GPSLatitude = 25.7862;
	public static String TestPhoto003LocationCountry = "Vereinigte Staaten von Amerika";
	public static String TestPhoto003LocationCity = "Miami Beach";

	// example 004
	public static Path TestPhoto004Path = TestFilePath.resolve("example_004.webp");
	public static Integer TestPhoto004PathHeight = 768;
	public static Integer TestPhoto004PathWidth = 1024;
	public static Orientation TestPhoto004Orientation = buildOrientation(String.valueOf(OrientationName.LANDSCAPE));
	public static LocalDate TestPhoto004CreationDate = LocalDate.of(2025, 1, 1);
	public static String TestPhoto004CameraModel = "iPhone 14 Pro";
	public static Double TestPhoto004GPSLongitude = 13.376194444444446;
	public static Double TestPhoto004GPSLatitude = 52.518680555555555;
	public static String TestPhoto004LocationCountry = "Deutschland";
	public static String TestPhoto004LocationCity = "Berlin";

}
