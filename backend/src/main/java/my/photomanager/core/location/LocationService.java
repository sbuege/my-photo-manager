package my.photomanager.core.location;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.utils.gpsResolver.GpsResolver;
import my.photomanager.utils.gpsResolver.GpsResolverException;
import my.photomanager.utils.metaDataParser.Metadata;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing {@link Location} entities.
 * This class provides functionality to create, retrieve, update, and delete locations
 * using the underlying {@link LocationRepository}. It also includes utility methods
 * for resolving and persisting location data.
 * <p>
 * This service uses Lombok annotations for reducing boilerplate code and Spring
 * annotations to mark it as a service component in the application context.
 */
@RequiredArgsConstructor
@Service
@Log4j2
public class LocationService {

    private final LocationRepository repository;

    /**
     * Retrieves all locations stored in the repository.
     *
     * @return a list of all locations
     */
    public List<Location> getAllLocations() {
        return repository.findAll();
    }


    /**
     * Creates and saves a new {@link Location} entity based on metadata containing GPS coordinates.
     * The method resolves the longitude and latitude from the given metadata to determine
     * the country and city. It then creates and attempts to persist the location.
     *
     * @param metaData the metadata containing GPS coordinates, must not be null
     * @return an {@link Optional} containing the saved or retrieved {@link Location}
     * if the operation is successful, or an empty {@link Optional} if the location could not be created
     * @throws GpsResolverException if there is an error resolving the GPS coordinates into a location
     */
    public Optional<Location> createAndSaveLocation(@NonNull Metadata metaData) throws GpsResolverException {
        var locationInfo = GpsResolver.resolveLongitudeLatitude(metaData.gpsLongitude(), metaData.gpsLatitude());

        return createAndSaveLocation(locationInfo.country(), locationInfo.city());
    }

    /**
     * Creates and saves a new {@link Location} based on the provided country and city.
     * If the country name is blank, the method logs a warning and returns an empty {@link Optional}.
     * Otherwise, it creates a new location and attempts to save or retrieve it from the repository.
     *
     * @param country the name of the country for the location, must not be null or blank
     * @param city    the name of the city for the location, must not be null
     * @return an {@link Optional} containing the saved or retrieved {@link Location}
     * if the country name is valid, or an empty {@link Optional} if the country name is blank
     */
    public Optional<Location> createAndSaveLocation(@NonNull String country, @NonNull String city) {
        if (country.isBlank()) {
            log.warn("country name cannot be blank");
            return Optional.empty();
        }

        log.debug("creating new location with country {} and city {}", country, city);
        var location = new Location(country, city);
        log.debug("created new location {}", location);

        return Optional.of(saveOrGetLocation(location));
    }


    /**
     * Updates the country and city of an existing {@link Location} identified by its ID.
     * <p>
     * If the provided country name is blank, a warning is logged, and the method
     * returns an empty {@link Optional}. If no location is found with the given ID,
     * an exception is thrown. Otherwise, the location's country and city fields are updated,
     * and the updated location is saved or retrieved from the repository.
     *
     * @param id      the unique identifier of the location to be edited
     * @param country the new name of the country for the location, must not be null or blank
     * @param city    the new name of the city for the location, must not be null
     * @return an {@link Optional} containing the updated {@link Location}
     * if the operation is successful, or an empty {@link Optional}
     * if the country name is blank
     */
    public Optional<Location> editLocation(long id, @NonNull String country, @NonNull String city) {
        if (country.isBlank()) {
            log.warn("country name cannot be blank");
            return Optional.empty();
        }

        var location = repository.findById(id)
                .orElseThrow(() -> new LocationServiceException("no location found with id " + id));
        log.debug("found location {} to edit", location);

        log.debug("updating location {} with country {} and city {}", location, country, city);
        location.setCountry(country);
        location.setCity(city);
        log.info("updated location {} successfully", location);

        return Optional.of(saveOrGetLocation(location));
    }

    /**
     * Deletes a location from the repository based on its ID.
     * <p>
     * The method retrieves the location by the given ID. If the location does not exist,
     * an exception is thrown. If found, the location is deleted from the repository.
     *
     * @param id the unique identifier of the location to be deleted
     * @throws LocationServiceException if no location is found with the given ID
     */
    public void deleteLocation(long id) {
        var location = repository.findById(id)
                .orElseThrow(() -> new LocationServiceException("no location found with id " + id));
        log.debug("found location {} to delete", location);

        log.debug("deleting location {}", location);
        repository.deleteById(id);
        log.info("deleted location {} successfully", location);
    }

    private Location saveOrGetLocation(@NonNull Location location) {
        Location savedLocation;

        if (repository.existsByCountryAndCity(location.getCountry(), location.getCity())) {
            savedLocation = repository.findByCountryAndCity(location.getCountry(), location.getCity())
                    .orElseThrow(() -> new LocationServiceException("no location found with country " + location.getCountry() + " and city " + location.getCity()));
            log.debug("location {} already exists", savedLocation);
        } else {
            savedLocation = repository.saveAndFlush(location);
            log.info("saved location {} successfully", savedLocation);
        }

        return savedLocation;
    }
}
