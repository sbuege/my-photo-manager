package my.photomanager.photo.album;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class PhotoAlbumServiceTest {

	private PhotoAlbumService photoAlbumService;

	@Mock
	private PhotoAlbumRepository photoAlbumRepository;

	@BeforeEach
	void setUp() {
		photoAlbumService = new PhotoAlbumService(photoAlbumRepository);
	}

	@Test
	void shouldSavePhotoAlbumWhenNotExisting() {
		// given
		final var photoAlbumName = "TestPhotoAlbum";
		var photoAlbum = new PhotoAlbum(photoAlbumName);
		when(photoAlbumRepository.findByName(photoAlbumName)).thenReturn(Optional.empty());

		// when
		photoAlbumService.saveOrGetPhotoAlbum(photoAlbum);

		// then
		verify(photoAlbumRepository).saveAndFlush(photoAlbum);
	}

	@Test
	void shouldReturnExistingPhotoAlbumWhenAlreadyExists() {
		// given
		final var photoAlbumName = "TestPhotoAlbum";
		var photoAlbum = new PhotoAlbum(photoAlbumName);
		when(photoAlbumRepository.findByName(photoAlbumName)).thenReturn(Optional.of(photoAlbum));

		// when
		photoAlbumService.saveOrGetPhotoAlbum(photoAlbum);

		// then
		verify(photoAlbumRepository, never()).saveAndFlush(photoAlbum);
	}

}