package my.photoLibrary.core.album;

import static my.photoLibrary.TestDataBuilder.TestAlbumId;
import static my.photoLibrary.TestDataBuilder.TestAlbumName;
import static my.photoLibrary.TestDataBuilder.buildAlbum;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

	@Mock
	private AlbumRepository repository;

	@InjectMocks
	private AlbumService service;

	@Test
	void shouldReturnAllAlbums() {
		// --- WHEN
		service.getAllAlbums();

		// --- THEN
		verify(repository).findAll();
	}

	@Nested
	class CreateAlbumTest {

		@Test
		void shouldCreateAndSaveAlbum() {
			// --- GIVEN ---
			when(repository.existsByName(anyString())).thenReturn(false);

			// --- WHEN ---
			service.createAndSaveAlbum(TestAlbumName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Album.class));
		}

		@Test
		void shouldReturnExistingAlbum() {
			// --- GIVEN ---
			when(repository.existsByName(anyString())).thenReturn(true);
			when(repository.findByName(anyString())).thenReturn(Optional.of(buildAlbum()));

			// --- WHEN ---
			service.createAndSaveAlbum(TestAlbumName);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Album.class));
		}
	}

	@Nested
	class EditAlbumTest {

		private final String newAlbumName = "newAlbumName";

		@Test
		void shouldEditAndSaveAlbum() {
			// --- GIVEN ---
			when(repository.findById(TestAlbumId)).thenReturn(Optional.of(buildAlbum()));
			when(repository.existsByName(anyString())).thenReturn(false);

			// --- WHEN ---
			service.editAlbum(TestAlbumId, newAlbumName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Album.class));
		}

		@Test
		void shouldReturnExistingAlbum() {
			// --- GIVEN ---
			when(repository.findById(TestAlbumId)).thenReturn(Optional.of(buildAlbum()));
			when(repository.existsByName(newAlbumName)).thenReturn(true);
			when(repository.findByName(newAlbumName)).thenReturn(Optional.of(buildAlbum(newAlbumName)));

			// --- WHEN ---
			service.editAlbum(TestAlbumId, newAlbumName);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Album.class));
		}

		@Test
		void shouldThrowExceptionWhenAlbumDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestAlbumId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(AlbumServiceException.class, () -> service.editAlbum(TestAlbumId, newAlbumName));
			verify(repository, never()).saveAndFlush(any(Album.class));
		}
	}

	@Nested
	class DeleteAlbumTest {

		@Test
		void shouldDeleteAlbum() {
			// --- GIVEN ---
			when(repository.findById(TestAlbumId)).thenReturn(Optional.of(buildAlbum()));

			// --- WHEN ---
			service.deleteAlbum(TestAlbumId);

			// --- THEN ---
			verify(repository).deleteById(TestAlbumId);
		}

		@Test
		void shouldThrowExceptionWhenAlbumDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestAlbumId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(AlbumServiceException.class, () -> service.deleteAlbum(TestAlbumId));
			verify(repository, never()).deleteById(TestAlbumId);
		}
	}
}