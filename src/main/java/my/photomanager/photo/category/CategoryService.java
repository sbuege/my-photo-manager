package my.photomanager.photo.category;

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
