package my.photomanager.core.location;

import static my.photomanager.TestDataBuilder.TestAlbumId;
import static my.photomanager.TestDataBuilder.TestLocationCity;
import static my.photomanager.TestDataBuilder.TestLocationCountry;
import static my.photomanager.TestDataBuilder.TestLocationId;
import static my.photomanager.TestDataBuilder.TestLocationLatitude;
import static my.photomanager.TestDataBuilder.TestLocationLongitude;
import static my.photomanager.TestDataBuilder.buildLocation;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;
import my.photomanager.utils.gpsResolver.GpsResolverException;
import my.photomanager.utils.metaDataParser.Metadata;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

	@Mock
	private LocationRepository repository;

	@InjectMocks
	private LocationService service;

	@Test
	void shouldReturnAllLocations() {
		// --- WHEN
		service.getAllLocations();

		// --- THEN
		verify(repository).findAll();
	}

	@Nested
	class CreateLocationTest {

		@Test
		void shouldCreateAndSaveLocation() {
			// --- GIVEN ---
			when(repository.existsByCountryAndCity(anyString(), anyString())).thenReturn(false);

			// --- WHEN ---
			service.createAndSaveLocation(TestLocationCountry, TestLocationCity);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Location.class));
		}

		public static Stream<Metadata> metaDataProvider() {
			return Stream.of(
					new Metadata(0, 0, Strings.EMPTY, null, TestLocationLongitude, TestLocationLatitude),
					new Metadata(0, 0, Strings.EMPTY, null, 0.0d, 0.0d)
			);
		}

		@ParameterizedTest
		@MethodSource("metaDataProvider")
		void shouldCreateAndSaveLocationFromMetaData(Metadata metaData) throws GpsResolverException {
			// --- GIVEN ---
			when(repository.existsByCountryAndCity(anyString(), anyString())).thenReturn(false);

			// --- WHEN ---
			service.createAndSaveLocation(metaData);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Location.class));
		}

		@Test
		void shouldReturnExistingLocation() {
			// --- GIVEN ---
			when(repository.existsByCountryAndCity(anyString(), anyString())).thenReturn(true);
			when(repository.findByCountryAndCity(anyString(), anyString())).thenReturn(Optional.of(buildLocation()));

			// --- WHEN ---
			service.createAndSaveLocation(TestLocationCountry, TestLocationCity);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Location.class));
		}
	}

	@Nested
	class EditLocationTest {

		private final String newLocationCountryName = "newLocationCountryName";
		private final String newLocationCityName = "newLocationCityName";

		@Test
		void shouldEditAndSaveLocation() {
			// --- GIVEN ---
			when(repository.findById(TestLocationId)).thenReturn(Optional.of(buildLocation()));
			when(repository.existsByCountryAndCity(anyString(), anyString())).thenReturn(false);

			// --- WHEN ---
			service.editLocation(TestLocationId, newLocationCountryName, newLocationCityName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Location.class));
		}

		@Test
		void shouldReturnExistingLocation() {
			// --- GIVEN ---
			when(repository.findById(TestLocationId)).thenReturn(Optional.of(buildLocation()));
			when(repository.existsByCountryAndCity(newLocationCountryName, newLocationCityName)).thenReturn(true);
			when(repository.findByCountryAndCity(newLocationCountryName, newLocationCityName)).thenReturn(
					Optional.of(buildLocation(newLocationCountryName, newLocationCityName)));

			// --- WHEN ---
			service.editLocation(TestLocationId, newLocationCountryName, newLocationCityName);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Location.class));
		}

		@Test
		void shouldThrowExceptionWhenLocationDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestLocationId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(LocationServiceException.class, () -> service.editLocation(TestLocationId, newLocationCountryName, newLocationCityName));
			verify(repository, never()).saveAndFlush(any(Location.class));
		}
	}

	@Nested
	class DeleteLocationTest {

		@Test
		void shouldDeleteLocation() {
			// --- GIVEN ---
			when(repository.findById(TestLocationId)).thenReturn(Optional.of(buildLocation()));

			// --- WHEN ---
			service.deleteLocation(TestLocationId);

			// --- THEN ---
			verify(repository).deleteById(TestAlbumId);
		}

		@Test
		void shouldThrowExceptionWhenLocationsDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestLocationId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(LocationServiceException.class, () -> service.deleteLocation(TestLocationId));
			verify(repository, never()).deleteById(TestLocationId);
		}
	}
}