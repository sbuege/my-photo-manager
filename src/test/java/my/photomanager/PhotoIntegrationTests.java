package my.photomanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import my.photomanager.filter.FilterProperties;
import my.photomanager.indexer.PhotoIndexer;
import my.photomanager.photo.Photo;
import my.photomanager.photo.PhotoRepository;
import my.photomanager.photo.PhotoService;
import my.photomanager.photo.album.PhotoAlbumRepository;
import my.photomanager.photo.cameraSettings.CameraSettingsService;
import my.photomanager.photo.location.PhotoLocation;
import my.photomanager.photo.location.PhotoLocationRepository;
import my.photomanager.photo.location.PhotoLocationService;
import org.apache.logging.log4j.util.Strings;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"photo.sourceFolder=src/test/resources/Testdata"})

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

	@Autowired
	private PhotoLocationService locationService;

	@Autowired
	private PhotoLocationRepository photoLocationRepository;

	@Autowired
	private CameraSettingsService cameraSettingsService;

	@Autowired
	private PhotoAlbumRepository photoAlbumRepository;

	@BeforeEach
	void setUp() {
		// given
		Awaitility.await()
				.atMost(5, TimeUnit.SECONDS)
				.until(() -> photoRepository.count() == 3);
	}

	@Test
	void shouldIndexAndCreatePhotos() {
		// when
		var photos = photoRepository.findAll();

		// then
		assertThat(photos).extracting(Photo::getHeight,
						Photo::getWidth,
						//TODO check creation date
						//Photo::getCreationDate,
						p -> p.getLocation()
								.getCountry(),
						p -> p.getLocation()
								.getCity(),
						p -> p.getCameraSettings()
								.getCameraModelName()
				)
				.containsExactlyInAnyOrder(
						tuple(768, 1024, "Deutschland", "Berlin", Strings.EMPTY),
						tuple(1536, 1024, "Frankreich", "Paris", Strings.EMPTY),
						tuple(768, 1024, Strings.EMPTY, Strings.EMPTY, Strings.EMPTY));
	}

	@Nested
	class FilterTest {

		@Test
		void shouldFilterPhotosByLocation() {
			// given
			var locationIDs = photoLocationRepository.findAll()
					.stream()
					.filter(photoLocation -> !photoLocation.getCountry()
							.isBlank())
					.map(PhotoLocation::getId)
					.toList();
			log.info("location ids: {}", locationIDs);

			// when
			var photos = photoService.filterPhotos(new FilterProperties(locationIDs, null, null));

			// then
			assertThat(photos.size()).isEqualTo(2);
			assertThat(photos).extracting(
							p -> p.getLocation()
									.getCountry(),
							p -> p.getLocation()
									.getCity())
					.containsExactlyInAnyOrder(
							tuple("Deutschland", "Berlin"),
							tuple("Frankreich", "Paris"));
		}

		@Test
		@Disabled
		void shouldFilterPhotosFromStartDate() {

		}

		@Test
		@Disabled
		void shouldFilterPhotosUntilEndDate() {

		}

		@Test
		@Disabled
		void shouldFilterPhotosBetweenStartAndEndDate() {
		}


		@Test
		@Disabled
		void shouldFilterByCreationDate() {
		}

		@Test
		@Disabled
		void shouldFilterByCreationStartDate() {
		}
	}


}