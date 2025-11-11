package my.photomanager.photo.album;

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
class PhotoAlbumRepositoryTest {

	@Autowired
	private PhotoAlbumRepository photoAlbumRepository;

	@Test
	@DisplayName("should enforce unique name constraint")
	void shouldEnforceUniqueConstraint() {
		// --- GIVEN ---
		var photoAlbum1 = TestDataBuilder.TestPhotoAlbumBuilder.build();
		var photoAlbum2 = TestDataBuilder.TestPhotoAlbumBuilder.build();
		photoAlbumRepository.saveAndFlush(photoAlbum1);

		// --- WHEN / THEN ---
		assertThrows(DataIntegrityViolationException.class, () -> photoAlbumRepository.saveAndFlush(photoAlbum2));
	}
}