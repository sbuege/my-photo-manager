package my.photomanager.photo.location;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhotoLocationServiceTest {

	private PhotoLocationService photoLocationService;

	@Mock
	private PhotoLocationRepository photoLocationRepository;

	@BeforeEach
	void setUp() {
		photoLocationService = new PhotoLocationService(photoLocationRepository);
	}

	@Test
	void shouldSavePhotoLocationWhenNotExisting() {
		// given
		final var photoLocationCountry = "TestPhotoCountry";
		final var photoLocationCity = "TestPhotoCity";
		var photoLocation = new PhotoLocation(photoLocationCountry, photoLocationCity);
		when(photoLocationRepository.findByCountryAndCity(photoLocationCountry, photoLocationCity)).thenReturn(Optional.empty());

		// when
		photoLocationService.saveOrGetPhotoLocation(photoLocation);

		// then
		verify(photoLocationRepository).saveAndFlush(photoLocation);
	}

	@Test
	void shouldReturnExistingPhotoLocationWhenAlreadyExists() {
		// given
		final var photoLocationCountry = "TestPhotoCountry";
		final var photoLocationCity = "TestPhotoCity";
		var photoLocation = new PhotoLocation(photoLocationCountry, photoLocationCity);
		when(photoLocationRepository.findByCountryAndCity(photoLocationCountry, photoLocationCity)).thenReturn(Optional.of(photoLocation));

		// when
		photoLocationService.saveOrGetPhotoLocation(photoLocation);

		// then
		verify(photoLocationRepository, never()).saveAndFlush(photoLocation);
	}
}