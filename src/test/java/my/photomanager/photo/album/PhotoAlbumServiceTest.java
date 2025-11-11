package my.photomanager.photo.album;

import static my.photomanager.TestDataBuilder.TEST_PHOTO_ALBUM_NAME;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhotoAlbumServiceTest {

	// TEST DATA
	final PhotoAlbum TEST_PHOTO_ALBUM = TestDataBuilder.TestPhotoAlbumBuilder.build();

	@Mock
	private PhotoAlbumRepository photoAlbumRepository;

	@InjectMocks
	private PhotoAlbumService photoAlbumService;

	@Test
	@DisplayName("should call saveAndFlush when photo album does not exist")
	void shouldCallSaveAndFlush() {
		// --- GIVEN ---
		when(photoAlbumRepository.findByName(TEST_PHOTO_ALBUM_NAME)).thenReturn(Optional.empty());

		// --- WHEN ---
		photoAlbumService.saveOrGetPhotoAlbum(TEST_PHOTO_ALBUM);

		// --- THEN ---
		verify(photoAlbumRepository).saveAndFlush(TEST_PHOTO_ALBUM);
	}

	@Test
	@DisplayName("should not call saveAndFlush when photo album already exists")
	void shouldNotCallSaveAndFlush() {
		// --- GIVEN ---
		when(photoAlbumRepository.findByName(TEST_PHOTO_ALBUM_NAME)).thenReturn(Optional.of(TEST_PHOTO_ALBUM));

		// --- WHEN ---
		photoAlbumService.saveOrGetPhotoAlbum(TEST_PHOTO_ALBUM);

		// --- THEN ---
		verify(photoAlbumRepository, never()).saveAndFlush(TEST_PHOTO_ALBUM);
	}
}