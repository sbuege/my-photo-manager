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
class PhotoCategoryRepositoryTest {

	@Autowired
	private PhotoCategoryRepository photoCategoryRepository;

	@Test
	@DisplayName("should enforce unique name constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var photoCategory1 = TestDataBuilder.TestPhotoCategoryBuilder.build();
		var photoCategory2 = TestDataBuilder.TestPhotoCategoryBuilder.build();
		photoCategoryRepository.saveAndFlush(photoCategory1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> photoCategoryRepository.saveAndFlush(photoCategory2));
	}
}