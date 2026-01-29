package my.photomanager.utils.metaDataParser;


import static my.photomanager.TestDataBuilder.TestPhoto001CameraModel;
import static my.photomanager.TestDataBuilder.TestPhoto001CreationDate;
import static my.photomanager.TestDataBuilder.TestPhoto001GPSLatitude;
import static my.photomanager.TestDataBuilder.TestPhoto001GPSLongitude;
import static my.photomanager.TestDataBuilder.TestPhoto001Path;
import static my.photomanager.TestDataBuilder.TestPhoto001PathHeight;
import static my.photomanager.TestDataBuilder.TestPhoto001PathWidth;
import static my.photomanager.TestDataBuilder.TestPhoto002CameraModel;
import static my.photomanager.TestDataBuilder.TestPhoto002CreationDate;
import static my.photomanager.TestDataBuilder.TestPhoto002GPSLatitude;
import static my.photomanager.TestDataBuilder.TestPhoto002GPSLongitude;
import static my.photomanager.TestDataBuilder.TestPhoto002Path;
import static my.photomanager.TestDataBuilder.TestPhoto002PathHeight;
import static my.photomanager.TestDataBuilder.TestPhoto002PathWidth;
import static my.photomanager.TestDataBuilder.TestPhoto003CameraModel;
import static my.photomanager.TestDataBuilder.TestPhoto003CreationDate;
import static my.photomanager.TestDataBuilder.TestPhoto003GPSLatitude;
import static my.photomanager.TestDataBuilder.TestPhoto003GPSLongitude;
import static my.photomanager.TestDataBuilder.TestPhoto003Path;
import static my.photomanager.TestDataBuilder.TestPhoto003PathHeight;
import static my.photomanager.TestDataBuilder.TestPhoto003PathWidth;
import static my.photomanager.TestDataBuilder.TestPhoto004CameraModel;
import static my.photomanager.TestDataBuilder.TestPhoto004CreationDate;
import static my.photomanager.TestDataBuilder.TestPhoto004GPSLatitude;
import static my.photomanager.TestDataBuilder.TestPhoto004GPSLongitude;
import static my.photomanager.TestDataBuilder.TestPhoto004Path;
import static my.photomanager.TestDataBuilder.TestPhoto004PathHeight;
import static my.photomanager.TestDataBuilder.TestPhoto004PathWidth;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MetadataParserTest {

	static Stream<Arguments> provideMetaData() {
		return Stream.of(
				Arguments.of(TestPhoto001Path, TestPhoto001PathHeight, TestPhoto001PathWidth, TestPhoto001CreationDate, TestPhoto001GPSLongitude,
						TestPhoto001GPSLatitude,
						TestPhoto001CameraModel),
				Arguments.of(TestPhoto002Path, TestPhoto002PathHeight, TestPhoto002PathWidth, TestPhoto002CreationDate, TestPhoto002GPSLongitude,
						TestPhoto002GPSLatitude,
						TestPhoto002CameraModel),
				Arguments.of(TestPhoto003Path, TestPhoto003PathHeight, TestPhoto003PathWidth, TestPhoto003CreationDate, TestPhoto003GPSLongitude,
						TestPhoto003GPSLatitude,
						TestPhoto003CameraModel),
				Arguments.of(TestPhoto004Path, TestPhoto004PathHeight, TestPhoto004PathWidth, TestPhoto004CreationDate, TestPhoto004GPSLongitude,
						TestPhoto004GPSLatitude,
						TestPhoto004CameraModel)
		);
	}

	@ParameterizedTest
	@MethodSource("provideMetaData")
	void shouldParseMetadata(Path photoPath, Integer expectedPhotoHeight, Integer expectedPhotoWidth, LocalDate expectedCreationDate,
			Double expectedLongitude, Double expectedLatitude, String expectedCameraModel) throws Exception {
		// --- WHEN ---
		var metadata = MetadataParser.parseMetadata(photoPath);

		// --- THEN ---
		assertThat(metadata).isNotNull();

		assertThat(metadata.photoHeight()).isEqualTo(expectedPhotoHeight);
		assertThat(metadata.photoWidth()).isEqualTo(expectedPhotoWidth);
		assertThat(metadata.creationDate()).isEqualTo(expectedCreationDate);
		assertThat(metadata.gpsLongitude()).isEqualTo(expectedLongitude);
		assertThat(metadata.gpsLatitude()).isEqualTo(expectedLatitude);
		assertThat(metadata.cameraModel()).isEqualTo(expectedCameraModel);
	}
}