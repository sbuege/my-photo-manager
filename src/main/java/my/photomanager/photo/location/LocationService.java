package my.photomanager.photo.location;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class LocationService {

	private final LocationRepository repository;

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
