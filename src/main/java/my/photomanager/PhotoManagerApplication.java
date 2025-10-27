package my.photomanager;

import my.photomanager.indexer.PhotoIndexer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PhotoManagerApplication implements CommandLineRunner {

	private final PhotoIndexer photoIndexer;

	protected PhotoManagerApplication(PhotoIndexer photoIndexer) {
		this.photoIndexer = photoIndexer;
	}


	@Override
	public void run(String... args) throws Exception {
		photoIndexer.indexPhotos();
	}

	public static void main(String[] args) {
		SpringApplication.run(PhotoManagerApplication.class, args);
	}
}
