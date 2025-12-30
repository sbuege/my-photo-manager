package my.photomanager;

import static my.photomanager.TestDataBuilder.EXAMPLE_001_CREATION_DATE;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_PHOTO;
import static my.photomanager.TestDataBuilder.EXAMPLE_002_PHOTO;
import static my.photomanager.TestDataBuilder.EXAMPLE_003_PHOTO;
import static my.photomanager.TestDataBuilder.EXAMPLE_004_CREATION_DATE;
import static my.photomanager.TestDataBuilder.EXAMPLE_004_PHOTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import my.photomanager.config.PhotoManagerConfiguration;
import my.photomanager.filterOption.FilterProperties;
import my.photomanager.library.Library;
import my.photomanager.library.LibrayService;
import my.photomanager.photo.Photo;
import my.photomanager.photo.PhotoRepository;
import my.photomanager.photo.PhotoService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestInstance(PER_CLASS)
@Profile("test")
@TestPropertySource(properties = {"photo.sourceFolder=src/test/resources/Testdata/"})
@Log4j2
class PhotoIntegrationTests {

	@Autowired
	private PhotoManagerConfiguration configuration;

	@Autowired
	private LibrayService libraryService;

	@Autowired
	private PhotoService service;

	@Autowired
	private PhotoRepository repository;

	@BeforeAll
	void setUp() {
		// --- GIVEN ---
		repository.deleteAll();
		assertThat(repository.count()).isZero();

		//libraryService.indexLibrary(Library.builder().withName("Test Library").withPath("src/test/resources/Testdata/").build());

		Awaitility.await()
				.atMost(5, TimeUnit.SECONDS)
				.until(() -> repository.count() == 4);
	}

	@Test
	@DisplayName("should index and save photos")
	void shouldIndexAndSavePhotos() {
		// --- WHEN ---
		var photos = repository.findAll();

		// --- THEN ---
		assertThat(photos)
				.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "hashValue", "fileName", "location.id", "cameraModel.id", "orientation.id")
				.containsExactlyInAnyOrderElementsOf(List.of(EXAMPLE_001_PHOTO, EXAMPLE_002_PHOTO, EXAMPLE_003_PHOTO, EXAMPLE_004_PHOTO));
	}

	static Stream<Arguments> testFilterPropertiesProvider() {
		return Stream.of(
				// location
				Arguments.of(FilterProperties.builder()
								.withLocationIDs(List.of(1L))
								.build(),
						List.of(EXAMPLE_001_PHOTO, EXAMPLE_003_PHOTO)),

				// creation date
				Arguments.of(FilterProperties.builder()
								.withStartDate(EXAMPLE_001_CREATION_DATE)
								.build(),
						List.of(EXAMPLE_001_PHOTO, EXAMPLE_002_PHOTO)
				),

				Arguments.of(FilterProperties.builder()
								.withEndDate(EXAMPLE_004_CREATION_DATE)
								.build(),
						List.of(EXAMPLE_001_PHOTO, EXAMPLE_002_PHOTO, EXAMPLE_003_PHOTO, EXAMPLE_004_PHOTO)
				),

				Arguments.of(FilterProperties.builder()
								.withStartDate(EXAMPLE_001_CREATION_DATE)
								.withEndDate(EXAMPLE_004_CREATION_DATE)
								.build(),
						List.of(EXAMPLE_001_PHOTO, EXAMPLE_002_PHOTO, EXAMPLE_003_PHOTO, EXAMPLE_004_PHOTO)

				),

				// camera model
				Arguments.of(FilterProperties.builder()
								.withCameraModelIds(List.of(1L))
								.build(),
						List.of(EXAMPLE_001_PHOTO, EXAMPLE_003_PHOTO, EXAMPLE_004_PHOTO)),

				// orientation
				Arguments.of(FilterProperties.builder()
								.withOrientationIDs(List.of(1L))
								.build(),
						List.of(EXAMPLE_001_PHOTO, EXAMPLE_002_PHOTO, EXAMPLE_003_PHOTO, EXAMPLE_004_PHOTO))

		);
	}

	@ParameterizedTest
	@MethodSource("testFilterPropertiesProvider")
	void shouldFilterPhotos(FilterProperties filterProperties, List<Photo> expectedPhotos) {
		// --- WHEN ---
		var photoIDs = service.filterPhotos(filterProperties);
		var photos = repository.findAll()
				.stream()
				.filter(photo -> photoIDs.contains(photo.getId()))
				.toList();

		// --- THEN ---
		assertThat(photos)
				.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "hashValue", "fileName", "location.id", "cameraModel.id", "orientation.id")
				.containsExactlyInAnyOrderElementsOf(expectedPhotos);

	}
}