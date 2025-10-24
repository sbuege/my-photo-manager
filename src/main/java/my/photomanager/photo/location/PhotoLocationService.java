package my.photomanager.photo.location;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PhotoLocationService {

	private final PhotoLocationRepository photoLocationRepository;

	protected PhotoLocationService(@NonNull PhotoLocationRepository photoLocationRepository) {
		this.photoLocationRepository = photoLocationRepository;
	}

	/**
	 * Saves the given {@link PhotoLocation} if no location with the same country and city exists,
	 * or returns the existing one.
	 *
	 * @param photoLocation the {@link PhotoLocation} to save or retrieve
	 * @return the existing or newly saved {@link PhotoLocation}
	 */
	public PhotoLocation saveOrGetPhotoLocation(@NonNull PhotoLocation photoLocation) {
		return photoLocationRepository.findByCountryAndCity(photoLocation.getCountry(), photoLocation.getCity())
				.orElseGet(() -> {
					var savedPhotoLocation = photoLocationRepository.saveAndFlush(photoLocation);
					log.info("saved photo location with country {} and city {} successfully", photoLocation.getCountry(), photoLocation.getCity());

					return savedPhotoLocation;
				});
	}
}
