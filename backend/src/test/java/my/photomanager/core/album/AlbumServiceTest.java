package my.photomanager.core.album;

import static my.photomanager.TestDataBuilder.TestAlbumId;
import static my.photomanager.TestDataBuilder.TestAlbumName;
import static my.photomanager.TestDataBuilder.buildAlbum;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
			when(repository.saveAndFlush(any(Album.class))).thenReturn(buildAlbum());

			// --- WHEN ---
			service.createAndSaveAlbum(TestAlbumName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Album.class));
		}

		@ParameterizedTest
		@MethodSource("my.photomanager.TestDataBuilder#emptyNameProvider")
		void shouldReturnEmptyAlbumWhenNameIsEmpty(String albumName) {
			// --- WHEN ---
			assertThat(service.createAndSaveAlbum(albumName)).isEmpty();

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Album.class));
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
			var exception = assertThrows(AlbumServiceException.class, () -> service.editAlbum(TestAlbumId, newAlbumName));
			assertEquals(String.format("no album found with id %d", TestAlbumId), exception.getMessage());

			verify(repository, never()).saveAndFlush(any(Album.class));
		}

		@ParameterizedTest
		@MethodSource("my.photomanager.TestDataBuilder#emptyNameProvider")
		void shouldReturnEmptyAlbumWhenNameIsEmpty(String albumName) {
			// --- WHEN ---
			assertThat(service.editAlbum(TestAlbumId, albumName)).isEmpty();

			// --- THEN ---
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
			var exception = assertThrows(AlbumServiceException.class, () -> service.deleteAlbum(TestAlbumId));
			assertEquals(String.format("no album found with id %d", TestAlbumId), exception.getMessage());

			verify(repository, never()).deleteById(TestAlbumId);
		}
	}
}