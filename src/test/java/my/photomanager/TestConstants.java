package my.photomanager;

import java.nio.file.Path;
import java.time.LocalDate;

public class TestConstants {

	public static Path TestFilePath = Path.of("src", "test", "resources", "Testdata");

	// example 001
	public static Path EXAMPLE_001_PATH = TestFilePath.resolve("example_001.jpg");
	public static Integer EXAMPLE_001_HEIGHT = 750;
	public static Integer EXAMPLE_001_WIDTH = 1000;
	public static LocalDate EXAMPLE_001_CREATION_DATE = LocalDate.of(2024, 1, 1);
	public static String EXAMPLE_001_CAMERA_MODEL = "iPhone 14 Pro";
	public static Double EXAMPLE_001_LONGITUDE = -80.128525;
	public static Double EXAMPLE_001_LATITUDE = 25.7862;
	public static String EXAMPLE_001_COUNTRY = "Vereinigte Staaten von Amerika";
	public static String EXAMPLE_001_CITY = "Miami Beach";

	// example 002
	public static Path EXAMPLE_002_PATH = TestFilePath.resolve("example_002.jpg");
	public static Integer EXAMPLE_002_HEIGHT = 693;
	public static Integer EXAMPLE_002_WIDTH = 1000;
	public static LocalDate EXAMPLE_002_CREATION_DATE = LocalDate.of(2024, 1, 1);
	public static String EXAMPLE_002_CAMERA_MODEL = "Canon EOS-1D Mark IV";
	public static Double EXAMPLE_002_LONGITUDE = 13.376194444444446;
	public static Double EXAMPLE_002_LATITUDE = 52.518680555555555;
	public static String EXAMPLE_002_COUNTRY = "Deutschland";
	public static String EXAMPLE_002_CITY = "Berlin";

	// example 003
	public static Path EXAMPLE_003_PATH = TestFilePath.resolve("example_003.jpg");
	public static Integer EXAMPLE_003_HEIGHT = 750;
	public static Integer EXAMPLE_003_WIDTH = 1000;
	public static LocalDate EXAMPLE_003_CREATION_DATE = LocalDate.of(2025, 1, 1);
	public static String EXAMPLE_003_CAMERA_MODEL = "iPhone 14 Pro";
	public static Double EXAMPLE_003_LONGITUDE = -80.128525;
	public static Double EXAMPLE_003_LATITUDE = 25.7862;
	public static String EXAMPLE_003_COUNTRY = "Vereinigte Staaten von Amerika";
	public static String EXAMPLE_003_CITY = "Miami Beach";

	// example 004
	public static Path EXAMPLE_004_PATH = TestFilePath.resolve("example_004.webp");
	public static Integer EXAMPLE_004_HEIGHT = 768;
	public static Integer EXAMPLE_004_WIDTH = 1024;
	public static LocalDate EXAMPLE_004_CREATION_DATE = LocalDate.of(2025, 1, 1);
	public static String EXAMPLE_004_CAMERA_MODEL = "iPhone 14 Pro";
	public static Double EXAMPLE_004_LONGITUDE = 13.376194444444446;
	public static Double EXAMPLE_004_LATITUDE = 52.518680555555555;
	public static String EXAMPLE_004_COUNTRY = "Deutschland";
	public static String EXAMPLE_004_CITY = "Berlin";
}
