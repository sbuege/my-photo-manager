package my.photomanager.photo;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Collection;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import my.photomanager.filter.FilterProperties;
import net.coobird.thumbnailator.Thumbnails;
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
	 * Saves the given {@link Photo} if no photo with the same hash value exists
	 *
	 * @param photo the {@link Photo} to save or retrieve
	 */
	public void saveIfPhotoNotExists(@NonNull Photo photo) {
		photoRepository.findByHashValue(photo.getHashValue())
				.orElseGet(() -> {
					var savedPhoto = photoRepository.saveAndFlush(photo);
					log.info("saved photo with filename successfully", photo.getFileName());

					return savedPhoto;
				});
	}

	public byte[] getThumbnail(@NonNull Long id) throws IOException, PhotoServiceException {
		var photo = photoRepository.findById(id)
				.orElseThrow(() -> new PhotoServiceException("no photo found with id " + id));
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Thumbnails.of(Path.of(photo.getFileName())
						.toFile())
				.scale(0.25)
				.toOutputStream(out);

		var base64String = Base64.getEncoder()
				.encodeToString(out.toByteArray());

		return out.toByteArray();
	}

	/**
	 * Filters photos from the repository based on the provided {@link FilterProperties}.
	 * It returns a collection of photo IDs that match all specified filters.
	 *
	 * @param filterProperties an object containing filtering parameters such as location,
	 *                         date range, and camera model
	 * @return a collection of photo IDs matching the applied filters;
	 */
	public Collection<Long> filterPhotos(@NonNull FilterProperties filterProperties) {
		var photos = photoRepository.findAll(Specification.where(containsLocation(filterProperties))
				.and(createdBetween(filterProperties).and(containsCameraModel(filterProperties))));

		return photos.stream()
				.map(Photo::getId)
				.toList();
	}

	private Specification<Photo> containsLocation(@NonNull FilterProperties filterProperties) {
		var locationIds = filterProperties.locationIDs();

		return (root, query, cb) -> {
			if (locationIds == null || locationIds.isEmpty()) {
				return null;
			}


			return cb.and(root.get("location")
					.get("id")
					.in(locationIds));
		};
	}

	private Specification<Photo> containsCameraModel(@NonNull FilterProperties filterProperties) {
		var cameraModelIds = filterProperties.cameraModelIds();

		return (root, query, cb) -> {
			if (cameraModelIds == null || cameraModelIds.isEmpty()) {
				return null;
			}

			return root
					.get("cameraSettings")
					.get("id")
					.in(cameraModelIds);
		};
	}

	private Specification<Photo> createdBetween(@NonNull FilterProperties filterProperties) {
		var startDate = filterProperties.startDate();
		var endDate = filterProperties.endDate();

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
