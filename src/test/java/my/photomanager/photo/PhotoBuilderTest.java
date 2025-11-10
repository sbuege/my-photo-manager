package my.photomanager.photo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import my.photomanager.TestConstants;
import my.photomanager.geoLocationResolver.GeoLocationResolverException;
import my.photomanager.metadata.PhotoMetadata;
import my.photomanager.metadata.PhotoMetadataReader;
import my.photomanager.metadata.PhotoMetadataReaderException;
import my.photomanager.photo.cameraSettings.CameraSettings;
import my.photomanager.photo.cameraSettings.CameraSettingsService;
import my.photomanager.photo.location.PhotoLocation;
import my.photomanager.photo.location.PhotoLocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Log4j2
class PhotoBuilderTest {

	@Mock
	private PhotoLocationService photoLocationService;

	@Mock
	private CameraSettingsService cameraSettingsService;

	@InjectMocks
	private PhotoBuilder photoBuilder;

	@Test
	void shouldCall() throws PhotoBuilderException, GeoLocationResolverException, PhotoMetadataReaderException, IOException {
		// given
		MockedStatic<PhotoMetadataReader> mockedStatic = mockStatic(PhotoMetadataReader.class);
		mockedStatic.when(() -> PhotoMetadataReader.readPhotoMetadata(any(Path.class)))
				.thenReturn(PhotoMetadata.builder()
						.build());
		// when

		photoBuilder.buildPhoto(TestConstants.EXAMPLE_001_PATH);
		// then
	}


	@Test
	void shouldBuildPhotoFromJPEGPhotoFile() throws GeoLocationResolverException, PhotoMetadataReaderException, IOException, PhotoBuilderException {
		// when
		var photo = photoBuilder.buildPhoto(TestConstants.EXAMPLE_001_PATH);
		log.info("photo: {}", photo);

		// then
		verify(photoLocationService).saveOrGetPhotoLocation(any(PhotoLocation.class));
		verify(cameraSettingsService).saveOrGetCameraSettings(any(CameraSettings.class));
	}

	@Test
	void shouldBuildPhotoFromWebpPhotoFile() throws GeoLocationResolverException, PhotoMetadataReaderException, IOException, PhotoBuilderException {
		// when
		var photo = photoBuilder.buildPhoto(TestConstants.EXAMPLE_004_PATH);
		log.info("photo: {}", photo);

		// then
		verify(photoLocationService).saveOrGetPhotoLocation(any(PhotoLocation.class));
	}

	@Test
	void shouldThrowExceptionWhenPhotoWidthIsEmpty() {
		// when
		try (MockedStatic<PhotoMetadataReader> mockedStatic = mockStatic(PhotoMetadataReader.class)) {
			mockedStatic.when(() -> PhotoMetadataReader.readPhotoMetadata(TestConstants.EXAMPLE_001_PATH))
					.thenReturn(
							new PhotoMetadata(Optional.empty(), Optional.of(TestConstants.EXAMPLE_001_HEIGHT), Optional.empty(), Optional.empty(),
									Optional.empty(),
									Optional.empty()));
			// then
			assertThrows(PhotoBuilderException.class, () -> photoBuilder.buildPhoto(TestConstants.EXAMPLE_001_PATH));
		}

	}

	@ParameterizedTest
	@ValueSource(ints = {0, -1})
	void shouldThrowExceptionWhenPhotoHasInvalidWidth(Integer width) {
		// when
		try (MockedStatic<PhotoMetadataReader> mockedStatic = mockStatic(PhotoMetadataReader.class)) {
			mockedStatic.when(() -> PhotoMetadataReader.readPhotoMetadata(TestConstants.EXAMPLE_001_PATH))
					.thenReturn(
							new PhotoMetadata(Optional.of(width), Optional.of(TestConstants.EXAMPLE_001_HEIGHT), Optional.empty(), Optional.empty(),
									Optional.empty(),
									Optional.empty()));

			// then
			assertThrows(PhotoBuilderException.class, () -> photoBuilder.buildPhoto(TestConstants.EXAMPLE_001_PATH));
		}
	}

	@Test
	void shouldThrowExceptionWhenPhotoHeightIsEmpty() {
		// when
		try (MockedStatic<PhotoMetadataReader> mockedStatic = mockStatic(PhotoMetadataReader.class)) {
			mockedStatic.when(() -> PhotoMetadataReader.readPhotoMetadata(TestConstants.EXAMPLE_001_PATH))
					.thenReturn(
							new PhotoMetadata(Optional.of(TestConstants.EXAMPLE_001_WIDTH), Optional.empty(), Optional.empty(), Optional.empty(),
									Optional.empty(),
									Optional.empty()));

			// then
			assertThrows(PhotoBuilderException.class, () -> photoBuilder.buildPhoto(TestConstants.EXAMPLE_001_PATH));
		}

	}

	@ParameterizedTest
	@ValueSource(ints = {0, -1})
	void shouldThrowExceptionWhenPhotoHasInvalidHeight(Integer height) {
		// when
		try (MockedStatic<PhotoMetadataReader> mockedStatic = mockStatic(PhotoMetadataReader.class)) {
			mockedStatic.when(() -> PhotoMetadataReader.readPhotoMetadata(TestConstants.EXAMPLE_001_PATH))
					.thenReturn(
							new PhotoMetadata(Optional.of(TestConstants.EXAMPLE_001_WIDTH), Optional.of(height), Optional.empty(), Optional.empty(),
									Optional.empty(),
									Optional.empty()));

			// then
			assertThrows(PhotoBuilderException.class, () -> photoBuilder.buildPhoto(TestConstants.EXAMPLE_001_PATH));
		}

	}
}