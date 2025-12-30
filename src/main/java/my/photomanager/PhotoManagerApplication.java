package my.photomanager;

import java.nio.file.Path;
import my.photomanager.library.Library;
import my.photomanager.library.LibrayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PhotoManagerApplication implements CommandLineRunner{

	@Autowired
	private LibrayService libraryService;

	public static void main(String[] args) {
		SpringApplication.run(PhotoManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	//	libraryService.indexLibrary(Library.builder().withName("Test Library").withPath(Path.of("/Users/sebastianbuge/git/private/my-photo-manager/launcher/test-photos").toString()).build());
	}
}
