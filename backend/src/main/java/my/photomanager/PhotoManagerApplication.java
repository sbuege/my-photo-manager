package my.photomanager;

import my.photomanager.core.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PhotoManagerApplication implements CommandLineRunner {

	@Autowired
	private LibraryService libraryService;


	public static void main(String[] args) {
		SpringApplication.run(PhotoManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var library = libraryService.createAndSaveLibrary("Test Photos", "/Users/sebastianbuge/git/private/my-photo-manager/backend/launcher/test-photos");
		libraryService.indexLibrary(library.getId());
	}
}
