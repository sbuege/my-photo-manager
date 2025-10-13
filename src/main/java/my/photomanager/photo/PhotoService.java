package my.photomanager.photo;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
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

	public Collection<Photo> filterPhotos(LocalDate start, LocalDate end, List<Long> locationIds) {
		return photoRepository.findAll(Specification.where(containsCreationDateBetween(start, end))
				.and(containsLocation(locationIds)));
	}

	private static Specification<Photo> containsCreationDateBetween(LocalDate start, LocalDate end) {
		return (root, query, cb) -> {
			if (start == null && end == null) {
				return null;
			}
			if (start != null && end != null) {
				return cb.between(root.get("creationDate"), start, end);
			}
			if (start != null) {
				return cb.greaterThanOrEqualTo(root.get("creationDate"), start);
			}
			return cb.lessThanOrEqualTo(root.get("creationDate"), end);
		};
	}

	private static Specification<Photo> containsLocation(List<Long> locationIds) {
		return (root, query, cb) -> {
			if (locationIds == null || locationIds.isEmpty()) {
				return null;
			}
			return root.get("location")
					.get("id")
					.in(locationIds);

		};

	}

}
