package my.photomanager.photo.location;

import static my.photomanager.TestDataBuilder.TEST_LOCATION_CITY;
import static my.photomanager.TestDataBuilder.TEST_LOCATION_COUNTRY;
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
class LocationServiceTest {

	// TEST DATA
	final Location TEST_LOCATION = TestDataBuilder.TestPhotoLocationBuilder.build();

	@Mock
	private LocationRepository repository;

	@InjectMocks
	private LocationService service;

	@Test
	@DisplayName("should call saveAndFlush when location does not exist")
	void shouldCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByCountryAndCity(TEST_LOCATION_COUNTRY, TEST_LOCATION_CITY)).thenReturn(Optional.empty());

		// --- WHEN ---
		service.saveOrGetLocation(TEST_LOCATION);

		// --- THEN ---
		verify(repository).saveAndFlush(TEST_LOCATION);
	}

	@Test
	@DisplayName("should not call saveAndFlush when location already exists")
	void shouldNotCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByCountryAndCity(TEST_LOCATION_COUNTRY, TEST_LOCATION_CITY)).thenReturn(Optional.of(TEST_LOCATION));

		// --- WHEN ---
		service.saveOrGetLocation(TEST_LOCATION);

		// --- THEN ---
		verify(repository, never()).saveAndFlush(TEST_LOCATION);
	}
}