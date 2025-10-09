package my.photomanager.indexer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import my.photomanager.PhotoManagerConfiguration;
import my.photomanager.photo.Photo;
import my.photomanager.photo.PhotoRepository;
import my.photomanager.photo.cameraSettings.CameraSettingsService;
import my.photomanager.photo.location.PhotoLocationService;
import org.apache.logging.log4j.util.Strings;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"photo.sourceFolder=src/test/resources/Testdata"})

@Log4j2
class PhotoIndexerTest {

	@Autowired
	private PhotoIndexer photoIndexer;

	@Autowired
	private PhotoManagerConfiguration configuration;

	@Autowired
	private PhotoLocationService locationService;

	@Autowired
	private CameraSettingsService cameraSettingsService;

	@Autowired
	private PhotoRepository photoRepository;

	@Test
	void shouldIndexPhotos() {
		// given
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		Awaitility.await()
				.atMost(5, TimeUnit.SECONDS)
				.until(() -> photoRepository.count() == 2);


		var photos = photoRepository.findAll();

		photoRepository.findAll()
				.forEach(System.out::println);

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
						tuple(768, 1024, Strings.EMPTY, Strings.EMPTY, Strings.EMPTY));
	}
}