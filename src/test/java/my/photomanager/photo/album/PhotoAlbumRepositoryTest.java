package my.photomanager.photo.album;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestUtils;
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
	void shouldThrowExceptionWhenSavingPhotoAlbumWithDuplicateName() {
		// given
		final var photoAlbumName = "TestPhotoAlbum";
		var photoAlbum1 = new PhotoAlbum(photoAlbumName);
		var photoAlbum2 = new PhotoAlbum(photoAlbumName);
		photoAlbumRepository.saveAndFlush(photoAlbum1);

		// when / then
		assertThrows(DataIntegrityViolationException.class, () -> photoAlbumRepository.saveAndFlush(photoAlbum2));
	}
}