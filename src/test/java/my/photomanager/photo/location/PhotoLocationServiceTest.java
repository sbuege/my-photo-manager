package my.photomanager.photo.location;

import static my.photomanager.TestDataBuilder.TEST_PHOTO_LOCATION_CITY;
import static my.photomanager.TestDataBuilder.TEST_PHOTO_LOCATION_COUNTRY;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhotoLocationServiceTest {

	// TEST DATA
	final PhotoLocation TEST_PHOTO_LOCATION = TestDataBuilder.TestPhotoLocationBuilder.build();

	@Mock
	private PhotoLocationRepository photoLocationRepository;

	@InjectMocks
	private PhotoLocationService photoLocationService;

	@Test
	@DisplayName("should call saveAndFlush when photo location does not exist")
	void shouldCallSaveAndFlush() {
		// --- GIVEN ---
		when(photoLocationRepository.findByCountryAndCity(TEST_PHOTO_LOCATION_COUNTRY, TEST_PHOTO_LOCATION_CITY)).thenReturn(Optional.empty());

		// --- WHEN ---
		photoLocationService.saveOrGetPhotoLocation(TEST_PHOTO_LOCATION);

		// --- THEN ---
		verify(photoLocationRepository).saveAndFlush(TEST_PHOTO_LOCATION);
	}

	@Test
	@DisplayName("should not call saveAndFlush when photo location already exists")
	void shouldNotCallSaveAndFlush() {
		// --- GIVEN ---
		when(photoLocationRepository.findByCountryAndCity(TEST_PHOTO_LOCATION_COUNTRY, TEST_PHOTO_LOCATION_CITY)).thenReturn(Optional.of(TEST_PHOTO_LOCATION));

		// --- WHEN ---
		photoLocationService.saveOrGetPhotoLocation(TEST_PHOTO_LOCATION);

		// --- THEN ---
		verify(photoLocationRepository, never()).saveAndFlush(TEST_PHOTO_LOCATION);
	}
}