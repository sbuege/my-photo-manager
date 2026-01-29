package my.photomanager.core.library;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class LibraryRepositoryTest {

	@Autowired
	private LibraryRepository repository;

	@Test
	@DisplayName("should enforce unique path constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var library1 = TestDataBuilder.buildLibrary();
		var library2 = TestDataBuilder.buildLibrary();
		repository.saveAndFlush(library1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(library2));
	}
}