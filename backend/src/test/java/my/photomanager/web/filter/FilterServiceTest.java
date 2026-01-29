package my.photomanager.web.filter;

import static org.mockito.Mockito.verify;

import my.photomanager.core.photo.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilterServiceTest {

	@Mock
	private PhotoRepository repository;

	@InjectMocks
	private FilterService service;

	@Test
	void shouldReturnCameraModelStatistics() {
		// --- WHEN
		service.getCameraModelStatistics();

		// --- THEN
		verify(repository).groupPhotosByCameraModel();
	}

	@Test
	void shouldReturnLocationStatistics() {
		// --- WHEN
		service.getLocationStatistics();

		// --- THEN
		verify(repository).groupPhotosByLocation();
	}

	@Test
	void shouldReturnCreationYearStatistics() {
		// --- WHEN
		service.getCreationYearStatistics();

		// --- THEN
		verify(repository).groupPhotosByCreationYear();
	}

	@Test
	void shouldReturnOrientationStatistics() {
		// --- WHEN
		service.getOrientationStatistics();

		// --- THEN
		verify(repository).groupPhotosByOrientation();
	}
}