package my.photomanager.photo.category;

import static my.photomanager.TestDataBuilder.TEST_PHOTO_CATEGORY_NAME;
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
class PhotoCategoryServiceTest {

	// TEST DATA
	final PhotoCategory TEST_PHOTO_CATEGORY = TestDataBuilder.TestPhotoCategoryBuilder.build();

	@Mock
	private PhotoCategoryRepository photoCategoryRepository;

	@InjectMocks
	private PhotoCategoryService photoCategoryService;

	@Test
	@DisplayName("should call saveAndFlush when photo category does not exist")
	void shouldCallSaveAndFlush() {
		// --- GIVEN ---
		when(photoCategoryRepository.findByName(TEST_PHOTO_CATEGORY_NAME)).thenReturn(Optional.empty());

		// --- WHEN ---
		photoCategoryService.saveOrGetPhotoCategory(TEST_PHOTO_CATEGORY);

		// --- THEN ---
		verify(photoCategoryRepository).saveAndFlush(TEST_PHOTO_CATEGORY);
	}

	@Test
	@DisplayName("should not call saveAndFlush when photo category already exists")
	void shouldNotCallSaveAndFlush() {
		// --- GIVEN ---
		when(photoCategoryRepository.findByName(TEST_PHOTO_CATEGORY_NAME)).thenReturn(Optional.of(TEST_PHOTO_CATEGORY));

		// --- WHEN ---
		photoCategoryService.saveOrGetPhotoCategory(TEST_PHOTO_CATEGORY);

		// --- THEN ---
		verify(photoCategoryRepository, never()).saveAndFlush(TEST_PHOTO_CATEGORY);
	}
}