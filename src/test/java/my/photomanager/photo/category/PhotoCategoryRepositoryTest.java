package my.photomanager.photo.category;

import static org.junit.jupiter.api.Assertions.assertThrows;

import my.photomanager.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@Import(TestUtils.PhotoIndexerMock.class)
class PhotoCategoryRepositoryTest {

	@Autowired
	private PhotoCategoryRepository photoCategoryRepository;

	@Test
	void shouldThrowExceptionWhenSavingPhotoCategoryWithDuplicateName() {
		// given
		final var photoCategoryName = "TestPhotoCategory";
		var photoCategory1 = new PhotoCategory(photoCategoryName);
		var photoCategory2 = new PhotoCategory(photoCategoryName);
		photoCategoryRepository.saveAndFlush(photoCategory1);

		// when / then
		assertThrows(DataIntegrityViolationException.class, () -> photoCategoryRepository.saveAndFlush(photoCategory2));
	}
}