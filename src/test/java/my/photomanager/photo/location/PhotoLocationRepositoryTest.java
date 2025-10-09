package my.photomanager.photo.location;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class PhotoLocationRepositoryTest {

	@Autowired
	private PhotoLocationRepository photoLocationRepository;

	@Test
	void shouldThrowExceptionWhenSavingPhotoLocationWithDuplicateCountryAndCity() {
		// given
		final var photoLocationCountry = "TestPhotoCountry";
		final var photoLocationCity = "TestPhotoCity";
		var photoLocation1 = new PhotoLocation(photoLocationCountry, photoLocationCity);
		var photoLocation2 = new PhotoLocation(photoLocationCountry, photoLocationCity);
		photoLocationRepository.saveAndFlush(photoLocation1);

		// when / then
		assertThrows(DataIntegrityViolationException.class, () -> photoLocationRepository.saveAndFlush(photoLocation2));
	}
}