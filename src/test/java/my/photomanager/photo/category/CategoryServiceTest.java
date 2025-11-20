package my.photomanager.photo.category;

import static my.photomanager.TestDataBuilder.TEST_CATEGORY_NAME;
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
class CategoryServiceTest {

	// TEST DATA
	final Category TEST_CATEGORY = TestDataBuilder.TestPhotoCategoryBuilder.build();

	@Mock
	private CategoryRepository repository;

	@InjectMocks
	private CategoryService service;

	@Test
	@DisplayName("should call saveAndFlush when category does not exist")
	void shouldCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.empty());

		// --- WHEN ---
		service.saveOrGetCategory(TEST_CATEGORY);

		// --- THEN ---
		verify(repository).saveAndFlush(TEST_CATEGORY);
	}

	@Test
	@DisplayName("should not call saveAndFlush when category already exists")
	void shouldNotCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(TEST_CATEGORY));

		// --- WHEN ---
		service.saveOrGetCategory(TEST_CATEGORY);

		// --- THEN ---
		verify(repository, never()).saveAndFlush(TEST_CATEGORY);
	}
}