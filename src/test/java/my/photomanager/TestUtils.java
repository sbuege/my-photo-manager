package my.photomanager;

import my.photomanager.indexer.PhotoIndexer;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

public class TestUtils {

	@TestConfiguration
	public static class PhotoIndexerTestConfig {

		@Bean
		PhotoIndexer photoIndexer() {
			return Mockito.mock(PhotoIndexer.class);
		}
	}
}
