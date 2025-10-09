package my.photomanager.photo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class PhotoRepositoryTest {

	@Autowired
	private PhotoRepository photoRepository;

	@Test
	void shouldThrowExceptionWhenSavingPhotoWithDuplicateHashValue() {
		// given
		final var photoHashValue = "TestPhotoHashValue";
		final var photoFileName = "TestPhotoFileName";
		final var photoCreationDate = LocalDate.now();
		var photo1 = Photo.builder()
				.withFileName(photoFileName)
				.withHashValue(photoHashValue)
				.withCreationDate(photoCreationDate)
				.build();
		var photo2 = Photo.builder()
				.withFileName(photoFileName)
				.withHashValue(photoHashValue)
				.withCreationDate(photoCreationDate)
				.build();
		photoRepository.saveAndFlush(photo1);

		// when / then
		assertThrows(DataIntegrityViolationException.class, () -> photoRepository.saveAndFlush(photo2));
	}
}