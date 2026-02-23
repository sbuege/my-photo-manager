package my.photomanager.core.location;

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

import java.util.Optional;

import static my.photomanager.TestDataBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
            when(repository.saveAndFlush(any(Location.class))).thenReturn(buildLocation());

            // --- WHEN ---
            service.createAndSaveLocation(TestLocationCountry, TestLocationCity);

            // --- THEN ---
            verify(repository).saveAndFlush(any(Location.class));
        }

        @ParameterizedTest
        @MethodSource("my.photomanager.TestDataBuilder#emptyNameProvider")
        void shouldReturnEmptyLocationWhenCountryNameIsEmpty(String countryName) {
            // --- WHEN ---
            assertThat(service.createAndSaveLocation(countryName, TestLocationCity)).isEmpty();

            // --- THEN ---
            verify(repository, never()).saveAndFlush(any(Location.class));
        }

        @Test
        void shouldCreateAndSaveLocationFromMetaData() throws GpsResolverException {
            // --- GIVEN ---
            var metaData = new Metadata(0, 0, Strings.EMPTY, null, TestLocationLongitude, TestLocationLatitude);
            when(repository.existsByCountryAndCity(anyString(), anyString())).thenReturn(false);
            when(repository.saveAndFlush(any(Location.class))).thenReturn(buildLocation());

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
            when(repository.saveAndFlush(any(Location.class))).thenReturn(buildLocation(newLocationCountryName, newLocationCityName));

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

        @ParameterizedTest
        @MethodSource("my.photomanager.TestDataBuilder#emptyNameProvider")
        void shouldReturnEmptyLocationWhenCountryNameIsEmpty(String countryName) {
            // --- WHEN ---
            assertThat(service.editLocation(TestCameraModelId, countryName, TestLocationCity)).isEmpty();

            // --- THEN ---
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