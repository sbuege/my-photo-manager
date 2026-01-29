package my.photoLibrary.core.album;

import static org.junit.jupiter.api.Assertions.*;

import my.photoLibrary.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class AlbumRepositoryTest {

	@Autowired
	private AlbumRepository repository;

	@Test
	@DisplayName("should enforce unique name constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var album1 = TestDataBuilder.buildAlbum();
		var album2 = TestDataBuilder.buildAlbum();
		repository.saveAndFlush(album1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(album2));
	}
}