package my.photomanager.photo.location;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhotoLocationServiceTest {

	// TEST DATA
	final String TEST_PHOTO_LOCATION_COUNTRY = "TestPhotoLocationCountry";
	final String TEST_PHOTO_LOCATION_CITY = "TestPhotoLocationCity";
	final PhotoLocation TEST_PHOTO_LOCATION = new PhotoLocation(TEST_PHOTO_LOCATION_COUNTRY, TEST_PHOTO_LOCATION_CITY);

	@Mock
	private PhotoLocationRepository photoLocationRepository;

	@InjectMocks
	private PhotoLocationService photoLocationService;

	@Test
	@DisplayName("should call saveAndFlush when location does not exist")
	void shouldCallSaveAndFlush() {
		// given
		when(photoLocationRepository.findByCountryAndCity(TEST_PHOTO_LOCATION_COUNTRY, TEST_PHOTO_LOCATION_CITY)).thenReturn(Optional.empty());

		// when
		photoLocationService.saveOrGetPhotoLocation(TEST_PHOTO_LOCATION);

		// then
		verify(photoLocationRepository).saveAndFlush(TEST_PHOTO_LOCATION);
	}

	@Test
	@DisplayName("should never all saveAndFlush when location exists already")
	void shouldNeveCallSaveAndFlush() {
		// given
		when(photoLocationRepository.findByCountryAndCity(TEST_PHOTO_LOCATION_COUNTRY, TEST_PHOTO_LOCATION_CITY)).thenReturn(Optional.of(TEST_PHOTO_LOCATION));

		// when
		photoLocationService.saveOrGetPhotoLocation(TEST_PHOTO_LOCATION);

		// then
		verify(photoLocationRepository, never()).saveAndFlush(TEST_PHOTO_LOCATION);
	}
}