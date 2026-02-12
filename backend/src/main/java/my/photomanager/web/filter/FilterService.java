package my.photomanager.web.filter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.cameraModel.CameraModelStatistic;
import my.photomanager.core.location.LocationStatistic;
import my.photomanager.core.orientation.OrientationStatistic;
import my.photomanager.core.photo.CreationYearStatistic;
import my.photomanager.core.photo.Photo;
import my.photomanager.core.photo.PhotoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class FilterService {

	private final PhotoRepository repository;

	/**
	 * Retrieves a list of camera model statistics. Each statistic includes the camera model's ID, name,
	 * and the number of photos associated with it. Only items with non-null names are included in the result.
	 *
	 * @return a list of {@code CameraModelStatistic} objects representing the statistics of camera models.
	 */
	public List<CameraModelStatistic> getCameraModelStatistics() {
		return repository.groupPhotosByCameraModel()
				.stream()
				.filter(filter -> filter.getName() != null)
				.toList();
	}

	/**
	 * Retrieves a list of statistics about locations where photos were taken.
	 * Each statistic includes details such as the location's ID, country, city,
	 * and the number of photos associated with it. Only locations with a non-null country
	 * are included in the results.
	 *
	 * @return a list of LocationStatistic objects containing location details and photo counts.
	 */
	public List<LocationStatistic> getLocationStatistics() {
		return repository.groupPhotosByLocation()
				.stream()
				.filter(filter -> filter.getCountry() != null)
				.toList();
	}


	/**
	 * Retrieves a list of creation year statistics for photos, where each statistic
	 * contains the year and the count of photos created in that year.
	 * Only includes years that are non-zero in the results.
	 *
	 * @return a list of {@code CreationYearStatistic} objects representing creation years
	 * 		and corresponding photo counts, sorted in descending order of year.
	 */
	public List<CreationYearStatistic> getCreationYearStatistics() {
		return repository.groupPhotosByCreationYear()
				.stream()
				.filter(filter -> filter != null && filter.getYear() != 0)
				.toList();
	}


	/**
	 * Retrieves a list of orientation statistics by grouping photos based on their orientation.
	 * Filters out results where the orientation name is null.
	 *
	 * @return a list of {@link OrientationStatistic} objects containing orientation ID, name, and the number of photos for each orientation.
	 */
	public List<OrientationStatistic> getOrientationStatistics() {
		return repository.groupPhotosByOrientation()
				.stream()
				.filter(filter -> filter.getName() != null)
				.toList();
	}

	/**
	 * Filters photos based on the specified criteria provided in the {@code ActiveFilter} object.
	 * The filtering can include location IDs, camera model IDs, and orientation IDs.
	 * The resulting photos are sorted in descending order of creation date and returned as a list of their IDs.
	 *
	 * @param activeFilter the filter criteria containing lists of location IDs, camera model IDs,
	 *                     and orientation IDs to filter the photos.
	 * @return a list of photo IDs that match the specified filtering criteria.
	 */
	public List<Long> filterPhotos(ActiveFilter activeFilter) {
		Specification<Photo> spec = Specification.where(null);

		spec = spec.and(filterPhotosByLocationIds(activeFilter.locationIds()));
		spec = spec.and(filterPhotosByCreationYear(activeFilter.creationYears()));
		spec = spec.and(filterPhotosByCameraModelIds(activeFilter.cameraModelIds()));
		spec = spec.and(filterPhotosByOrientationIds(activeFilter.orientationIds()));

		var photos = repository.findAll(spec, Sort.by(Sort.Direction.DESC, "creationDate"));

		return photos.stream()
				.map(Photo::getId)
				.toList();
	}

	private Specification<Photo> filterPhotosByLocationIds(List<Long> locationIds) {
		return (root, query, cb) -> {
			if (locationIds == null || locationIds.isEmpty()) {
				return null;
			}

			return cb.and(root.get("location")
					.get("id")
					.in(locationIds));
		};
	}

	private Specification<Photo> filterPhotosByCreationYear(List<Integer> years) {
		return (root, query, cb) -> {
			if (years == null || years.isEmpty()) {
				return null;
			}

			return root.get("creationDate")
					.in(years);
		};
	}

	private Specification<Photo> filterPhotosByCameraModelIds(List<Long> cameraModelIds) {
		return (root, query, cb) -> {
			if (cameraModelIds == null || cameraModelIds.isEmpty()) {
				return null;
			}

			return root
					.get("cameraModel")
					.get("id")
					.in(cameraModelIds);
		};
	}

	private Specification<Photo> filterPhotosByOrientationIds(List<Long> orientationIds) {
		return (root, query, cb) -> {
			if (orientationIds == null || orientationIds.isEmpty()) {
				return null;
			}

			return root
					.get("orientation")
					.get("id")
					.in(orientationIds);
		};
	}
}
