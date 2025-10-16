package my.photomanager.photo;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import my.photomanager.filter.FilterProperties;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@Log4j2
public class PhotoService {

	private final PhotoRepository photoRepository;

	protected PhotoService(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}

	public boolean existsPhotoByHashValue(Path photoPath) throws IOException {
		return photoRepository.existsByHashValue(DigestUtils.md5DigestAsHex(new FileInputStream(photoPath.toFile())));
	}

	/**
	 * Saves the given {@link Photo} if no photo with the same hash value exists,
	 * or returns the existing one.
	 *
	 * @param photo the {@link Photo} to save or retrieve
	 * @return the existing or newly saved {@link Photo}
	 */
	public Photo saveOrGetPhoto(@NonNull Photo photo) {
		return photoRepository.findByHashValue(photo.getHashValue())
				.orElseGet(() -> {
					var savedPhoto = photoRepository.saveAndFlush(photo);
					log.info("saved photo with filename successfully", photo.getFileName());

					return savedPhoto;
				});
	}

	public Collection<Photo> filterPhotos(@NonNull FilterProperties filterProperties) {
		return photoRepository.findAll(Specification.where(containsLocation(filterProperties.locationCountries(), filterProperties.locationCities()))
				.and(createdBetween(filterProperties.startDate(), filterProperties.endDate()).and(containsCameraModel(filterProperties.cameraModels()))));

	}

	private Specification<Photo> containsLocation(List<String> locationCountries, List<String> locationCities) {
		return (root, query, cb) -> {
			if (locationCountries == null || locationCountries.isEmpty()) {
				return null;
			}

			if (locationCities == null || locationCities.isEmpty()) {
				return null;
			}

			return cb.and(root.get("location")
					.get("country")
					.in(locationCountries), root.get("location")
					.get("city")
					.in(locationCities));
		};
	}

	private Specification<Photo> containsCameraModel(List<String> cameraModels) {
		return (root, query, cb) -> {
			if (cameraModels == null || cameraModels.isEmpty()) {
				return null;
			}

			return root
					.get("cameraSettings")
					.get("cameraModelName")
					.in(cameraModels);
		};
	}

	private Specification<Photo> createdBetween(LocalDate startDate, LocalDate endDate) {
		return (root, query, cb) -> {
			if (startDate == null && endDate == null) {
				return null;
			}
			if (startDate != null && endDate != null) {
				return cb.between(root.get("creationDate"), startDate, endDate);
			}
			if (startDate != null) {
				return cb.equal(root.get("creationDate"), startDate);
			}
			return cb.lessThanOrEqualTo(root.get("creationDate"), endDate);
		};
	}
}
