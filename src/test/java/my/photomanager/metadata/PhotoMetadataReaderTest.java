package my.photomanager.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import my.photomanager.TestConstants;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Log4j2
class PhotoMetadataReaderTest {

	public static Stream<Path> photosWithExifDataFileProvider() {
		return Stream.of(TestConstants.jpegWithExifDataFileProvider(),
						TestConstants.webWithExifDataFileProvider())
				.flatMap(s -> s);
	}

	@ParameterizedTest
	@MethodSource("photosWithExifDataFileProvider")
	void shouldReturnExistingPhotoMetadata(Path photoPath) throws Exception {
		// when
		var photoMetadata = PhotoMetadataReader.readPhotoMetadata(photoPath);
		log.info("photoMetadata : {}", photoMetadata);

		// then
		assertThat(photoMetadata).isNotNull();
		assertThat(photoMetadata.photoHeight()).isEqualTo(768);
		assertThat(photoMetadata.photoWidth()).isEqualTo(1024);
		assertThat(photoMetadata.creationDate()).isEqualTo(LocalDate.of(2025, 1, 1));
		assertThat(photoMetadata.gpsLongitude()).isEqualTo(13.376194444444446);
		assertThat(photoMetadata.gpsLatitude()).isEqualTo(52.518680555555555);
		assertThat(photoMetadata.cameraModel()).isEqualTo(Strings.EMPTY);
	}

	public static Stream<Path> photosWithoutExifDataFileProvider() {
		return Stream.of(TestConstants.jpegWithoutExifDataFileProvider(),
						TestConstants.webWithoutExifDataFileProvider())
				.flatMap(s -> s);
	}

	@ParameterizedTest
	@MethodSource("photosWithoutExifDataFileProvider")
	void shouldReturnEmptyPhotoMetadata(Path photoPath) throws Exception {
		// when
		var photoMetadata = PhotoMetadataReader.readPhotoMetadata(photoPath);
		log.info("photoMetadata : {}", photoMetadata);

		// then
		assertThat(photoMetadata).isNotNull();
		assertThat(photoMetadata.photoHeight()).isEqualTo(768);
		assertThat(photoMetadata.photoWidth()).isEqualTo(1024);
		assertThat(photoMetadata.creationDate()).isEqualTo(LocalDate.of(1800, 1, 1));
		assertThat(photoMetadata.gpsLongitude()).isEqualTo(0.0d);
		assertThat(photoMetadata.gpsLatitude()).isEqualTo(0.0d);
		assertThat(photoMetadata.cameraModel()).isEqualTo(Strings.EMPTY);

	}

	@Test
	void shouldThrowExceptionWhenPhotoMetadataCannotBeRead() {
		// when / then
		assertThrows(PhotoMetadataReaderException.class, () -> PhotoMetadataReader.readPhotoMetadata(TestConstants.TestFilePath.resolve("Textfile.txt")));
	}
}