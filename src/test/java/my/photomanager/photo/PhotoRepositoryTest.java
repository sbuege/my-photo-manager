package my.photomanager.photo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestDataBuilder;
import my.photomanager.TestUtils;
import my.photomanager.photo.orientation.OrientationRepository;
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
	private PhotoRepository repository;

	@Autowired
	private OrientationRepository orientationRepository;

	@Test
	@DisplayName("should enforce unique hash value constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var orientation1 = TestDataBuilder.TestOrientationBuilder.buildAndSave(orientationRepository);
		var photo1 = TestDataBuilder.TestPhotoBuilder.build()
				.toBuilder()
				.withOrientation(orientation1)
				.build();
		var photo2 = TestDataBuilder.TestPhotoBuilder.build()
				.toBuilder()
				.withOrientation(orientation1)
				.build();
		repository.saveAndFlush(photo1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(photo2));
	}
}