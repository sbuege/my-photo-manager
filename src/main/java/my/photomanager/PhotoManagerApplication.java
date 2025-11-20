package my.photomanager;

import my.photomanager.indexer.Indexer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PhotoManagerApplication implements CommandLineRunner {

	private final Indexer photoIndexer;

	protected PhotoManagerApplication(Indexer photoIndexer) {
		this.photoIndexer = photoIndexer;
	}


	@Override
	public void run(String... args) {
		photoIndexer.indexPhotos();
	}

	public static void main(String[] args) {
		SpringApplication.run(PhotoManagerApplication.class, args);
	}
}
