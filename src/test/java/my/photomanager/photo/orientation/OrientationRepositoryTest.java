package my.photomanager.photo.orientation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class OrientationRepositoryTest {

	@Autowired
	private OrientationRepository repository;

	@Test
	@DisplayName("should enforce unique name constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var orientation1 = TestDataBuilder.TestOrientationBuilder.build();
		var orientation2 = TestDataBuilder.TestOrientationBuilder.build();
		repository.saveAndFlush(orientation1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(orientation2));
	}

}