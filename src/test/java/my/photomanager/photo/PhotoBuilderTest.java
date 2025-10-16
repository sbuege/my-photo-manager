package my.photomanager.photo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import my.photomanager.TestConstants;
import my.photomanager.geoLocationResolver.GeoLocationResolverException;
import my.photomanager.metadata.PhotoMetadataReaderException;
import my.photomanager.photo.cameraSettings.CameraSettings;
import my.photomanager.photo.cameraSettings.CameraSettingsService;
import my.photomanager.photo.location.PhotoLocation;
import my.photomanager.photo.location.PhotoLocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Log4j2
class PhotoBuilderTest {

	private PhotoBuilder photoBuilder;

	@Mock
	private PhotoLocationService photoLocationService;

	@Mock
	private CameraSettingsService cameraSettingsService;


	@BeforeEach
	public void setUp() {
		photoBuilder = new PhotoBuilder(photoLocationService, cameraSettingsService);
	}

	public static Stream<Path> photoFileProvider() {
		return Stream.of(TestConstants.EXAMPLE_001_PATH, TestConstants.EXAMPLE_004_PATH);
	}

	@ParameterizedTest
	@MethodSource("photoFileProvider")
	void shouldBuildPhotoFromPhotoFile(Path photoPath) throws GeoLocationResolverException, PhotoMetadataReaderException, IOException {
		// when
		var photo = photoBuilder.buildPhoto(photoPath);
		log.info("photo: {}", photo);

		// then
		verify(photoLocationService).saveOrGetPhotoLocation(any(PhotoLocation.class));
		verify(cameraSettingsService).saveOrGetCameraSettings(any(CameraSettings.class));
	}
}