package my.photomanager.photo.category;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestDataBuilder;
import my.photomanager.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@Import(TestUtils.PhotoIndexerMock.class)
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repository;

	@Test
	@DisplayName("should enforce unique name constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var category1 = TestDataBuilder.TestPhotoCategoryBuilder.build();
		var category2 = TestDataBuilder.TestPhotoCategoryBuilder.build();
		repository.saveAndFlush(category1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(category2));
	}
}