package my.photomanager.metadataparser;

import static my.photomanager.TestDataBuilder.EXAMPLE_001_CAMERA_MODEL;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_CREATION_DATE;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_HEIGHT;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_LATITUDE;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_LONGITUDE;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_PATH;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_WIDTH;
import static my.photomanager.TestDataBuilder.EXAMPLE_002_CAMERA_MODEL;
import static my.photomanager.TestDataBuilder.EXAMPLE_002_CREATION_DATE;
import static my.photomanager.TestDataBuilder.EXAMPLE_002_HEIGHT;
import static my.photomanager.TestDataBuilder.EXAMPLE_002_LATITUDE;
import static my.photomanager.TestDataBuilder.EXAMPLE_002_LONGITUDE;
import static my.photomanager.TestDataBuilder.EXAMPLE_002_PATH;
import static my.photomanager.TestDataBuilder.EXAMPLE_002_WIDTH;
import static my.photomanager.TestDataBuilder.EXAMPLE_003_CAMERA_MODEL;
import static my.photomanager.TestDataBuilder.EXAMPLE_003_CREATION_DATE;
import static my.photomanager.TestDataBuilder.EXAMPLE_003_HEIGHT;
import static my.photomanager.TestDataBuilder.EXAMPLE_003_LATITUDE;
import static my.photomanager.TestDataBuilder.EXAMPLE_003_LONGITUDE;
import static my.photomanager.TestDataBuilder.EXAMPLE_003_PATH;
import static my.photomanager.TestDataBuilder.EXAMPLE_003_WIDTH;
import static my.photomanager.TestDataBuilder.EXAMPLE_004_CAMERA_MODEL;
import static my.photomanager.TestDataBuilder.EXAMPLE_004_CREATION_DATE;
import static my.photomanager.TestDataBuilder.EXAMPLE_004_HEIGHT;
import static my.photomanager.TestDataBuilder.EXAMPLE_004_LATITUDE;
import static my.photomanager.TestDataBuilder.EXAMPLE_004_LONGITUDE;
import static my.photomanager.TestDataBuilder.EXAMPLE_004_PATH;
import static my.photomanager.TestDataBuilder.EXAMPLE_004_WIDTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Log4j2
class MetadataParserTest {

	static Stream<Arguments> provideMetaData() {
		return Stream.of(
				Arguments.of(EXAMPLE_001_PATH, EXAMPLE_001_HEIGHT, EXAMPLE_001_WIDTH, EXAMPLE_001_CREATION_DATE, EXAMPLE_001_LONGITUDE, EXAMPLE_001_LATITUDE,
						EXAMPLE_001_CAMERA_MODEL),
				Arguments.of(EXAMPLE_002_PATH, EXAMPLE_002_HEIGHT, EXAMPLE_002_WIDTH,
						EXAMPLE_002_CREATION_DATE, EXAMPLE_002_LONGITUDE, EXAMPLE_002_LATITUDE,
						EXAMPLE_002_CAMERA_MODEL),
				Arguments.of(EXAMPLE_003_PATH, EXAMPLE_003_HEIGHT, EXAMPLE_003_WIDTH,
						EXAMPLE_003_CREATION_DATE, EXAMPLE_003_LONGITUDE, EXAMPLE_003_LATITUDE,
						EXAMPLE_003_CAMERA_MODEL),
				Arguments.of(EXAMPLE_004_PATH, EXAMPLE_004_HEIGHT, EXAMPLE_004_WIDTH,
						EXAMPLE_004_CREATION_DATE, EXAMPLE_004_LONGITUDE, EXAMPLE_004_LATITUDE,
						EXAMPLE_004_CAMERA_MODEL));
	}

	@ParameterizedTest
	@MethodSource("provideMetaData")
	@DisplayName("should parse metadata correctly")
	void shouldParseMetadata(Path photoPath, Integer expectedPhotoHeight, Integer expectedPhotoWidth, LocalDate expectedCreationDate,
			Double expectedLongitude, Double expectedLatitude, String expectedCameraModel) throws Exception {
		// --- WHEN ---
		var metadata = MetadataParser.parseMetadata(photoPath);

		// --- THEN ---
		assertThat(metadata).isNotNull();

		assertThat(metadata.photoHeight()).isPresent();
		assertThat(metadata.photoHeight()
				.get()).isEqualTo(expectedPhotoHeight);

		assertThat(metadata.photoWidth()).isPresent();
		assertThat(metadata.photoWidth()
				.get()).isEqualTo(expectedPhotoWidth);

		assertThat(metadata.creationDate()).isPresent();
		assertThat(metadata.creationDate()
				.get()).isEqualTo(expectedCreationDate);

		assertThat(metadata.gpsLongitude()).isPresent();
		assertThat(metadata.gpsLongitude()
				.get()).isEqualTo(expectedLongitude);

		assertThat(metadata.gpsLatitude()).isPresent();
		assertThat(metadata.gpsLatitude()
				.get()).isEqualTo(expectedLatitude);

		assertThat(metadata.cameraModel()).isPresent();
		assertThat(metadata.cameraModel()
				.get()).isEqualTo(expectedCameraModel);
	}

	@Test
	@DisplayName("should throw exception when reading non-image file")
	void shouldThrowException() {
		// --- WHEN / THEN ---
		assertThrows(MetadataParserException.class, () -> MetadataParser.parseMetadata(TestDataBuilder.TestFilePath.resolve("Textfile.txt")));
	}

}