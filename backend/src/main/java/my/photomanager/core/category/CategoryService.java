package my.photomanager.core.category;

import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.album.AlbumServiceException;
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
	 * Creates a new category with the given name, saves it to the repository if it does not already exist,
	 * and returns the saved or existing category wrapped in an {@code Optional}.
	 * If the provided name is blank, a warning is logged, and an empty {@code Optional} is returned.
	 *
	 * @param name the name of the category to be created and saved; must not be blank or null
	 * @return an {@code Optional} containing the saved or existing {@code Category}, or an empty {@code Optional} if the name is blank
	 */
	public Optional<Category> createAndSaveCategory(@NonNull String name) {
		if (name.isBlank()) {
			log.warn("category name cannot be blank");
			return Optional.empty();
		}

		log.debug("creating new category with name {}", name);
		var category = new Category(name);
		log.debug("created new category {}", category);

		return Optional.of(saveOrGetCategory(category));
	}


	/**
	 * Edits an existing category by updating its name.
	 * Retrieves the category by its ID, updates its name, and saves the changes.
	 * If the provided name is blank, a warning is logged, and an empty {@code Optional} is returned.
	 * If no category is found with the given ID, a {@link CategoryServiceException} is thrown.
	 *
	 * @param id the ID of the category to be edited
	 * @param name the new name of the category; must not be blank or null
	 * @return an {@code Optional} containing the updated {@code Category}, or an empty {@code Optional} if the name is blank
	 */
	public Optional<Category> editCategory(long id, @NonNull String name) {
		if (name.isBlank()) {
			log.warn("category name cannot be blank");
			return Optional.empty();		}

		var category = repository.findById(id)
				.orElseThrow(() -> new CategoryServiceException("no category found with id " + id));
		log.debug("found category {} to edit", category);

		log.debug("updating category {} with name {}", category, name);
		category.setName(name);
		log.info("updated category {} successfully", category);

		return Optional.of(saveOrGetCategory(category));
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

		log.debug("deleting category {}", category);
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
