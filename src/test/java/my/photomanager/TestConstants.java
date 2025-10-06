package my.photomanager;

import java.nio.file.Path;
import java.util.stream.Stream;

public class TestConstants {

	public static Path TestFilePath = Path.of("src", "test", "resources", "Testdata");

	public static Stream<Path> jpegWithExifDataFileProvider() {
		return Stream.of(TestFilePath.resolve(Path.of("Berlin_WithExifData.jpg")));
	}

	public static Stream<Path> jpegWithoutExifDataFileProvider() {
		return Stream.of(TestFilePath.resolve(Path.of("Berlin_WithoutExifData.jpg")));
	}

	public static Stream<Path> webWithExifDataFileProvider() {
		return Stream.of(TestFilePath.resolve(Path.of("Berlin_WithExifData.webp")));
	}

	public static Stream<Path> webWithoutExifDataFileProvider() {
		return Stream.of(TestFilePath.resolve(Path.of("Berlin_WithoutExifData.webp")));
	}

}
