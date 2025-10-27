package my.photomanager.filter;

import static org.mockito.Mockito.verify;

import my.photomanager.photo.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilterServiceTest {

	private FilterService filterService;

	@Mock
	private PhotoRepository photoRepository;

	@BeforeEach
	void setUp() {
		filterService = new FilterService(photoRepository);
	}

	@Test
	void shouldReturnCameraSettingsFilters() {
		// when
		filterService.getCameraSettingsFilters();

		// then
		verify(photoRepository).countPhotosGroupByCameraSettings();
	}

	@Test
	void shouldReturnLocationFilters() {
		// when
		filterService.getLocationFilters();

		// then
		verify(photoRepository).countPhotosGroupedByLocation();
	}

	@Test
	void shouldReturnCreationDateFilters() {
		// when
		filterService.getCreationDateFilters();

		// then
		verify(photoRepository).countPhotosGroupedByCreationYear();
	}

	@Test
	void shouldReturnOrientationFilters() {
		// when
		filterService.getOrientationFilters();

		// then
		verify(photoRepository).countPhotosGroupByOrientation();
	}
}