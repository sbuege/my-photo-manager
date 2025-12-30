package my.photomanager.photo.location;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.photo.album.Album;
import my.photomanager.photo.category.Category;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class LocationService {

	private final LocationRepository repository;

	protected void addLocation(@NonNull String country, @NonNull String city) {
		var location = new Location(country, city);
		log.info("created new location {}", location);

		saveLocationIfNotExists(location);
	}

	protected void editLocation(long id, @NonNull String country, @NonNull String city) {
		var location = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("no location found with id " + id));
		log.info("found location {} to edit", location);

		location.setCountry(country);
		location.setCity(city);
		log.info("updated location {} successfully", location);
	}

	protected void deleteLocation(long id) {
		var location = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("no location found with id " + id));
		log.info("found location {} to delete", location);

		repository.delete(location);
		log.info("deleted location {} successfully", location);
	}

	private Location saveLocationIfNotExists(@NonNull Location location){
		return null;
	}

	/**
	 * Saves the given {@link Location} if no location with the same country and city exists,
	 * or returns the existing one.
	 *
	 * @param location the {@link Location} to save or retrieve
	 * @return the existing or newly saved {@link Location}
	 */
	public Location saveOrGetLocation(@NonNull Location location) {
		return repository.findByCountryAndCity(location.getCountry(), location.getCity())
				.orElseGet(() -> {
					var savedLocation = repository.saveAndFlush(location);
					log.info("saved location with country {} and city {} successfully", location.getCountry(), location.getCity());

					return savedLocation;
				});
	}
}
