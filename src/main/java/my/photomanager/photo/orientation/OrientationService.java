package my.photomanager.photo.orientation;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class OrientationService {

	private final OrientationRepository repository;

	/**
	 * Saves the given {@link Orientation} if no orientation with the same name exists,
	 * or returns the existing one.
	 *
	 * @param orientation the {@link Orientation} to save or retrieve
	 * @return the existing or newly saved {@link Orientation}
	 */
	public Orientation saveOrGetOrientation(@NonNull Orientation orientation) {
		return repository.findByName(orientation.getName())
				.orElseGet(() -> {
					var savedOrientation = repository.saveAndFlush(orientation);
					log.info("saved orientation with name {} successfully", orientation.getName());

					return savedOrientation;
				});
	}
}
