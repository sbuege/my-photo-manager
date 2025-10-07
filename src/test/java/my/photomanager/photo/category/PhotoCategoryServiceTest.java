package my.photomanager.photo.category;

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
class PhotoCategoryServiceTest {

	private PhotoCategoryService photoCategoryService;

	@Mock
	private PhotoCategoryRepository photoCategoryRepository;

	@BeforeEach
	void setUp() {
		photoCategoryService = new PhotoCategoryService(photoCategoryRepository);
	}

	@Test
	void shouldSavePhotoCategoryWhenNotExisting() {
		// given
		final var photoCategoryName = "TestPhotoCategory";
		var photoCategory = new PhotoCategory(photoCategoryName);
		when(photoCategoryRepository.findByName(photoCategoryName)).thenReturn(Optional.empty());

		// when
		photoCategoryService.saveOrGetPhotoCategory(photoCategory);

		// then
		verify(photoCategoryRepository).saveAndFlush(photoCategory);
	}

	@Test
	void shouldReturnExistingPhotoCategoryWhenAlreadyExists() {
		// given
		final var photoCategoryName = "TestPhotoCategory";
		var photoCategory = new PhotoCategory(photoCategoryName);
		when(photoCategoryRepository.findByName(photoCategoryName)).thenReturn(Optional.of(photoCategory));

		// when
		photoCategoryService.saveOrGetPhotoCategory(photoCategory);

		// then
		verify(photoCategoryRepository, never()).saveAndFlush(photoCategory);
	}
}