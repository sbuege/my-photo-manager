package my.photomanager.filterOption;

import static org.mockito.Mockito.verify;

import my.photomanager.photo.PhotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilterOptionServiceTest {

	@Mock
	private PhotoRepository photoRepository;

	@InjectMocks
	private FilterOptionService filterOptionService;

	@Test
	@DisplayName("should call group photos by camera model")
	void shouldCallGroupPhotosByCameraModel() {
		// --- WHEN  ---
		filterOptionService.getCameraModelFilters();

		// --- THEN ---
		verify(photoRepository).groupPhotosByCameraModel();
	}

	@Test
	@DisplayName("should call group photos by location")
	void shouldCallGroupPhotosByLocation() {
		// --- WHEN  ---
		filterOptionService.getLocationFilters();

		// --- THEN ---
		verify(photoRepository).groupPhotosByLocation();
	}

	@Test
	@DisplayName("should call group photos by creation year")
	void shouldCallGroupPhotosByCreationYear() {
		// --- WHEN  ---
		filterOptionService.getCreationDateFilters();

		// --- THEN ---
		verify(photoRepository).groupPhotosByCreationYear();
	}

	@Test
	@DisplayName("should call group by orientation")
	void shouldCallGroupPhotosByOrientation() {
		// --- WHEN  ---
		filterOptionService.getOrientationFilters();

		// --- THEN ---
		verify(photoRepository).groupPhotosByOrientation();
	}
}