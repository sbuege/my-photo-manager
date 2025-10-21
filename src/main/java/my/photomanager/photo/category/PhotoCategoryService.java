package my.photomanager.photo.category;

import java.util.Collection;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PhotoCategoryService {

	private final PhotoCategoryRepository photoCategoryRepository;

	protected PhotoCategoryService(@NonNull PhotoCategoryRepository photoCategoryRepository) {
		this.photoCategoryRepository = photoCategoryRepository;
	}

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

	/**
	 * Retrieves all photo categories from the repository and converts them into {@link PhotoCategoryDTO} objects.
	 *
	 * @return a collection of {@link PhotoCategoryDTO} representing all stored photo categories.
	 */
	public Collection<PhotoCategoryDTO> getPhotoCategoryDTOs() {
		return photoCategoryRepository.findAll()
				.stream()
				.map(category -> new PhotoCategoryDTO(category.getId(), category.getName()))
				.toList();
	}
}
