package my.photoLibrary.core.library;

import static my.photoLibrary.TestDataBuilder.TestLibraryId;
import static my.photoLibrary.TestDataBuilder.TestLibraryName;
import static my.photoLibrary.TestDataBuilder.TestLibraryPath;
import static my.photoLibrary.TestDataBuilder.buildLibrary;
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
class LibraryServiceTest {

	@Mock
	private LibraryRepository repository;


	@InjectMocks
	private LibraryService service;

	@Test
	void shouldReturnAllLibraries() {
		// --- WHEN
		service.getAllLibraries();

		// --- THEN
		verify(repository).findAll();
	}

	@Nested
	class CreateLibraryTest {

		@Test
		void shouldCreateAndSaveLibrary() {
			// --- GIVEN ---
			when(repository.existsByPath(anyString())).thenReturn(false);

			// --- WHEN ---
			service.createAndSaveLibrary(TestLibraryName, TestLibraryPath);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Library.class));
		}

		@Test
		void shouldReturnExistingLibrary() {
			// --- GIVEN ---
			when(repository.existsByPath(anyString())).thenReturn(true);
			when(repository.findByPath(anyString())).thenReturn(Optional.of(buildLibrary()));

			// --- WHEN ---
			service.createAndSaveLibrary(TestLibraryName, TestLibraryPath);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Library.class));
		}
	}

	@Nested
	class EditLibraryTest {

		private final String newLibraryName = "newLibraryName";

		@Test
		void shouldEditAndSaveLibrary() {
			// --- GIVEN ---
			when(repository.findById(TestLibraryId)).thenReturn(Optional.of(buildLibrary()));
			when(repository.existsByPath(anyString())).thenReturn(false);

			// --- WHEN ---
			service.editLibrary(TestLibraryId, newLibraryName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Library.class));
		}

		@Test
		void shouldThrowExceptionWhenLibraryDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestLibraryId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(LibraryServiceException.class, () -> service.editLibrary(TestLibraryId, newLibraryName));
			verify(repository, never()).saveAndFlush(any(Library.class));
		}
	}

	@Nested
	class DeleteLibraryTest {

		@Test
		void shouldDeleteLibrary() {
			// --- GIVEN ---
			when(repository.findById(TestLibraryId)).thenReturn(Optional.of(buildLibrary()));

			// --- WHEN ---
			service.deleteLibrary(TestLibraryId);

			// --- THEN ---
			verify(repository).deleteById(TestLibraryId);
		}

		@Test
		void shouldThrowExceptionWhenLibraryDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestLibraryId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(LibraryServiceException.class, () -> service.deleteLibrary(TestLibraryId));
			verify(repository, never()).deleteById(TestLibraryId);
		}
	}
}