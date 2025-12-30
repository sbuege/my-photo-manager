package my.photomanager.photo.category;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class CategoryService {

	private final CategoryRepository repository;

	protected void addCategory(@NonNull String name) {
		var category = new Category(name);
		log.info("created new category {}", category);

		saveCategoryIfNotExists(category);
	}

	protected void editCategory(long id, @NonNull String name) {
		var category = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("no category found with id " + id));
		log.info("found category {} to edit", category);

		category.setName(name);
		log.info("updated category {} successfully", category);
	}

	protected void deleteCategory(long id) {
		var category = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("no album category with id " + id));
		log.info("found category {} to delete", category);

		repository.delete(category);
		log.info("deleted category {} successfully", category);
	}

	private Category saveCategoryIfNotExists(@NonNull Category category) {
		return null;
	}

	/**
	 * Saves the given {@link Category} if no category with the same name exists,
	 * or returns the existing one.
	 *
	 * @param category the {@link Category} to save or retrieve
	 * @return the existing or newly saved {@link Category}
	 */
	public Category saveOrGetCategory(@NonNull Category category) {
		return repository.findByName(category.getName())
				.orElseGet(() -> {
					var savedCategory = repository.saveAndFlush(category);
					log.info("saved category with name {} successfully", category.getName());

					return savedCategory;
				});
	}
}
