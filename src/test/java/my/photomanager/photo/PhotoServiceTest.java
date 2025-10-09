package my.photomanager.photo;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

	private PhotoService photoService;

	@Mock
	private PhotoRepository photoRepository;

	@BeforeEach
	void setUp() {
		photoService = new PhotoService(photoRepository);
	}

	@Test
	void shouldSavePhotoWhenNotExisting() {
		// given
		final var photoHashValue = "TestPhotoHashValue";
		final var photoFileName = "TestPhotoFileName";
		final var photoCreationDate = LocalDate.now();
		var photo = Photo.builder()
				.withFileName(photoFileName)
				.withHashValue(photoHashValue)
				.withCreationDate(photoCreationDate)
				.build();
		when(photoRepository.findByHashValue(photoHashValue)).thenReturn(Optional.empty());

		// when
		photoService.saveOrGetPhoto(photo);

		// then
		verify(photoRepository).saveAndFlush(photo);
	}

	@Test
	void shouldReturnExistingPhotoWhenAlreadyExists() {
		// given
		final var photoHashValue = "TestPhotoHashValue";
		final var photoFileName = "TestPhotoFileName";
		final var photoCreationDate = LocalDate.now();
		var photo = Photo.builder()
				.withFileName(photoFileName)
				.withHashValue(photoHashValue)
				.withCreationDate(photoCreationDate)
				.build();
		when(photoRepository.findByHashValue(photoHashValue)).thenReturn(Optional.of(photo));

		// when
		photoService.saveOrGetPhoto(photo);

		// then
		verify(photoRepository, never()).saveAndFlush(photo);
	}
}