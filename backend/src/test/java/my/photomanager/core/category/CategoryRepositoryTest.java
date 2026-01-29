package my.photomanager.core.category;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repository;

	@Test
	@DisplayName("should enforce unique name constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var category1 = TestDataBuilder.buildCategory();
		var category2 = TestDataBuilder.buildCategory();
		repository.saveAndFlush(category1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(category2));
	}

}