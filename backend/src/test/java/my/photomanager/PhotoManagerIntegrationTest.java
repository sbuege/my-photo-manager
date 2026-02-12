package my.photomanager;

import static my.photomanager.TestDataBuilder.TestFilePath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import my.photomanager.core.library.LibraryService;
import my.photomanager.core.location.LocationStatistic;
import my.photomanager.core.orientation.OrientationStatistic;
import my.photomanager.core.photo.CreationYearStatistic;
import my.photomanager.core.photo.PhotoService;
import my.photomanager.web.filter.FilterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(PER_CLASS)
public class PhotoManagerIntegrationTest {

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private FilterService filterService;

	@BeforeAll
	void setUp() throws IOException {
		// --- GIVEN ---
		var library = libraryService.createAndSaveLibrary("TestLibrary", TestFilePath.toString());

		// --- WHEN
		var indexStartTimeStamp = Instant.now();
		library = libraryService.indexLibrary(library.getId());

		// --- THEN
		assertThat(library.getNumberOfPhotos()).isEqualTo(4);
		assertThat(library.getLastIndexAt()).isAfter(indexStartTimeStamp);
	}

	@Test
	void shouldReturnCameraStatistic() throws IOException {
		// --- WHEN
		var cameraStatistic = filterService.getCameraModelStatistics();

		// --- THEN
		assertThat(cameraStatistic).isNotEmpty();
		//TODO check content
	}

	@Test
	void shouldReturnLocationsStatistic(){
		// --- WHEN ---
		var locationStatistics = filterService.getLocationStatistics();

		// --- THEN
		assertThat(locationStatistics).isNotEmpty();
		//TODO check content
	}

	@Test
	void shouldReturnOrientationStatistic(){
		// --- WHEN ---
		var orientationStatistics = filterService.getOrientationStatistics();

		// --- THEN
		assertThat(orientationStatistics).isNotEmpty();
		//TODO check content
	}

	@Test
	void shouldReturnCreationYearStatistic(){
		// --- WHEN ---
		var creationYearStatistics = filterService.getCreationYearStatistics();

		// --- THEN
		assertThat(creationYearStatistics).isNotEmpty();
		//TODO check content
	}
}
