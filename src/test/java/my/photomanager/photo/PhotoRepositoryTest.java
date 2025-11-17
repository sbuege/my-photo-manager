package my.photomanager.photo;

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
class PhotoRepositoryTest {

	@Autowired
	private PhotoRepository photoRepository;

	@Test
	@DisplayName("should enforce unique hash value constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var photo1 = TestDataBuilder.TestPhotoBuilder.build();
		var photo2 = TestDataBuilder.TestPhotoBuilder.build();
		photoRepository.saveAndFlush(photo1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> photoRepository.saveAndFlush(photo2));
	}
}