package my.photomanager.photo.album;

import static my.photomanager.TestDataBuilder.TEST_ALBUM_NAME;
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
class AlbumServiceTest {

	// TEST DATA
	final Album TEST_ALBUM = TestDataBuilder.TestPhotoAlbumBuilder.build();

	@Mock
	private AlbumRepository repository;

	@InjectMocks
	private AlbumService service;

	@Test
	@DisplayName("should call saveAndFlush when album does not exist")
	void shouldCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByName(TEST_ALBUM_NAME)).thenReturn(Optional.empty());

		// --- WHEN ---
		service.saveOrGetAlbum(TEST_ALBUM);

		// --- THEN ---
		verify(repository).saveAndFlush(TEST_ALBUM);
	}

	@Test
	@DisplayName("should not call saveAndFlush when album already exists")
	void shouldNotCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByName(TEST_ALBUM_NAME)).thenReturn(Optional.of(TEST_ALBUM));

		// --- WHEN ---
		service.saveOrGetAlbum(TEST_ALBUM);

		// --- THEN ---
		verify(repository, never()).saveAndFlush(TEST_ALBUM);
	}
}