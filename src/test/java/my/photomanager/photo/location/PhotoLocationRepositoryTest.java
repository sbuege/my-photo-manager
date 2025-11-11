package my.photomanager.photo.location;

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
class PhotoLocationRepositoryTest {

	@Autowired
	private PhotoLocationRepository photoLocationRepository;

	@Test
	@DisplayName("should enforce unique country and city constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var photoLocation1 = TestDataBuilder.TestPhotoLocationBuilder.build();
		var photoLocation2 = TestDataBuilder.TestPhotoLocationBuilder.build();
		photoLocationRepository.saveAndFlush(photoLocation1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> photoLocationRepository.saveAndFlush(photoLocation2));
	}
}