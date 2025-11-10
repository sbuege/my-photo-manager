package my.photomanager.photo.category;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhotoCategoryServiceTest {

	// TEST DATA
	final String TEST_PHOTO_CATEGORY_NAME = "TestPhotoCategory";
	final PhotoCategory TEST_PHOTO_CATEGORY = new PhotoCategory(TEST_PHOTO_CATEGORY_NAME);

	@Mock
	private PhotoCategoryRepository photoCategoryRepository;

	@InjectMocks
	private PhotoCategoryService photoCategoryService;

	@Test
	@DisplayName("should call saveAndFlush when photo category does not exist")
	void shouldCallSaveAndFlush() {
		// given
		when(photoCategoryRepository.findByName(TEST_PHOTO_CATEGORY_NAME)).thenReturn(Optional.empty());

		// when
		photoCategoryService.saveOrGetPhotoCategory(TEST_PHOTO_CATEGORY);

		// then
		verify(photoCategoryRepository).saveAndFlush(TEST_PHOTO_CATEGORY);
	}

	@Test
	@DisplayName("should never all saveAndFlush when camera settings exists already")
	void shouldNeveCallSaveAndFlush() {
		// given

		when(photoCategoryRepository.findByName(TEST_PHOTO_CATEGORY_NAME)).thenReturn(Optional.of(TEST_PHOTO_CATEGORY));

		// when
		photoCategoryService.saveOrGetPhotoCategory(TEST_PHOTO_CATEGORY);

		// then
		verify(photoCategoryRepository, never()).saveAndFlush(TEST_PHOTO_CATEGORY);
	}
}