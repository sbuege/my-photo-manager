package my.photomanager;

import static my.photomanager.TestConstants.EXAMPLE_001_CAMERA_MODEL;
import static my.photomanager.TestConstants.EXAMPLE_001_CITY;
import static my.photomanager.TestConstants.EXAMPLE_001_COUNTRY;
import static my.photomanager.TestConstants.EXAMPLE_001_CREATION_DATE;
import static my.photomanager.TestConstants.EXAMPLE_001_HEIGHT;
import static my.photomanager.TestConstants.EXAMPLE_001_WIDTH;
import static my.photomanager.TestConstants.EXAMPLE_002_CAMERA_MODEL;
import static my.photomanager.TestConstants.EXAMPLE_002_CITY;
import static my.photomanager.TestConstants.EXAMPLE_002_COUNTRY;
import static my.photomanager.TestConstants.EXAMPLE_002_CREATION_DATE;
import static my.photomanager.TestConstants.EXAMPLE_002_HEIGHT;
import static my.photomanager.TestConstants.EXAMPLE_002_WIDTH;
import static my.photomanager.TestConstants.EXAMPLE_003_CAMERA_MODEL;
import static my.photomanager.TestConstants.EXAMPLE_003_CITY;
import static my.photomanager.TestConstants.EXAMPLE_003_COUNTRY;
import static my.photomanager.TestConstants.EXAMPLE_003_CREATION_DATE;
import static my.photomanager.TestConstants.EXAMPLE_003_HEIGHT;
import static my.photomanager.TestConstants.EXAMPLE_003_WIDTH;
import static my.photomanager.TestConstants.EXAMPLE_004_CAMERA_MODEL;
import static my.photomanager.TestConstants.EXAMPLE_004_CITY;
import static my.photomanager.TestConstants.EXAMPLE_004_COUNTRY;
import static my.photomanager.TestConstants.EXAMPLE_004_CREATION_DATE;
import static my.photomanager.TestConstants.EXAMPLE_004_HEIGHT;
import static my.photomanager.TestConstants.EXAMPLE_004_WIDTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import my.photomanager.filter.FilterProperties;
import my.photomanager.indexer.PhotoIndexer;
import my.photomanager.photo.Photo;
import my.photomanager.photo.PhotoRepository;
import my.photomanager.photo.PhotoService;
import org.assertj.core.groups.Tuple;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"photo.sourceFolder=src/test/resources/Testdata/"})
@Log4j2
class PhotoIntegrationTests {

	@Autowired
	private PhotoManagerConfiguration configuration;

	@Autowired
	private PhotoIndexer photoIndexer;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoRepository photoRepository;

	@BeforeEach
	void setUp() {
		// given
		Awaitility.await()
				.atMost(5, TimeUnit.SECONDS)
				.until(() -> photoRepository.count() == 4);
	}

	@Test
	void shouldIndexAndCreatePhotos() {
		// when
		var photos = photoRepository.findAll();

		// then
		assertThat(photos).extracting(Photo::getHeight,
						Photo::getWidth,
						Photo::getCreationDate,
						p -> p.getLocation() != null ? p.getLocation()
								.getCountry() : null,
						p -> p.getLocation() != null ? p.getLocation()
								.getCity() : null,
						p -> p.getCameraSettings() != null ? p.getCameraSettings()
								.getCameraModelName() : null

				)
				.containsExactlyInAnyOrder(
						tuple(EXAMPLE_001_HEIGHT, EXAMPLE_001_WIDTH, EXAMPLE_001_CREATION_DATE, EXAMPLE_001_COUNTRY, EXAMPLE_001_CITY,
								EXAMPLE_001_CAMERA_MODEL),
						tuple(EXAMPLE_002_HEIGHT, EXAMPLE_002_WIDTH, EXAMPLE_002_CREATION_DATE, EXAMPLE_002_COUNTRY, EXAMPLE_002_CITY,
								EXAMPLE_002_CAMERA_MODEL),
						tuple(EXAMPLE_003_HEIGHT, EXAMPLE_003_WIDTH, EXAMPLE_003_CREATION_DATE, EXAMPLE_003_COUNTRY, EXAMPLE_003_CITY,
								EXAMPLE_003_CAMERA_MODEL),
						tuple(EXAMPLE_004_HEIGHT, EXAMPLE_004_WIDTH, EXAMPLE_004_CREATION_DATE, EXAMPLE_004_COUNTRY, EXAMPLE_004_CITY,
								EXAMPLE_004_CAMERA_MODEL));


	}

	static Stream<Arguments> testDataProvider() {
		return Stream.of(
				// location
				Arguments.of(FilterProperties.builder()
								.locationCountries(List.of(EXAMPLE_001_COUNTRY))
								.locationCities(List.of(EXAMPLE_001_CITY))
								.build(),
						List.of(
								tuple(EXAMPLE_001_CREATION_DATE, EXAMPLE_001_COUNTRY, EXAMPLE_001_CITY,
										EXAMPLE_001_CAMERA_MODEL),
								tuple(EXAMPLE_003_CREATION_DATE, EXAMPLE_003_COUNTRY, EXAMPLE_003_CITY,
										EXAMPLE_003_CAMERA_MODEL)
						)),
				// creation date
				Arguments.of(FilterProperties.builder()
								.startDate(EXAMPLE_001_CREATION_DATE)
								.build(),
						List.of(tuple(EXAMPLE_001_CREATION_DATE, EXAMPLE_001_COUNTRY, EXAMPLE_001_CITY,
										EXAMPLE_001_CAMERA_MODEL),
								tuple(EXAMPLE_002_CREATION_DATE, EXAMPLE_002_COUNTRY, EXAMPLE_002_CITY,
										EXAMPLE_002_CAMERA_MODEL)
						)
				),

				Arguments.of(FilterProperties.builder()
								.endDate(EXAMPLE_004_CREATION_DATE)
								.build(),
						List.of(tuple(EXAMPLE_001_CREATION_DATE, EXAMPLE_001_COUNTRY, EXAMPLE_001_CITY,
										EXAMPLE_001_CAMERA_MODEL),
								tuple(EXAMPLE_002_CREATION_DATE, EXAMPLE_002_COUNTRY, EXAMPLE_002_CITY,
										EXAMPLE_002_CAMERA_MODEL),
								tuple(EXAMPLE_003_CREATION_DATE, EXAMPLE_003_COUNTRY, EXAMPLE_003_CITY,
										EXAMPLE_003_CAMERA_MODEL),
								tuple(EXAMPLE_004_CREATION_DATE, EXAMPLE_004_COUNTRY, EXAMPLE_004_CITY,
										EXAMPLE_004_CAMERA_MODEL)

						)
				),

				Arguments.of(FilterProperties.builder()
								.startDate(EXAMPLE_001_CREATION_DATE)
								.endDate(EXAMPLE_004_CREATION_DATE)
								.build(),
						List.of(
								tuple(EXAMPLE_001_CREATION_DATE, EXAMPLE_001_COUNTRY, EXAMPLE_001_CITY,
										EXAMPLE_001_CAMERA_MODEL),
								tuple(EXAMPLE_002_CREATION_DATE, EXAMPLE_002_COUNTRY, EXAMPLE_002_CITY,
										EXAMPLE_002_CAMERA_MODEL),
								tuple(EXAMPLE_003_CREATION_DATE, EXAMPLE_003_COUNTRY, EXAMPLE_003_CITY,
										EXAMPLE_003_CAMERA_MODEL),
								tuple(EXAMPLE_004_CREATION_DATE, EXAMPLE_004_COUNTRY, EXAMPLE_004_CITY,
										EXAMPLE_004_CAMERA_MODEL)
						)
				),

				// camera model
				Arguments.of(FilterProperties.builder()
								.cameraModels(List.of(EXAMPLE_001_CAMERA_MODEL))
								.build(),
						List.of(
								tuple(EXAMPLE_001_CREATION_DATE, EXAMPLE_001_COUNTRY, EXAMPLE_001_CITY,
										EXAMPLE_001_CAMERA_MODEL),
								tuple(EXAMPLE_003_CREATION_DATE, EXAMPLE_003_COUNTRY, EXAMPLE_003_CITY,
										EXAMPLE_003_CAMERA_MODEL)
						))
		);

	}

	@ParameterizedTest
	@MethodSource("testDataProvider")
	void shouldFilterPhotos(FilterProperties filterProperties, List<Tuple> expected) {
		// when
		var photoIDs = photoService.filterPhotos(filterProperties);
		var photos = photoRepository.findAll()
				.stream()
				.filter(photo -> photoIDs.contains(photo.getId()))
				.toList();

		// then
		assertThat(photos).extracting(Photo::getCreationDate,
						p -> p.getLocation() != null ? p.getLocation()
								.getCountry() : null,
						p -> p.getLocation() != null ? p.getLocation()
								.getCity() : null,
						p -> p.getCameraSettings() != null ? p.getCameraSettings()
								.getCameraModelName() : null
				)
				.containsExactlyInAnyOrderElementsOf(expected);

	}
}