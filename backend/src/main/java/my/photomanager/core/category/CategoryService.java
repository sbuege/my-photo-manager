package my.photomanager.core.category;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class CategoryService {

	private final CategoryRepository repository;

	/**
	 * Retrieves all categories from the repository.
	 *
	 * @return a list of all categories
	 */
	public List<Category> getAllCategories() {
		return repository.findAll();
	}

	/**
	 * Creates a new category with the specified name and saves it to the repository.
	 * If a category with the same name already exists, it retrieves the existing one.
	 *
	 * @param name the name of the category to create
	 * @return the created or retrieved category
	 */
	public Category createAndSaveCategory(@NonNull String name) {
		var category = new Category(name);
		log.debug("created new category {}", category);

		return saveOrGetCategory(category);
	}

	/**
	 * Updates the name of an existing category identified by its ID and saves the changes.
	 * If no category is found with the specified ID, a {@link CategoryServiceException} is thrown.
	 *
	 * @param id the ID of the category to be updated
	 * @param name the new name for the category, must not be null
	 * @return the updated and saved category
	 */
	public Category editCategory(long id, @NonNull String name) {
		var category = repository.findById(id)
				.orElseThrow(() -> new CategoryServiceException("no category found with id " + id));
		log.debug("found category {} to edit", category);

		category.setName(name);
		log.info("updated category {} successfully", category);

		return saveOrGetCategory(category);
	}

	/**
	 * Deletes a category with the specified ID from the repository.
	 * If no category is found with the given ID, a {@link CategoryServiceException}
	 * is thrown.
	 *
	 * @param id the ID of the category to be deleted
	 */
	public void deleteCategory(long id) {
		var category = repository.findById(id)
				.orElseThrow(() -> new CategoryServiceException("no category found with id " + id));
		log.debug("found category {} to delete", category);

		repository.deleteById(id);
		log.info("deleted category {} successfully", category);
	}

	private Category saveOrGetCategory(@NonNull Category category) {
		Category savedCategory;

		if (repository.existsByName(category.getName())) {
			savedCategory = repository.findByName(category.getName())
					.orElseThrow(() -> new CategoryServiceException("no category found with name " + category.getName()));
			log.debug("category {} already exists", savedCategory);
		} else {
			savedCategory = repository.saveAndFlush(category);
			log.info("saved category {} successfully", savedCategory);
		}

		return savedCategory;
	}
}
