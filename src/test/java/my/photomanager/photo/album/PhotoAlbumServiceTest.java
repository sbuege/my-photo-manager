package my.photomanager.photo.album;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class PhotoAlbumServiceTest {

	// TEST DATA
	final String TEST_PHOTO_ALBUM_NAME = "TestPhotoAlbum";
	final PhotoAlbum TEST_PHOTO_ALBUM = new PhotoAlbum(TEST_PHOTO_ALBUM_NAME);

	@Mock
	private PhotoAlbumRepository photoAlbumRepository;

	@InjectMocks
	private PhotoAlbumService photoAlbumService;


	@Test
	@DisplayName("should call saveAndFlush when photo album does not exist")
	void shouldCallSaveAndFlush() {
		// given
		when(photoAlbumRepository.findByName(TEST_PHOTO_ALBUM_NAME)).thenReturn(Optional.empty());

		// when
		photoAlbumService.saveOrGetPhotoAlbum(TEST_PHOTO_ALBUM);

		// then
		verify(photoAlbumRepository).saveAndFlush(TEST_PHOTO_ALBUM);
	}

	@Test
	@DisplayName("should never all saveAndFlush when photo album exists already")
	void shouldNeveCallSaveAndFlush() {
		// given
		when(photoAlbumRepository.findByName(TEST_PHOTO_ALBUM_NAME)).thenReturn(Optional.of(TEST_PHOTO_ALBUM));

		// when
		photoAlbumService.saveOrGetPhotoAlbum(TEST_PHOTO_ALBUM);

		// then
		verify(photoAlbumRepository, never()).saveAndFlush(TEST_PHOTO_ALBUM);
	}
}