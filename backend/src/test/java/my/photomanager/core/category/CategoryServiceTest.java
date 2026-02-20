package my.photomanager.core.category;

import static my.photomanager.TestDataBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import my.photomanager.core.cameraModel.CameraModel;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
			when(repository.saveAndFlush(any(Category.class))).thenReturn(buildCategory());

			// --- WHEN ---
			service.createAndSaveCategory(TestCategoryName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Category.class));
		}

		@ParameterizedTest
		@MethodSource("my.photomanager.TestDataBuilder#emptyNameProvider")
		void shouldReturnEmptyCameraModelWhenNameIsEmpty(String categoryName) {
			// --- WHEN ---
			assertThat(service.createAndSaveCategory(categoryName)).isEmpty();

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Category.class));
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
			when(repository.saveAndFlush(any(Category.class))).thenReturn(buildCategory(newCategoryName));

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

		@ParameterizedTest
		@MethodSource("my.photomanager.TestDataBuilder#emptyNameProvider")
		void shouldReturnEmptyCategoryWhenNameIsEmpty(String categoryName) {
			// --- WHEN ---
			assertThat(service.editCategory(TestCameraModelId, categoryName)).isEmpty();

			// --- THEN ---
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