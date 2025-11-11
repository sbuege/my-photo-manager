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
	@DisplayName("should call group and count photos by camera settings")
	void shouldCallGroupAndCountPhotosByCameraSettings() {
		// --- WHEN  ---
		filterOptionService.getCameraSettingsFilters();

		// --- THEN ---
		verify(photoRepository).countPhotosGroupByCameraSettings();
	}

	@Test
	@DisplayName("should call group and count photos by location")
	void shouldCallGroupAndCountPhotosByLocation() {
		// --- WHEN  ---
		filterOptionService.getLocationFilters();

		// --- THEN ---
		verify(photoRepository).countPhotosGroupedByLocation();
	}

	@Test
	@DisplayName("should call group and count photos by creation year")
	void shouldCallGroupAndCountPhotosByCreationYear() {
		// --- WHEN  ---
		filterOptionService.getCreationDateFilters();

		// --- THEN ---
		verify(photoRepository).countPhotosGroupedByCreationYear();
	}

	@Test
	@DisplayName("should call group and count by orientation")
	void shouldCallGroupAndCountByOrientation() {
		// --- WHEN  ---
		filterOptionService.getOrientationFilters();

		// --- THEN ---
		verify(photoRepository).countPhotosGroupByOrientation();
	}
}