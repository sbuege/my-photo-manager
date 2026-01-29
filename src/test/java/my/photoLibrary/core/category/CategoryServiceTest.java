package my.photoLibrary.core.category;

import static my.photoLibrary.TestDataBuilder.TestCategoryId;
import static my.photoLibrary.TestDataBuilder.TestCategoryName;
import static my.photoLibrary.TestDataBuilder.buildCategory;
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
class CategoryServiceTest {

	@Mock
	private CategoryRepository repository;

	@InjectMocks
	private CategoryService service;

	@Test
	void shouldReturnAllCategories() {
		// --- WHEN
		service.getAllCategories();

		// --- THEN
		verify(repository).findAll();
	}

	@Nested
	class CreateCategoryTest {

		@Test
		void shouldCreateAndSaveCategory() {
			// --- GIVEN ---
			when(repository.existsByName(anyString())).thenReturn(false);

			// --- WHEN ---
			service.createAndSaveCategory(TestCategoryName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Category.class));
		}

		@Test
		void shouldReturnExistingCategory() {
			// --- GIVEN ---
			when(repository.existsByName(anyString())).thenReturn(true);
			when(repository.findByName(anyString())).thenReturn(Optional.of(buildCategory()));

			// --- WHEN ---
			service.createAndSaveCategory(TestCategoryName);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Category.class));
		}
	}

	@Nested
	class EditCategoryTest {

		private final String newCategoryName = "newCategoryName";

		@Test
		void shouldEditAndSaveCategory() {
			// --- GIVEN ---
			when(repository.findById(TestCategoryId)).thenReturn(Optional.of(buildCategory()));
			when(repository.existsByName(anyString())).thenReturn(false);

			// --- WHEN ---
			service.editCategory(TestCategoryId, newCategoryName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Category.class));
		}

		@Test
		void shouldReturnExistingCategory() {
			// --- GIVEN ---
			when(repository.findById(TestCategoryId)).thenReturn(Optional.of(buildCategory()));
			when(repository.existsByName(newCategoryName)).thenReturn(true);
			when(repository.findByName(newCategoryName)).thenReturn(Optional.of(buildCategory(newCategoryName)));

			// --- WHEN ---
			service.editCategory(TestCategoryId, newCategoryName);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Category.class));
		}

		@Test
		void shouldThrowExceptionWhenCategoryDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestCategoryId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(CategoryServiceException.class, () -> service.editCategory(TestCategoryId, newCategoryName));
			verify(repository, never()).saveAndFlush(any(Category.class));
		}
	}

	@Nested
	class DeleteCategoryTest {

		@Test
		void shouldDeleteCategory() {
			// --- GIVEN ---
			when(repository.findById(TestCategoryId)).thenReturn(Optional.of(buildCategory()));

			// --- WHEN ---
			service.deleteCategory(TestCategoryId);

			// --- THEN ---
			verify(repository).deleteById(TestCategoryId);
		}

		@Test
		void shouldThrowExceptionWhenCategoryDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestCategoryId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(CategoryServiceException.class, () -> service.deleteCategory(TestCategoryId));
			verify(repository, never()).deleteById(TestCategoryId);
		}
	}
}