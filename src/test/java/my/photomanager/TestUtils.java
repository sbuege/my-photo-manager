package my.photomanager;

import my.photomanager.filterOption.FilterOptionService;
import my.photomanager.indexer.PhotoIndexer;
import my.photomanager.photo.PhotoService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

public class TestUtils {

	@TestConfiguration
	public static class PhotoIndexerMock {

		@Bean
		PhotoIndexer photoIndexer() {
			return Mockito.mock(PhotoIndexer.class);
		}
	}

	@TestConfiguration
	public static class PhotoServiceMock {

		@Bean
		PhotoService photoService() {
			return Mockito.mock(PhotoService.class);
		}
	}

	@TestConfiguration
	public static class FilterServiceMock {

		@Bean
		FilterOptionService filterService() {
			return Mockito.mock(FilterOptionService.class);
		}
	}
}
