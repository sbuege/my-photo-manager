package my.photomanager.photo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import my.photomanager.TestDataBuilder;
import my.photomanager.geoLocationResolver.GeoLocationResolverException;
import my.photomanager.metadata.PhotoMetadata;
import my.photomanager.metadata.PhotoMetadataReader;
import my.photomanager.metadata.PhotoMetadataReaderException;
import my.photomanager.photo.cameraSettings.CameraSettings;
import my.photomanager.photo.cameraSettings.CameraSettingsService;
import my.photomanager.photo.location.PhotoLocation;
import my.photomanager.photo.location.PhotoLocationService;
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
	private PhotoLocationService photoLocationService;

	@Mock
	private CameraSettingsService cameraSettingsService;

	@InjectMocks
	private PhotoBuilder photoBuilder;

	@Test
	@DisplayName("should invoke camera settings and location services")
	void shouldInvokeCameraSettingsAndLocationServices()
			throws PhotoBuilderException, GeoLocationResolverException, PhotoMetadataReaderException, IOException {

		try (MockedStatic<PhotoMetadataReader> photoMetadataReader = mockStatic(PhotoMetadataReader.class)) {
			// --- GIVEN ---
			photoMetadataReader.when(() -> PhotoMetadataReader.readPhotoMetadata(any(Path.class)))
					.thenReturn(TestDataBuilder.PhotoMetadataBuilder.build());

			// --- WHEN ---
			photoBuilder.buildPhoto(TEST_PHOTO_PATH);

			// --- THEN ---
			verify(cameraSettingsService).saveOrGetCameraSettings(any(CameraSettings.class));
			verify(photoLocationService).saveOrGetPhotoLocation(any(PhotoLocation.class));
		}
	}

	static Stream<PhotoMetadata> provideMetaDataWitInvalidCameraModel() {
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
	@DisplayName("should skip camera settings service when metadata contains no valid camera model")
	void yshouldSkipCameraSettingsService(PhotoMetadata metadata)
			throws PhotoBuilderException, GeoLocationResolverException, PhotoMetadataReaderException, IOException {

		try (MockedStatic<PhotoMetadataReader> photoMetadataReader = mockStatic(PhotoMetadataReader.class)) {
			// --- GIVEN ---
			photoMetadataReader.when(() -> PhotoMetadataReader.readPhotoMetadata(any(Path.class)))
					.thenReturn(metadata);

			// --- WHEN ---
			photoBuilder.buildPhoto(TEST_PHOTO_PATH);

			// --- THEN ---
			verify(cameraSettingsService, never()).saveOrGetCameraSettings(any(CameraSettings.class));
			verify(photoLocationService).saveOrGetPhotoLocation(any(PhotoLocation.class));
		}
	}

	static Stream<PhotoMetadata> provideMetaDataWithInvalidGpsCoordinates() {
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
	void shouldSkipLocationService(PhotoMetadata metadata)
			throws PhotoBuilderException, GeoLocationResolverException, PhotoMetadataReaderException, IOException {

		try (MockedStatic<PhotoMetadataReader> photoMetadataReader = mockStatic(PhotoMetadataReader.class)) {
			// --- GIVEN ---
			photoMetadataReader.when(() -> PhotoMetadataReader.readPhotoMetadata(any(Path.class)))
					.thenReturn(metadata);

			// --- WHEN ---
			photoBuilder.buildPhoto(TEST_PHOTO_PATH);

			// --- THEN ---
			verify(cameraSettingsService).saveOrGetCameraSettings(any(CameraSettings.class));
			verify(photoLocationService, never()).saveOrGetPhotoLocation(any(PhotoLocation.class));
		}
	}

	static Stream<PhotoMetadata> provideMetaDataWithInvalidPhotoWidth() {
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
	void shouldThrowExceptionWhenMetadataContainsInvalidPhotoWidth(PhotoMetadata metadata) {

		try (MockedStatic<PhotoMetadataReader> mockedStatic = mockStatic(PhotoMetadataReader.class)) {
			// --- GIVEN ---
			mockedStatic.when(() -> PhotoMetadataReader.readPhotoMetadata(any(Path.class)))
					.thenReturn(metadata);

			// --- WHEN / THEN ---
			assertThrows(PhotoBuilderException.class, () -> photoBuilder.buildPhoto(TEST_PHOTO_PATH));
		}
	}

	static Stream<PhotoMetadata> provideMetaDataWithInvalidPhotoHeight() {
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
	void shouldThrowExceptionWhenMetadataContainsInvalidPhotoHeight(PhotoMetadata metadata) {

		try (MockedStatic<PhotoMetadataReader> mockedStatic = mockStatic(PhotoMetadataReader.class)) {
			// --- GIVEN ---
			mockedStatic.when(() -> PhotoMetadataReader.readPhotoMetadata(any(Path.class)))
					.thenReturn(metadata);

			// --- WHEN / THEN ---
			assertThrows(PhotoBuilderException.class, () -> photoBuilder.buildPhoto(TEST_PHOTO_PATH));
		}
	}
}