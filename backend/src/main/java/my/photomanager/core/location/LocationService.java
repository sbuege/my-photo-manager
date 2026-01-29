package my.photomanager.core.location;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.location.Location;
import my.photomanager.utils.gpsResolver.GpsResolver;
import my.photomanager.utils.gpsResolver.GpsResolverException;
import my.photomanager.utils.metaDataParser.Metadata;
import org.springframework.stereotype.Service;

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
		var locations = repository.findAll();
		log.debug("found {} locations", locations.size());

		return locations;
	}

	/**
	 * Creates and saves a {@link Location} based on GPS metadata.
	 *
	 * This method extracts the GPS coordinates from the given metadata,
	 * resolves the corresponding country and city using a geocoding service,
	 * and then creates and saves a new location instance.
	 *
	 * @param metaData the metadata containing GPS coordinates (longitude and latitude)
	 * @return the created {@link Location} object
	 * @throws GpsResolverException if the geocoding resolution fails
	 */
	public Location createAndSaveLocation(@NonNull Metadata metaData) throws GpsResolverException {
		var locationInfo = GpsResolver.resolveLongitudeLatitude(metaData.gpsLongitude(), metaData.gpsLatitude());

		return createAndSaveLocation(locationInfo.country(), locationInfo.city());
	}

	/**
	 * Creates and saves a new {@link Location} instance with the provided country and city.
	 *
	 * If a location with the same country and city already exists in the repository,
	 * the existing location will be returned. Otherwise, the new location will be saved
	 * and returned.
	 *
	 * @param country the country of the location to be created, must not be null
	 * @param city the city of the location to be created, must not be null
	 * @return the saved or existing {@link Location} object
	 */
	public Location createAndSaveLocation(@NonNull String country, @NonNull String city) {
		var location = new Location(country, city);
		log.debug("created new location {}", location);

		return saveOrGetLocation(location);
	}

	/**
	 * Updates the details of an existing {@link Location} identified by its ID.
	 *
	 * This method retrieves the location by its ID. If the location does not exist,
	 * an exception is thrown. If found, the country and city of the location are updated
	 * and the updated location is saved and returned.
	 *
	 * @param id the unique identifier of the location to be edited
	 * @param country the new country to be set for the location, must not be null
	 * @param city the new city to be set for the location, must not be null
	 * @return the updated {@link Location} object
	 * @throws LocationServiceException if no location is found with the given ID
	 */
	public Location editLocation(long id, @NonNull String country, @NonNull String city) {
		var location = repository.findById(id)
				.orElseThrow(() -> new LocationServiceException("no location found with id " + id));
		log.debug("found location {} to edit", location);

		location.setCountry(country);
		location.setCity(city);

		return saveOrGetLocation(location);
	}

	/**
	 * Deletes a location from the repository based on its ID.
	 *
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
