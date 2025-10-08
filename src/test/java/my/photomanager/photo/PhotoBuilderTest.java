package my.photomanager.photo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import my.photomanager.TestConstants;
import my.photomanager.geoLocationResolver.GeoLocationResolverException;
import my.photomanager.metadata.PhotoMetadataReaderException;
import my.photomanager.photo.cameraSettings.CameraSettingsService;
import my.photomanager.photo.location.PhotoLocationService;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class PhotoBuilderTest {

	private PhotoBuilder photoBuilder;

	@Autowired
	private PhotoLocationService photoLocationService;

	@Autowired
	private CameraSettingsService cameraSettingsService;

	@BeforeEach
	public void setUp() {
		photoBuilder = new PhotoBuilder(photoLocationService, cameraSettingsService);
	}

	public static Stream<Path> photosWithExifDataFileProvider() {
		return Stream.of(TestConstants.jpegWithExifDataFileProvider(),
						TestConstants.webWithExifDataFileProvider())
				.flatMap(s -> s);
	}

	@ParameterizedTest
	@MethodSource("photosWithExifDataFileProvider")
	void shouldBuildPhotoFromPhotoWithExifData(Path photoPath) throws GeoLocationResolverException, PhotoMetadataReaderException, IOException {
		// given / when
		var photo = photoBuilder.buildPhoto(photoPath);
		log.info("photo: {}", photo);

		// then
		assertThat(photo).isNotNull();

		assertThat(photo.getHashValue()).isNotNull();
		assertThat(photo.getHashValue()).isNotEmpty();

		assertThat(photo.getFileName()).isNotNull();
		assertThat(photo.getFileName()).isEqualTo(photoPath.toAbsolutePath()
				.toString());

		assertThat(photo.getHeight()).isEqualTo(768);

		assertThat(photo.getWidth()).isEqualTo(1024);

		assertThat(photo.getCreationDate()).isNotNull();
		assertThat(photo.getCreationDate()).isEqualTo(LocalDate.of(2025, 1, 1));

		assertThat(photo.getLocation()).isNotNull();
		assertThat(photo.getLocation()
				.getId()).isGreaterThan(0);
		assertThat(photo.getLocation()
				.getCountry()).isEqualTo("Deutschland");
		assertThat(photo.getLocation()
				.getCity()).isEqualTo("Berlin");

		assertThat(photo.getCameraSettings()).isNotNull();
		assertThat(photo.getCameraSettings()
				.getId()).isGreaterThan(0);
		assertThat(photo.getCameraSettings()
				.getCameraModelName()).isEqualTo(Strings.EMPTY);
	}

	public static Stream<Path> photosWithoutExifDataFileProvider() {
		return Stream.of(TestConstants.jpegWithoutExifDataFileProvider(),
						TestConstants.webWithoutExifDataFileProvider())
				.flatMap(s -> s);
	}

	@ParameterizedTest
	@MethodSource("photosWithoutExifDataFileProvider")
	void shouldBuildPhotoFromPhotoWithoutExifData(Path photoPath) throws Exception {
		// given / when
		var photo = photoBuilder.buildPhoto(photoPath);
		log.info("photo: {}", photo);

		// then
		assertThat(photo).isNotNull();

		assertThat(photo.getHashValue()).isNotNull();
		assertThat(photo.getHashValue()).isNotEmpty();

		assertThat(photo.getFileName()).isNotNull();
		assertThat(photo.getFileName()).isEqualTo(photoPath.toAbsolutePath()
				.toString());

		assertThat(photo.getHeight()).isEqualTo(768);

		assertThat(photo.getWidth()).isEqualTo(1024);

		assertThat(photo.getCreationDate()).isNotNull();
		assertThat(photo.getCreationDate()).isEqualTo(LocalDate.of(0, 1, 1));

		assertThat(photo.getLocation()).isNotNull();
		assertThat(photo.getLocation()
				.getId()).isGreaterThan(0);
		assertThat(photo.getLocation()
				.getCountry()).isEqualTo(Strings.EMPTY);
		assertThat(photo.getLocation()
				.getCity()).isEqualTo(Strings.EMPTY);

		assertThat(photo.getCameraSettings()).isNotNull();
		assertThat(photo.getCameraSettings()
				.getId()).isGreaterThan(0);
		assertThat(photo.getCameraSettings()
				.getCameraModelName()).isEqualTo(Strings.EMPTY);
	}
}