package my.photomanager.photo.location;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class LocationRepositoryTest {

	@Autowired
	private LocationRepository repository;

	@Test
	@DisplayName("should enforce unique country and city constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var location1 = TestDataBuilder.TestPhotoLocationBuilder.build();
		var location2 = TestDataBuilder.TestPhotoLocationBuilder.build();
		repository.saveAndFlush(location1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(location2));
	}
}