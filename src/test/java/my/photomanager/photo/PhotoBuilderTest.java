package my.photomanager.photo;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import my.photomanager.TestDataBuilder;
import my.photomanager.gpsResolver.GpsResolverException;
import my.photomanager.metadata.Metadata;
import my.photomanager.metadata.MetadataParser;
import my.photomanager.metadata.MetadataParserException;
import my.photomanager.photo.cameraModel.CameraModel;
import my.photomanager.photo.cameraModel.CameraModelService;
import my.photomanager.photo.location.Location;
import my.photomanager.photo.location.LocationService;
import my.photomanager.photo.orientation.Orientation;
import my.photomanager.photo.orientation.OrientationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Log4j2
class PhotoBuilderTest {

	// TEST DATA
	private final Path TEST_PHOTO_PATH = TestDataBuilder.EXAMPLE_001_PATH;

	@Mock
	private LocationService locationService;

	@Mock
	private CameraModelService cameraModelService;

	@Mock
	private OrientationService orientationService;

	@InjectMocks
	private PhotoBuilder photoBuilder;

	@Test
	@DisplayName("should invoke camera model and location services")
	void shouldInvokeCameraModelAndLocationServices()
			throws PhotoBuilderException, GpsResolverException, MetadataParserException, IOException {

		try (MockedStatic<MetadataParser> photoMetadataReader = mockStatic(MetadataParser.class)) {
			// --- GIVEN ---
			photoMetadataReader.when(() -> MetadataParser.parseMetadata(any(Path.class)))
					.thenReturn(TestDataBuilder.PhotoMetadataBuilder.build());
			when(orientationService.saveOrGetOrientation(any())).thenReturn(TestDataBuilder.TestOrientationBuilder.build());

			// --- WHEN ---
			photoBuilder.buildPhoto(TEST_PHOTO_PATH);

			// --- THEN ---
			verify(cameraModelService).saveOrGetCameraModel(any(CameraModel.class));
			verify(locationService).saveOrGetLocation(any(Location.class));
		}
	}

	static Stream<Metadata> provideMetaDataWitInvalidCameraModel() {
		return Stream.of(/*TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.cameraModel(Optional.of(Strings.EMPTY))
						.build(),
				TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.cameraModel(Optional.of(" "))
						.build(),*/
				TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.cameraModel(Optional.empty())
						.build());

	}

	@ParameterizedTest
	@MethodSource("provideMetaDataWitInvalidCameraModel")
	@DisplayName("should skip camera model service when metadata contains no valid camera model")
	void shouldSkipCameraModelService(Metadata metadata)
			throws PhotoBuilderException, GpsResolverException, MetadataParserException, IOException {

		try (MockedStatic<MetadataParser> photoMetadataReader = mockStatic(MetadataParser.class)) {
			// --- GIVEN ---
			photoMetadataReader.when(() -> MetadataParser.parseMetadata(any(Path.class)))
					.thenReturn(metadata);
			when(orientationService.saveOrGetOrientation(any())).thenReturn(TestDataBuilder.TestOrientationBuilder.build());

			// --- WHEN ---
			photoBuilder.buildPhoto(TEST_PHOTO_PATH);

			// --- THEN ---
			verify(cameraModelService, never()).saveOrGetCameraModel(any(CameraModel.class));
			verify(locationService).saveOrGetLocation(any(Location.class));
		}
	}

	static Stream<Metadata> provideMetaDataWithInvalidGpsCoordinates() {
		return Stream.of(TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.gpsLongitude(Optional.empty())
						.build(),
				TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.gpsLatitude(Optional.empty())
						.build());
	}

	@ParameterizedTest
	@MethodSource("provideMetaDataWithInvalidGpsCoordinates")
	@DisplayName("should skip location service when metadata contains invalid gps coordinates")
	void shouldSkipLocationService(Metadata metadata)
			throws PhotoBuilderException, GpsResolverException, MetadataParserException, IOException {

		try (MockedStatic<MetadataParser> photoMetadataReader = mockStatic(MetadataParser.class)) {
			// --- GIVEN ---
			photoMetadataReader.when(() -> MetadataParser.parseMetadata(any(Path.class)))
					.thenReturn(metadata);
			when(orientationService.saveOrGetOrientation(any())).thenReturn(TestDataBuilder.TestOrientationBuilder.build());

			// --- WHEN ---
			photoBuilder.buildPhoto(TEST_PHOTO_PATH);

			// --- THEN ---
			verify(cameraModelService).saveOrGetCameraModel(any(CameraModel.class));
			verify(locationService, never()).saveOrGetLocation(any(Location.class));
		}
	}

	static Stream<Metadata> provideMetaDataWithInvalidPhotoWidth() {
		return Stream.of(TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.photoWidth(Optional.of(0))
						.build(),
				TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.photoWidth(Optional.of(-1))
						.build(),
				TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.photoWidth(Optional.empty())
						.build()
		);
	}

	@ParameterizedTest
	@MethodSource("provideMetaDataWithInvalidPhotoWidth")
	@DisplayName("should throw exception when metadata contains invalid photo width")
	void shouldThrowExceptionWhenMetadataContainsInvalidPhotoWidth(Metadata metadata) {

		try (MockedStatic<MetadataParser> mockedStatic = mockStatic(MetadataParser.class)) {
			// --- GIVEN ---
			mockedStatic.when(() -> MetadataParser.parseMetadata(any(Path.class)))
					.thenReturn(metadata);

			// --- WHEN / THEN ---
			assertThrows(PhotoBuilderException.class, () -> photoBuilder.buildPhoto(TEST_PHOTO_PATH));
		}
	}

	static Stream<Metadata> provideMetaDataWithInvalidPhotoHeight() {
		return Stream.of(TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.photoHeight(Optional.of(0))
						.build(),
				TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.photoHeight(Optional.of(-1))
						.build(),
				TestDataBuilder.PhotoMetadataBuilder.build()
						.toBuilder()
						.photoHeight(Optional.empty())
						.build()
		);
	}

	@ParameterizedTest
	@MethodSource("provideMetaDataWithInvalidPhotoHeight")
	@DisplayName("should throw exception when metadata contains invalid photo height")
	void shouldThrowExceptionWhenMetadataContainsInvalidPhotoHeight(Metadata metadata) {

		try (MockedStatic<MetadataParser> mockedStatic = mockStatic(MetadataParser.class)) {
			// --- GIVEN ---
			mockedStatic.when(() -> MetadataParser.parseMetadata(any(Path.class)))
					.thenReturn(metadata);

			// --- WHEN / THEN ---
			assertThrows(PhotoBuilderException.class, () -> photoBuilder.buildPhoto(TEST_PHOTO_PATH));
		}
	}
}