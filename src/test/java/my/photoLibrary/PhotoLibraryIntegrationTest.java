package my.photoLibrary;

import static my.photomanager.TestDataBuilder.TestFilePath;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Instant;
import my.photoLibrary.core.library.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PhotoLibraryIntegrationTest {

	@Autowired
	private LibraryService libraryService;


	@Test
	void shouldIndexPhotos() throws IOException {
		// --- GIVEN ---
		var library = libraryService.createAndSaveLibrary("TestLibrary", TestFilePath.toString());

		// --- WHEN
		var indexStartTimeStamp = Instant.now();
		library = libraryService.indexLibrary(library.getId());

		// --- THAT
		assertThat(library.getNumberOfPhotos()).isEqualTo(4);
		assertThat(library.getLastIndexAt()).isAfter(indexStartTimeStamp);
	}

}
