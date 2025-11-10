package my.photomanager.photo.category;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class PhotoCategoryService {

	private final PhotoCategoryRepository photoCategoryRepository;

	/**
	 * Saves the given {@link PhotoCategory} if no category with the same name exists,
	 * or returns the existing one.
	 *
	 * @param photoCategory the {@link PhotoCategory} to save or retrieve
	 * @return the existing or newly saved {@link PhotoCategory}
	 */
	public PhotoCategory saveOrGetPhotoCategory(@NonNull PhotoCategory photoCategory) {
		return photoCategoryRepository.findByName(photoCategory.getName())
				.orElseGet(() -> {
					var savedPhotoCategory = photoCategoryRepository.saveAndFlush(photoCategory);
					log.info("saved photo category with name {} successfully", photoCategory.getName());

					return savedPhotoCategory;
				});
	}
}
